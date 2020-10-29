package com.bld.project.system.block.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.system.block.mapper.BlockDeviceMapper;
import com.bld.project.system.block.mapper.HzBlockDeviceMapper;
import com.bld.project.system.block.model.Block;
import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.customer.service.CustomerService;
import com.bld.project.system.device.service.DeviceService;
import com.bld.project.utils.BlockUtils;
import com.bld.project.utils.ListQuery;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author SOFAS
 * @date 2020/7/3
 * @directions 区块链设备信息实现
 */
@Slf4j
@Service
public class BlockDeviceServiceImpl implements BlockDeviceService {
    @Resource
    private BlockDeviceMapper blockDeviceMapper;
    @Resource
    private DeviceService deviceService;

    @Autowired
    private HzBlockDeviceMapper hzBlockDeviceMapper;

    @Value("${block.peerUrl1}")
    private String peerUrl1;
    @Value("${block.password}")
    private String walletPassword;

    @Override
    public ResultListInfo select(BlockDevice bd) {
        int pageNum = bd.getPageNum();
        bd.setPageNum((bd.getPageNum()-1)*bd.getPageSize());
        int pageSize = bd.getPageSize();
        List<BlockDevice> select = blockDeviceMapper.select(bd);
        Integer integer = blockDeviceMapper.selectCount(bd);
        return ResultListInfo.success(select, "查询成功", integer, pageNum, pageSize);
    }

    @Resource
    private CustomerService customerService;
    @Override
    public ResultListInfo searchDeviceGps(String chipId, String cId, String tId) {
        if (StringUtils.isNullString(chipId)){
            chipId = null;
        }
        BlockDevice selectBd = new BlockDevice(chipId);
        selectBd.setSelectGps(true);
        selectBd.setCustomerId(cId);
        selectBd.setTenantId(tId);
        List<BlockDevice> select = blockDeviceMapper.select(selectBd);
        if (select != null){
            /*目前系统网络情况较差，所以减少请求发送*/
            HashMap<String, String> idMap = new HashMap<>();
            for (BlockDevice bd : select){
                String cId1 = bd.getCustomerId();
                idMap.putIfAbsent(cId1, cId1);
            }

            ArrayList<String> ids = new ArrayList<>(idMap.keySet());
            ResultInfo<JSONObject> customerName = customerService.getCustomerName(ids);
            JSONObject data = customerName.getData();
            for (BlockDevice bd : select){
                String cId1 = bd.getCustomerId();
                bd.setCustomerName(data.getString(cId1));
            }
        }
        return ResultListInfo.success(select, null, 0, 0, 0);
    }

    @Override
    public ResultInfo addBlockDevice(BlockDevice bd, String tbToken) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        String s = bd.addCheck();
        if (s != null){
            return ResultInfo.error(s);
        }
        if (bd.getIsBesu()){
            try {
                Block wallet = BlockUtils.getWallet();
                bd.setDeviceWallet(wallet.getAddr());
            } catch (Exception e) {
                e.printStackTrace();
                return ResultInfo.error("添加失败");
            }
            bd.setToWallet("0xa963D384CAC4927a4f632BE6D51C74E3D549538f");
        }else {
            //        获取转账钱包
            String coinbaseJson = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_coinbase\",\"params\":[],\"id\":64}";
            String coinbaseStr = OkHttpUtil.postJsonParams1(peerUrl1, coinbaseJson);
            String coinbase = JSONObject.parseObject(coinbaseStr).getString("result");
            bd.setToWallet(coinbase);
//        新建设备钱包
            Block wallet = BlockUtils.getWallet();
            bd.setDeviceWallet(wallet.getAddr());
        }

//        获取转账钱包
        String coinbaseJson = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_coinbase\",\"params\":[],\"id\":64}";
        String coinbaseStr = OkHttpUtil.postJsonParams1(peerUrl1, coinbaseJson);
        String coinbase = JSONObject.parseObject(coinbaseStr).getString("result");
        bd.setToWallet(coinbase);
//        新建设备钱包
        Block wallet = BlockUtils.getWallet();
        bd.setDeviceWallet(wallet.getAddr());

//        获取设备token
        ResultInfo<String> deviceToken = deviceService.getDeviceToken(bd.getDeviceId(), tbToken);
        bd.setDeviceToken(deviceToken.isSuccess() ? deviceToken.getData() : bd.getDeviceToken());

        return blockDeviceMapper.add(bd) > 0 ? ResultInfo.success(bd, "添加成功") : ResultInfo.error("添加失败");
    }

    @Async
    @Override
    public void asyncAddBlockDevice(BlockDevice bd) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException, IOException {
        ResultInfo resultInfo = addBlockDevice(bd, ShiroUtils.getTbToken());
        if (!resultInfo.isSuccess()){
            log.error("添加新设备区块链信息失败，返回: {}，添加数据: {}", resultInfo.getMessage(), bd);
        }
    }

    @Override
    public boolean updateBlockDevice(BlockDevice updateBd, BlockDevice whereBd) {
        return blockDeviceMapper.update(updateBd, whereBd) > 0;
    }

    @Async
    @Override
    public void asyncUpdateBlockDevice(BlockDevice updateBd, BlockDevice whereBd) {
        if (!updateBlockDevice(updateBd, whereBd)){
            log.error("区块链设备信息更新失败，更新参数：{}，更新条件：{}", JSONObject.toJSONString(updateBd), JSONObject.toJSONString(whereBd));
        }
    }
}
