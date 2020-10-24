package com.bld.framework.config;

import ch.ethz.ssh2.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Component
public class ConnConfig {
    @Value("${linux.ip}")
    private String ip;

    @Value("${linux.port}")
    private int port;

    @Value("${linux.user}")
    private String user;

    @Value("${linux.password}")
    private String password;

    public static Connection conn = null;
    public  boolean login(){
        //创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
        //new Connection(ip, port)创建对象
        conn = new Connection(ip,port);
        try {
            //连接远程服务器
            conn.connect();
            //使用用户名和密码登录
            return conn.authenticateWithPassword(user, password);
        } catch (Exception e) {
            System.err.printf("用户%s密码%s登录服务器%s失败！", user, password, "192.168.159.128");
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 复制到JAVA所在服务器
     * @param conn
     * @param fileName
     * @param localPath
     */
    public void copyFile(Connection conn, String fileName,String localPath){
        SCPClient sc = new SCPClient(conn);
        try {
            sc.get(fileName, localPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 流式输出，用于浏览器下载
     * @param conn
     * @param fileName
     * @param outputStream
     */
    public void copyFile(Connection conn, String fileName, ServletOutputStream outputStream){
        SCPClient sc = new SCPClient(conn);
        try {
            sc.get(fileName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在远程LINUX服务器上，在指定目录下，获取文件各个属性
     * @param[in] conn Conncetion对象
     * @param[in] remotePath 远程主机的指定目录
     */
    public List<String> getFileProperties(Connection conn, String remotePath){
        List<String> fileList=new ArrayList<>();
        try {

            SFTPv3Client sft = new SFTPv3Client(conn);

            Vector<?> v = sft.ls(remotePath);

            for(int i=0;i<v.size();i++){
                SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                s = (SFTPv3DirectoryEntry) v.get(i);
                //文件名
                String filename = s.filename;
                fileList.add(filename);
                System.out.println(filename);
                //文件的大小
                Long fileSize = s.attributes.size;
            }

            sft.close();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return fileList;
    }

    /**
     * 在远程LINUX服务器上，在指定目录下，删除指定文件
     * @param[in] fileName 文件名
     * @param[in] remotePath 远程主机的指定目录
     * @return
     */
    public void delFile(String remotePath, String fileName) {
        try {
            SFTPv3Client sft = new SFTPv3Client(conn);
            //获取远程目录下文件列表
            Vector<?> v = sft.ls(remotePath);

            for (int i = 0; i < v.size(); i++) {
                SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                s = (SFTPv3DirectoryEntry) v.get(i);
                //判断列表中文件是否与指定文件名相同
                if (s.filename.equals(fileName)) {
                    //rm()方法中，须是文件绝对路径+文件名称
                    sft.rm(remotePath + s.filename);
                }
                sft.close();
            }
    }catch (Exception e){
        e.getStackTrace();
        }
    }
        /**
         * 执行脚本
         * @param conn Connection对象
        * @param cmds 要在linux上执行的指令
         */
        public int exec(Connection conn ,String cmds){
            InputStream stdOut = null;
            InputStream stdErr = null;
            int ret = -1;
            try {
                //在connection中打开一个新的会话
                Session session = conn.openSession();
                //在远程服务器上执行linux指令
                session.execCommand(cmds);
                //指令执行结束后的输出
                stdOut = new StreamGobbler(session.getStdout());
                //指令执行结束后的错误
                stdErr = new StreamGobbler(session.getStderr());
                //等待指令执行结束
                session.waitForCondition(ChannelCondition.EXIT_STATUS, 60);
                //取得指令执行结束后的状态
                ret = session.getExitStatus();

                conn.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            return ret;
        }

}
