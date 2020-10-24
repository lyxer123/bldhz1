package com.bld.project.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.project.system.block.model.Block;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bld.framework.utils.Hex;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author SOFAS
 * @date 2020/7/10
 * @directions  区块链通用工具类
*/
@Slf4j
@Component
public class BlockUtils {
    /**
     * 区块链地址
     */
    private static String peerUrl1;
    /**
     * 文件密码
     */
    private static String blockPassword;

    /**
     * 默认gas
     */
    public static BigInteger gas = new BigInteger("1000", 10);
    public static BigInteger gas_limit = new BigInteger("47b760", 16);

    public static HashMap<String, String> collectMap = new HashMap<>();

    @Value("${block.peerUrl1}")
    private void setPeerUrl1(String url1){
        peerUrl1 = url1;
    }

    @Value("${block.sepriPassword}")
    private void setWalletPassword(String password){
        blockPassword = password;
    }

//    -----------------------------------------------------sepri------------------------------------------------------

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  创建一个新的钱包
     * @return  com.bld.project.system.block.model.Block
     */
    public static Block getWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException {
        return getWallet(null);
    }

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  如果有私钥则导入钱包，没有私钥则创建钱包
     * @param privateKey  私钥
     * @return  com.bld.project.system.block.model.Block
     */
    public static Block getWallet(String privateKey) throws CipherException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        /*生成的密钥文件存放路径*/
        String destinationDirectory = System.getProperty("os.name").toLowerCase().contains("windows") ? "E:\\testData" : "/usr/local/web/bld/keys/";

        /*创建钱包并生成密钥文件*/
        ECKeyPair ecKeyPair = StringUtils.isNullString(privateKey) ? Keys.createEcKeyPair() : ECKeyPair.create(new BigInteger(privateKey, 16));

        /*完全加密 useFullScrypt*/
        WalletFile walletFile = Wallet.createStandard(blockPassword, ecKeyPair);;

        String fileName = getWalletFileName(walletFile);
        File destination = new File(destinationDirectory, fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(destination, walletFile);

        return new Block(ecKeyPair.getPrivateKey().toString(), ecKeyPair.getPublicKey().toString(), walletFile.getAddress());
    }

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  获取指定地址的余额
     * @param wallet  钱包地址
     * @return  long
     */
    public static long getBalance(String wallet){
        //获取余额
        String jp = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBalance\",\"params\":[\"" + wallet + "\", \"latest\"],\"id\":1}";
        String s = OkHttpUtil.postJsonParams1(peerUrl1, jp);
        JSONObject json = JSONObject.parseObject(s);
        String result = json.getString("result");
        String substring = result.substring(2);
        BigInteger big = new BigInteger(substring.trim(), 16);
        return big.longValue();
    }

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  发起交易
     * @param sendAddress  出款地址
     * @param toAddress  收款地址
     * @param value  金额
     * @param gas  消耗gas
     * @param gasLimit  gas上限
     * @param data  自定义数据
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    public static ResultInfo<String> blockTransaction(String sendAddress, String toAddress, BigInteger value, BigInteger gas, BigInteger gasLimit, String data) throws IOException, CipherException {
        String rawTransaction = signTransaction(sendAddress, toAddress, value, gas, gasLimit, data);
        String jp = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendRawTransaction\",\"params\":[\"" + rawTransaction + "\"],\"id\":1}";
        String s = OkHttpUtil.postJsonParams1(peerUrl1, jp);
        JSONObject result = JSONObject.parseObject(s);
        String resultS = result.getString("result");
        if (resultS == null){
            String error = result.getString("error");
            JSONObject err = JSONObject.parseObject(error);
            Object message = err.get("message");
            log.error("区块链请求错误，错误信息：{}", message);
        }

        return StringUtils.isNullString(resultS) ? ResultInfo.error(result.getString("message")) : ResultInfo.success(resultS);
    }

    /**
     * @author SOFAS
     * @date   2020/7/14
     * @directions  查询hash信息
     * @param hash  hash
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    public static ResultInfo<JSONObject> getBlockHashInfo(String hash){
        String param = "{\"id\": 854, \"jsonrpc\": \"2.0\", \"method\": \"eth_getTransactionByHash\", \"params\": [\"" + hash + "\"]}";
        return OkHttpUtil.bldPostJsonParams(peerUrl1, param);
    }

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  获取最新的区块高度
     * @return  com.bld.framework.web.domain.ResultInfo<com.alibaba.fastjson.JSONObject>
     */
    public static ResultInfo<JSONObject> getBlockNumber(){
        String param = "{\"id\": 854, \"jsonrpc\": \"2.0\", \"method\": \"eth_blockNumber\", \"params\": []}";
        return OkHttpUtil.bldPostJsonParams(peerUrl1, param);
    }

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  根据区块的hash获取指定的区块
     * @param hash 待查询的hash
     * @return  com.bld.framework.web.domain.ResultInfo<com.alibaba.fastjson.JSONObject>
     */
    public static ResultInfo<JSONObject> getBlockNumberByHash(String hash){
        String param = "{\"id\": 854, \"jsonrpc\": \"2.0\", \"method\": \"eth_getBlockByHasH\", \"params\": [\"" + hash + "\", \"true\"]}";
        return OkHttpUtil.bldPostJsonParams(peerUrl1, param);
    }

    /**
     * @author SOFAS
     * @date   2020/8/27
     * @directions  根据区块的hash获取指定的区块
     * @param hash 待查询的hash
     * @return  com.bld.framework.web.domain.ResultInfo<com.alibaba.fastjson.JSONObject>
     */
    public static ResultInfo<JSONObject> getBlockNumberByNumber(String hash){
        String param = "{\"id\": 854, \"jsonrpc\": \"2.0\", \"method\": \"eth_getBlockByNumber\", \"params\": [\"" + hash + "\", \"false\"]}";
        return OkHttpUtil.bldPostJsonParams(peerUrl1, param);
    }

    private static String getWalletFileName(WalletFile walletFile) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return now.format(format) + walletFile.getAddress() + ".json";
    }

    /**
     * @author SOFAS
     * @date   2020/8/12
     * @directions  besu对交易签名
     * @param sendAddress  出款地址
     * @param toAddress  收款地址
     * @param value  转账值
     * @param gas  消耗气体
     * @param gasLimit  气体上线
     * @param data  自定义交易信息
     * @return  java.lang.String
     */
    private static String signTransaction(String sendAddress, String toAddress, BigInteger value, BigInteger gas, BigInteger gasLimit, String data) throws IOException, CipherException{
        sendAddress = sendAddress.contains("0x") ? sendAddress.substring(2) : sendAddress;
        String dataHex = "0x" + Hex.str2HexStr(data);
        String destinationDirectory = System.getProperty("os.name").toLowerCase().contains("windows") ? "E:\\testData/" : "/usr/local/web/bld/keys/";
        File dir = new File(destinationDirectory);
        File[] files = dir.listFiles();
        if (files == null){
            return null;
        }
        boolean have = false;
        String fileName = "";
        for (File f : files){
            fileName = f.getName();
            if (fileName.toLowerCase().contains(sendAddress)){
                have = true;
                break;
            }
        }
        if (!have){
            return null;
        }
        String file = System.getProperty("os.name").toLowerCase().contains("windows") ? "E:\\testData\\UTC--2020-08-12T06-13-42.511504400Z--a963d384cac4927a4f632be6d51c74e3d549538f.json" : destinationDirectory + fileName;
        Credentials credentials = WalletUtils.loadCredentials("bld", file);

        // get the next available nonce
        String jp = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getTransactionCount\",\"params\":[\"" + sendAddress + "\", \"latest\"],\"id\":1}";
        String s = OkHttpUtil.postJsonParams1("http://125.64.98.21:6006", jp);
        JSONObject json = JSONObject.parseObject(s);
        String nonceS = json.getString("result");
        BigInteger nonce = new BigInteger(nonceS.substring(2), 16);

        // 创建交易实体
        RawTransaction rawTransaction  = RawTransaction.createTransaction(nonce, gas, gasLimit, toAddress, value, dataHex);
        // 对交易进行签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }
}
