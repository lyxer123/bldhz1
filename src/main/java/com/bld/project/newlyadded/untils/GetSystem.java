package com.bld.project.newlyadded.untils;


import com.bld.project.newlyadded.entity.SystemEntity;
import org.hyperic.sigar.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class GetSystem {

    @Autowired
    private Sigar sigar;

//    public static void main(String[] args) {
//        try {
//            file();
//        }catch (Exception e){}
//    }

    public   SystemEntity getSystem() throws SigarException {
        Mem mem = sigar.getMem();//获取内存使用
        CpuPerc[] cpuPercList = sigar.getCpuPercList();//获取当前cpu组
        FileSystem[] fslist = sigar.getFileSystemList(); //当前盘符组
        double cpuuse = 0;
        for (int i = 0; i <cpuPercList.length ; i++) {
            cpuuse +=cpuPercList[i].getCombined();
        }
        //计算平均使用率
        double usecpu = cpuuse/(double) cpuPercList.length;
        long totalSizeOfFilesInDir = getTotalSizeOfFilesInDir(new File("/usr/local/besu"));

        long used = 0;//使用的
        long free = 0;//未使用的
        long total = 0;//总大小
        for (int i = 0; i < fslist.length; i++) {
            FileSystem fs = fslist[i];
            FileSystemUsage usage = null;
            try {
                usage = sigar.getFileSystemUsage(fs.getDirName());
            }catch (SigarException s){
               // System.out.println("发生错误");
                continue;
            }
            switch (fs.getType()) {
                case 0: // TYPE_UNKNOWN ：未知
                    break;
                case 1: // TYPE_NONE
                    break;
                case 2: // TYPE_LOCAL_DISK : 本地硬盘
                    // 文件系统总大小
                    //System.out.println(fs.getDevName() + "总大小:    " + usage.getTotal() + "KB");
                    total += usage.getTotal();
                    used += usage.getUsed();
                    free += usage.getFree();
                    //double usePercent = usage.getUsePercent() * 100D;
                    break;
                case 3:// TYPE_NETWORK ：网络
                    break;
                case 4:// TYPE_RAM_DISK ：闪存
                    break;
                case 5:// TYPE_CDROM ：光驱
                    break;
                case 6:// TYPE_SWAP ：页面交换
                    break;
            }
        }
        SystemEntity systemEntity = new SystemEntity();
        systemEntity.setMem(mem);
        systemEntity.setCpuPerc(cpuPercList);
        systemEntity.setFileSystem(fslist);
        systemEntity.setFileFree(free/1024/1024);
        systemEntity.setFileTotal(total/1024/1024);
        systemEntity.setFileUse(used/1024/1024);
        systemEntity.setCpuuse(usecpu);
        //systemEntity.setWatchFile((double)FileUtils.sizeOf(new File("/usr/local/besu"))/1024/1024/1024);
        if(mem != null){
            systemEntity.setMenUse((mem.getUsed()*1.0)/mem.getTotal());
        }

        return systemEntity;
    }

    private   long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }


}
