package com.bld.project.system.block;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.framework.web.domain.ResultListInfo;
import com.bld.framework.web.domain.Results;
import com.bld.project.system.block.mapper.BlockDeviceMapper;
import com.bld.project.system.block.mapper.BlockHashMapper;
import com.bld.project.system.block.model.Block;
import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.block.model.BlockHash;
import com.bld.project.system.block.service.BlockDeviceService;
import com.bld.project.system.block.service.BlockHashService;
import com.bld.project.system.device.model.TbDevice;
import com.bld.project.system.device.service.DeviceService;
import com.bld.project.system.telemetry.service.TelemetryService;
import com.bld.project.system.user.domain.ThingsboardUser;
import com.bld.project.utils.BlockUtils;
import com.bld.project.utils.ListQuery;
import com.bld.project.utils.RegularUtils;
import com.bld.project.utils.TbApiUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("block")
@RestController
public class BlockController {
    @Resource
    private BlockDeviceService blockDeviceService;
    @Resource
    private BlockHashService blockHashService;

    @GetMapping("searchBlock.json")
    public ResultListInfo searchBlock(ListQuery lq){
        BlockDevice bd = new BlockDevice(lq.getPageNum(), lq.getLimit());
        bd.setSearch(lq.getSearch());
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        if (!tbUser.isSysAdmin()){
            bd.setTenantId(tbUser.getTenantId().getId());
            if (!tbUser.isTenantAdmin()){
                bd.setCustomerId(tbUser.getCustomerId().getId());
            }
        }
        return blockDeviceService.select(bd);
    }

    @GetMapping("searchBlockHash.json")
    public ResultListInfo searchBlockHash(BlockHash bh){
        return blockHashService.select(bh);
    }

    @GetMapping("searchTableMoneyData.json")
    public ResultListInfo searchTableMoneyData(BlockHash bh){
        return blockHashService.searchTableMoneyData(bh);
    }

    @GetMapping("searchDeviceGps.json")
    public ResultInfo searchDeviceGps(String chipId, String cId, String tId){
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        if (!tbUser.isSysAdmin()){
            tId = tbUser.getTenantId().getId();
            if (!tbUser.isTenantAdmin()){
                cId = tbUser.getCustomerId().getId();
            }
        }
        return blockDeviceService.searchDeviceGps(chipId, cId, tId);
    }

    @PostMapping("updateDeviceGps.json")
    public ResultInfo updateDeviceGps(@RequestBody BlockDevice bd){
        String chipId = bd.getChipId();
        if (StringUtils.isNullString(chipId)){
            return ResultInfo.error("没有获取到chipId");
        }
        String gps = bd.getGps();
        if (gps != null && !RegularUtils.verifyReg(RegularUtils.COORDINATE, gps)){
            return ResultInfo.error("参数错误");
        }
        return blockDeviceMapper.updateGps(gps, bd.getAddr(), bd.getChipId()) > 0 ? ResultInfo.success("") : ResultInfo.error("修改失败");
    }

    @GetMapping("blockHashInfo.json")
    @ResponseBody
    public ResultInfo getDeviceTypes(String hash){
        if (StringUtils.isNullString(hash)){
            return ResultInfo.error("参数错误");
        }
        ResultInfo<JSONObject> resultInfo = BlockUtils.getBlockHashInfo(hash);
        if (resultInfo.isSuccess()){
            JSONObject json = resultInfo.getData();
            JSONObject result = json.getJSONObject("result");
            String blockNumber = result.getString("blockNumber");
            ResultInfo<JSONObject> blockInfo = BlockUtils.getBlockNumberByNumber(blockNumber);
            if (blockInfo.isSuccess()){
                JSONObject bj = blockInfo.getData();
                JSONObject result1 = bj.getJSONObject("result");
                String time = result1.getString("timestamp");
                result.put("timestamp", time);
            }
            ResultInfo<JSONObject> bnr= BlockUtils.getBlockNumber();
            if (bnr.isSuccess()){
                JSONObject data = bnr.getData();
                String bns = data.getString("result");
                result.put("newBlockNum", bns);
            }

            json.put("result", result);
            resultInfo.setData(json);
        }
        return resultInfo;
    }

    @GetMapping("getBlockInfo.json")
    @ResponseBody
    public ResultInfo getBlockInfo(long blockNum){
        return BlockUtils.getBlockNumberByNumber(blockNum + "");
    }



// -----------------------------------测试用api--------------------------------------------
    @Resource
    private BlockDeviceMapper blockDeviceMapper;
    @Resource
    private DeviceService deviceService;

    @Value("${block.peerUrl1}")
    private String peerUrl1;
    @Value("${block.password}")
    private String walletPassword;
    @Value("${block.balance}")
    private String balance;

    /**
     * @author SOFAS
     * @date 2020/7/7
     * @directions  获取所有设备并添加到bld中
    */
    @GetMapping("updateData.json")
    public void dataSynchronous() throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        String tbToken = new TbApiUtils().loginTb();
        List<BlockDevice> select = blockDeviceMapper.select(null);
        ResultInfo<List<TbDevice>> resultInfo = deviceService.searchDevice(10000, "", ShiroUtils.getTbToken(),"","");
        if (!resultInfo.isSuccess()){
            return;
        }
        List<TbDevice> list = resultInfo.getData();
        String tentId;
        String customerId;
        String token = null;
        for (TbDevice td : list){
            String id = td.getId().getId();
            String name = td.getName();
            boolean add = true;
            for (BlockDevice blockDevice : select) {
                if (blockDevice.getChipId().equals(name) || blockDevice.getDeviceId().equals(id)) {
                    add = false;
                    ResultInfo<String> dtr = deviceService.getDeviceToken(blockDevice.getDeviceId(), tbToken);
                    if (dtr.isSuccess() && !dtr.getData().equals(blockDevice.getDeviceToken())){
                        BlockDevice updateBd = new BlockDevice();
                        updateBd.setDeviceToken(dtr.getData());
                        blockDeviceMapper.update(updateBd, new BlockDevice(blockDevice.getChipId()));
                    }
                    break;
                }
            }

            if (add){
                customerId = td.getCustomerId().getId();
                tentId = td.getTenantId().getId();
//                获取转账钱包
                String coinbaseJson = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_coinbase\",\"params\":[],\"id\":64}";
                String coinbaseStr = OkHttpUtil.postJsonParams1(peerUrl1, coinbaseJson);
                String coinbase = JSONObject.parseObject(coinbaseStr).getString("result");
//                新建设备钱包
                Block wallet = BlockUtils.getWallet();

//                获取设备token
                ResultInfo<String> deviceToken = deviceService.getDeviceToken(id, ShiroUtils.getTbToken());
                token = deviceToken.isSuccess() ? deviceToken.getData() : token;
                BlockDevice blockDevice = new BlockDevice(id, tentId, customerId, name, null, token, coinbase, wallet.getAddr(), null, null, 0);
                blockDeviceMapper.add(blockDevice);
            }
        }
    }

    @Resource
    private TelemetryService telemetryService;
    @Resource
    private BlockHashMapper blockHashMapper;

    /**
     * @author SOFAS
     * @date 2020/7/7
     * @directions  获取所有上链历史数据并添加到bld中
    */
    @GetMapping("getHistory.json")
    @ResponseBody
    public void getHistory(){
        String substring = balance.substring(2);
        BigInteger bigInteger = new BigInteger(substring, 16);
        long addMoney = bigInteger.longValue();
        List<BlockDevice> select = blockDeviceMapper.select(new BlockDevice());
        for (BlockDevice bd : select){
            ResultInfo<JSONObject> thr = telemetryService.getTelemetrys("Txhash", bd.getDeviceId());
            boolean f = thr.getData().toString().contains("没有该设备");
            if (thr.isSuccess() && !f){
                JSONObject json = thr.getData();
                String txHashs= json.getString("Txhash");
                if (!StringUtils.isNullString(txHashs)){
                    List list = JSONObject.parseObject(txHashs, List.class);
                    int i = 0;
                    for (Object o : list){
                        JSONObject json1 = JSONObject.parseObject(o.toString());
                        String txHash = json1.getString("value");
                        Date ts = json1.getDate("ts");
                        List<BlockHash> select1 = blockHashMapper.select(new BlockHash(bd.getChipId(), txHash, null, null, null, null));
                        if (select1 == null || select1.size() < 1){
                            blockHashMapper.add(new BlockHash(bd.getChipId(), txHash, bd.getDeviceWallet(), bd.getToWallet(), addMoney, ts));
                            i++;
                        }else {
                            BlockHash update = new BlockHash();
                            update.setAddTime(ts);
                            BlockHash where = new BlockHash();
                            where.setHash(txHash);
                            blockHashMapper.update(update, where);
                        }
                    }
                    if (i > 0){
                        BlockDevice update = new BlockDevice();
                        update.setDeviceMoney(update.getDeviceMoney() == null ? i * addMoney : update.getDeviceMoney() + i * addMoney);
                        blockDeviceMapper.update(update, new BlockDevice(bd.getChipId()));
                    }
                }
            }else{
                if (f){
                    blockDeviceMapper.delete(new BlockDevice(bd.getChipId()));
                }
            }

        }

    }

    @GetMapping("newWallet.json")
    @ResponseBody
    public ResultInfo newWallet() throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        boolean f = ShiroUtils.getSysUser().getThingsboardUser().isTenantAdmin() || ShiroUtils.getSysUser().getThingsboardUser().isSysAdmin();
        if (f){
            Block wallet = BlockUtils.getWallet();
            return ResultInfo.success(wallet);
        }else {
            return ResultInfo.error("操作权限不足");
        }
    }

    @GetMapping("balance.json")
    @ResponseBody
    public ResultInfo balance(String wallet){
        if (!ShiroUtils.getSysUser().getThingsboardUser().isTenantAdmin() && !ShiroUtils.getSysUser().getThingsboardUser().isSysAdmin()){
            return ResultInfo.error("没有权限");
        }
        long balance = BlockUtils.getBalance(wallet);
        return ResultInfo.success(balance);
    }

//    -------------------------------------------besu测试----------------------------------------------------

    /**
     * thingsboard地址
     */
    @Value("${app.url}")
    private String url;
    /**
     * thingsboard账户
     */
    @Value("${tenat.userName}")
    private String auotoAddUserName;
    /**
     * thingsboard密码
     */
    @Value("${tenat.password}")
    private String auotoAddPassword;

    @PostMapping("creatWalletTest.json")
    @ResponseBody
    public Results save(@RequestBody String json) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        Results<Object> result = new Results<>();
        result.setSmartMeter1("registration failed");
        JSONObject j = JSONObject.parseObject(json);
        String chipId = j.getString("ChipID");
        String apiKey = j.getString("APIKEY");
        String wv = j.getString("WorkingVersion");
        String sn1 = j.getString("SN1");
        String sn2 = j.getString("SN2");
        String sn = j.getString("SN");
        /*有时会只传一个sn进来，如果只有一个sn，默认为sn1*/
        if (StringUtils.isNullString(sn1) && !StringUtils.isNullString(sn)){
            sn1 = sn;
        }
        /*参数检查*/
        if (StringUtils.isNullString(chipId) || StringUtils.isNullString(apiKey)){
            return result;
        }
        if (!"tPmAT5Ab3j7F9".equals(apiKey)) {
            return result;
        }
        List<BlockDevice> select = blockDeviceMapper.select(new BlockDevice(chipId));
        if (select != null && select.size() > 0) {
            BlockDevice bd = select.get(0);
            BlockDevice update = new BlockDevice();
            update.setDeviceVersion(wv);
            boolean isUpdate = false;
            if (!StringUtils.isNullString(wv)){
                isUpdate = true;
                update.setDeviceVersion(wv);
            }
            if (!StringUtils.isNullString(sn1)){
                isUpdate = true;
                update.setSn1(sn1);
            }else {
                sn1 = bd.getSn1();
            }
            if (!StringUtils.isNullString(sn2)){
                isUpdate = true;
                update.setSn2(sn2);
            }else {
                sn2 = bd.getSn2();
            }
            if (StringUtils.isNullString(bd.getDeviceWallet())){
                Block wallet = BlockUtils.getWallet();
                String addr = wallet.getAddr();
                if (!StringUtils.isNullString(addr)){
                    update.setDeviceWallet(addr);
                }
            }

            return isUpdate && blockDeviceMapper.update(update, new BlockDevice(chipId)) < 0 ? result : new Results<>(sn1, sn2, bd.getDeviceToken(), bd.getToWallet(), bd.getDeviceWallet());
        } else {
            /*登陆自动注册帐号获取token*/
            String url1 = url + "/api/auth/login";
            String jsonParams = "{\"username\":\"" + auotoAddUserName + "\"," + "\"password\":\"" + auotoAddPassword + "\"}";
            String s = OkHttpUtil.postJsonParams(url1, jsonParams, "");
            JSONObject loginJ = JSONObject.parseObject(s);
            Object tokenO = loginJ.get("token");
            String token;
            if (StringUtils.isNullString(tokenO)){
                return result;
            }
            token = "Bearer " + tokenO.toString();
            /*注册设备*/
            BlockDevice bd = new BlockDevice(chipId);
            List<BlockDevice> bds = blockDeviceMapper.select(bd);
            if(bds == null || bds.size() < 1){
                boolean isBesu = true;
                ResultInfo resultInfo = deviceService.autoAddDevice(chipId, null, token, wv, sn1, sn2, isBesu);
                if (!resultInfo.isSuccess()){
                    return result;
                }
                bd = JSONObject.parseObject(JSONObject.toJSONString(resultInfo.getData()), BlockDevice.class);
            }else {
                bd = bds.get(0);
            }
            return new Results<>(sn1, sn2, bd.getDeviceToken(), bd.getToWallet(), bd.getDeviceWallet());
        }
    }

    @PostMapping("rawTransaction.json")
    @ResponseBody
    public ResultInfo besuRawTransaction(@RequestBody JSONObject json){
        String rawTransaction = json.getString("rawTransaction");
        String jp = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendRawTransaction\",\"params\":[\"" + rawTransaction + "\"],\"id\":1}";
        String s = OkHttpUtil.postJsonParams1("http://125.64.98.21:6006", jp);
        JSONObject result = JSONObject.parseObject(s);
        String resultS = result.getString("result");
        if (StringUtils.isNullString(resultS)){
            String message = result.getString("message");
            return ResultInfo.error(message);
        }
        return ResultInfo.success(resultS);
    }

    @GetMapping("creatNewWallet.json")
    @ResponseBody
    public ResultInfo creatNewBesuWallet() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {
        Block wallet = BlockUtils.getWallet();
        return ResultInfo.success(wallet);
    }

    @PostMapping("importWallet.json")
    @ResponseBody
    public ResultInfo importWallet(@RequestBody JSONObject json) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        String privateKey = json.getString("privateKey");
        Block wallet = BlockUtils.getWallet(privateKey);
        return ResultInfo.success(wallet);
    }

    @GetMapping("walletChange.json")
    @ResponseBody
    public ResultInfo walletChange() throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        List<BlockDevice> select = blockDeviceMapper.select(new BlockDevice());
        for (BlockDevice bd : select){
            BlockDevice bdUp = new BlockDevice();
            bdUp.setToWallet("a963d384cac4927a4f632be6d51c74e3d549538f");
            Block wallet = BlockUtils.getWallet();
            bd.setDeviceWallet(wallet.getAddr());
            BlockDevice wh = new BlockDevice();
            wh.setId(bd.getId());
            blockDeviceMapper.update(bdUp, wh);
        }
        return ResultInfo.success(null, "修改完成");
    }

    @PostMapping("getDeviceByToken.json")
    @ResponseBody
    public ResultInfo getDeviceByToken(@RequestBody JSONObject param){
        String dt = param.getString("deviceToken");
        if (StringUtils.isNullString(dt)){
            return ResultInfo.error("参数错误");
        }
        BlockDevice sbd = new BlockDevice();
        sbd.setDeviceToken(dt);
        List<BlockDevice> bds = blockDeviceMapper.select(sbd);
        return bds == null ? ResultInfo.error("没有查询到数据") : ResultInfo.success(bds.get(0).getDeviceWallet());
    }
}