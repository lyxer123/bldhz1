package com.bld.project.newlyadded.entity;

import lombok.Data;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.Mem;

/**
 * 内存
 */
@Data
public class SystemEntity {

    //内存
    private Mem  Mem;

    //cpu
    private CpuPerc[] cpuPerc;


    //盘符
    private FileSystem[] fileSystem;


    //内存使用
    private double menUse;

    //总盘符使用
    private long fileUse;

    //总盘符未使用
    private long fileFree;

    //盘符总使用量
    private long fileTotal;

    private double cpuuse;

    private double watchFile;


}
