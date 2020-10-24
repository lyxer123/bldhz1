//import com.alibaba.fastjson.JSONObject;
//import com.bld.bldApplication;
//import com.bld.common.utils.DateUtils;
//import com.bld.framework.web.domain.ResultInfo;
//import com.bld.project.system.xitaChain.utils.XiTaChainUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Base64;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//
//@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = bldApplication.class)
//public class XiTaTest {
//
//    @Test
//    public void test1(){
////        bldTest1: {"code":200,"data":{"exception":"","code":200,"data":{"accessKey":"oYjNLXVmxaynPyeB","accessSecret":"dB2E3FNwY4gyqZBR8zUC2dlHanCakpoQ"},"message":"success"},"message":"操作成功","success":true}
////        bldTest2: {"code":200,"data":{"exception":"","code":200,"data":{"accessKey":"FnD9JxMnyyHvzR7U","accessSecret":"kQw43M6sPQ1r5NVpQXUmikUzwa3F8fdd"},"message":"success"},"message":"操作成功","success":true}
//        String bizAccountId = "bldTest2";
//        String salt = "abcdefg12345";
//        String s = XiTaChainUtils.getSignature(bizAccountId, salt);
//        ResultInfo token = XiTaChainUtils.createKeyAndSecret(bizAccountId, s);
//        log.info("********************************创建帐号并获取鉴权token：{}", JSONObject.toJSONString(token));
//    }
//
//    @Test
//    public void test2(){
////        buthorization":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y","expiration":86400000}
////        bldTestldTest1: {"a2: {"authorization":"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiZXhwIjoxNTg5MzUxNjg0LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk8O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkyNjUyODQsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.YNuQmARjtj923eFHYN8Nn-aqYfIEyhCfhAHOIwNSmC8","expiration":86400000}
//        ResultInfo token = XiTaChainUtils.createToken("FnD9JxMnyyHvzR7U", "kQw43M6sPQ1r5NVpQXUmikUzwa3F8fdd");
//        log.info("*************************生成的token : {}", JSONObject.toJSONString(token));
//    }
//
//    @Test
//    public void test3(){
////        bldTest1: [{"accessKeyId":"YbjRq3VcLB","createdAt":"2020-05-11 16:51:47","address":"0ce1baaaf00674636cf3db217d6127422f4390b4","encryptedMethod":"SECP256K1","publicKey":"b60bff8b5f030fbdfa35f4f184258084a46936fbd6182b3e828f6ac9c65592cee59cdd4cf3b570c3c8092af41a42331ee0fb122b5c2dfe57d83f3e9d86c25e17"},{"accessKeyId":"XpF5v30bha","createdAt":"2020-05-11 16:51:47","address":"2573b04e8351ce9e9c72cb2fdfcaeb8f8e50e36d","encryptedMethod":"SM2","publicKey":"040ef7c7bf9df233fb726dabe4ad4703341e69beab4492b0df1f359f155df42dd72bad9deaf8fcc0bdbd230920b216259b1dd4d7ebeacf80f05c89f4ffeab863e1"}]
////        bldTest2: [{"accessKeyId":"Yh1WDE1PFK","createdAt":"2020-05-12 14:39:51","address":"ec388deb9efb0f889d48d0c402594b5bc8db920e","encryptedMethod":"SECP256K1","publicKey":"f15ae2b01b5e0d19b543769ace7c3b1336246dcab4489c8432a3acf610248e75793c1069150ceaeedd863163b3dfd2e9d02b4497138d606dc277fb4c7532eb7"},{"accessKeyId":"UWIyXcjpb0","createdAt":"2020-05-12 14:39:51","address":"e9e9a732c21b8d520a63c4a52096445c6d7c6877","encryptedMethod":"SM2","publicKey":"04923b9157daa68ae61c9c6b0ffa37be6ad00f2e819e52c3c53f35220d2e24e803a2fe66b37603ec92245ff415513664953a8257383bb69a6f89f485cb3603210e"}]
//        ResultInfo account = XiTaChainUtils.createAccount("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiZXhwIjoxNTg5MzUxNjg0LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk8O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkyNjUyODQsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.YNuQmARjtj923eFHYN8Nn-aqYfIEyhCfhAHOIwNSmC8");
//        log.info("*******************生成的账号信息：{}", JSONObject.toJSONString(account));
//    }
//
//    @Test
//    public void test4(){
//        /*{"txHash":"0x54bc991e0276d64d6e4810e7c620a38e6660de41f456667f8a57d02b3a595646"},"message":"success"} bizId=1*/
//        ResultInfo evidences = XiTaChainUtils.evidences("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y");
//        log.info("******************上链: {}", JSONObject.toJSONString(evidences));
//    }
//
//    @Test
//    public void test5() throws IOException {
////        {"confidentInfo":{"blockHeight":28365,"fileHash":"d7f68ae33cb89ee2b5e35887b64e91265859c9feb5e28953ef7049f734e25def","txState":"succeeded","txHash":"0xb5f8e07e567d5606a85c9181111d231aa3247e0a706a544ffdcf3db353d2f5e5","fileBizId":"3","timestamp":1589248973272},"ossPath":"http://192.168.3.13:7003/upload/evidences/d7f68ae33cb89ee2b5e35887b64e91265859c9feb5e28953ef7049f734e25def"}
//        String filePath = "C:\\Users\\Administrator\\Desktop\\aa.txt";
//        byte[] b = Files.readAllBytes(Paths.get(filePath));
//        String base64 = Base64.getEncoder().encodeToString(b);
//        JSONObject paramJ = new JSONObject();
//        paramJ.put("base64", "5L2g5aW977yM5rqq5aGUCg==" + base64);
//        paramJ.put("fileBizId", "6");
//        paramJ.put("fileName", "测试文件6");
//        ResultInfo resultInfo = XiTaChainUtils.evidencesBase64("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiZXhwIjoxNTg5NTEwOTMzLCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk8O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODk0MjQ1MzMsImp0aSI6IjVhN2I2NWM3LTIxYmYtNGI1ZS1iY2MzLTM2N2FjMmI3ODc2ZSJ9.HR_vtsFJdPMckoO9qQNUOyDlv43bLDID8tCcgu7egcs", paramJ.toJSONString());
//        log.info("***********************base上链：{}", JSONObject.toJSONString(resultInfo));
//    }
//
//    @Test
//    public void test6() throws IOException {
////        {"txHash":"0xda687bedcb249e282b8cad49067124db4056a34878a721f7eb3b933d1564da9d","ossPath":"http://localhost:7003/upload/evidences/d7f68ae33cb89ee2b5e35887b64e91265859c9feb5e28953ef7049f734e25def"}
//        String filePath = "C:\\Users\\Administrator\\Desktop\\1.png";
//        byte[] b = Files.readAllBytes(Paths.get(filePath));
//        String base64 = Base64.getEncoder().encodeToString(b);
//        JSONObject paramJ = new JSONObject();
//        paramJ.put("base64", "5L2g5aW977yM5rqq5aGUCg==" + base64);
//        paramJ.put("fileBizId", "5");
//        paramJ.put("fileName", "测试文件5");
//        ResultInfo resultInfo = XiTaChainUtils.evidencesBase64Async("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y", paramJ.toJSONString());
//        log.info("***********************base异步上链：{}", JSONObject.toJSONString(resultInfo));
//    }
//
//    @Test
//    public void test7(){
//        HashMap<String, String> paramMap = new HashMap<>(2);
////        参数可以2选1
//        paramMap.put("bizId", "4");
//////        paramMap.put("txHash", "0x54bc991e0276d64d6e4810e7c620a38e6660de41f456667f8a57d02b3a595646");
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y");
//        ResultInfo resultInfo = XiTaChainUtils.query(paramMap, headerMap);
//        log.info("***************************原文上链查询: {}", JSONObject.toJSONString(resultInfo));
//    }
//
//    @Test
//    public void test8(){
//        HashMap<String, String> paramMap = new HashMap<>(2);
////        参数可以2选1
//        paramMap.put("fileBizId", "4");
////        paramMap.put("txHash", "0x54bc991e0276d64d6e4810e7c620a38e6660de41f456667f8a57d02b3a595646");
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y");
//        ResultInfo resultInfo = XiTaChainUtils.queryBase64(paramMap, headerMap);
//        log.info("***************************base64上链查询: {}", JSONObject.toJSONString(resultInfo));
//    }
//
//    @Test
//    public void test9(){
////        {"txHash":"0x38f9dfa5f7afbca810bb2b63cdc4aebd713b3f2a76110ddadb8103bb89c4158d","ossPath":"http://localhost:7003/upload/evidences/e0a30bdf651024d08739c94e80d87392977fc23811634fb3fd039a9126437f29"}
//        /*bldTest1 把bizId为2 的存证权限授权给bldTest2一天*/
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y");
//        JSONObject paramJ = new JSONObject();
//        paramJ.put("authorizedAppKey", "FnD9JxMnyyHvzR7U");
//        paramJ.put("fileBizId", "2");
//        paramJ.put("startTimestamp", String.valueOf(System.currentTimeMillis()));
//        paramJ.put("endTimestamp", String.valueOf(DateUtils.getAfterTimestamp(new Date(), 1, Calendar.DAY_OF_MONTH)));
//        ResultInfo s = XiTaChainUtils.authorize(headerMap, paramJ.toJSONString());
//        log.info("*************************授权访问：{}", JSONObject.toJSONString(s));
//    }
//
//    @Test
//    public void test10(){
////        {"code":200,"data":["2020-05-12 06:41:42 oYjNLXVmxaynPyeB 授权 FnD9JxMnyyHvzR7U 开通 2的原文下载权限。"],"message":"","success":true}
//        ResultInfo resultInfo = XiTaChainUtils.authorizedList("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y");
//        log.info("**********************查询授权列表：{}", JSONObject.toJSONString(resultInfo));
//    }
//
//    @Test
//    public void test11(){
////        ["FnD9JxMnyyHvzR7U"]
//        ResultInfo resultInfo = XiTaChainUtils.authorizedUserList("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNTg5MjcwMTY4LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk7O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkxODM3NjgsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.Uwo4WQOeBPx5B31J8Zk5QzXWLB5Vi7XauVFYJyLhq3Y");
//        log.info("**********************查询已授权用户列表：{}", JSONObject.toJSONString(resultInfo));
//    }
//
//    @Test
//    public void test12(){
////        [{"confidentInfo":{"blockHeight":27487,"errorMessage":"","fileHash":"测试上链信息2","txState":"succeeded","txHash":"0x926d7baa61d3b919f3121ed41a362898bf1d6adcdf0b63f50d1374e1bb8f5885","fileBizId":"2","timestamp":1589246338101},"ossPath":""}]
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiZXhwIjoxNTg5MzUxNjg0LCJ1dWlkIjoiOUw_QU5MSU4-PTs8QUk9OT4-TEk8O2w7ajk4bGxpPzpuQTxuQT1pOWsiLCJpYXQiOjE1ODkyNjUyODQsImp0aSI6ImQ2ZDU0MDVmLTA3ZDMtNGE2NC05N2Y0LWVkYTJmMDY5MDdjMyJ9.YNuQmARjtj923eFHYN8Nn-aqYfIEyhCfhAHOIwNSmC8");
//        JSONObject j = new JSONObject();
//        j.put("authorization", "oYjNLXVmxaynPyeB");
//        /*非必填参数*/
////        j.put("fileBizId", "2");
////        j.put("txHash", "")
//        ResultInfo authorization = XiTaChainUtils.authorization(headerMap, j.toJSONString());
//        log.info("******************查询被授权的存证：{}",JSONObject.toJSONString(authorization));
//    }
//}
