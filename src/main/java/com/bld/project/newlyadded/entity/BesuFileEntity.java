package com.bld.project.newlyadded.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BesuFileEntity implements Serializable {

    private static final long serialVersionUID = 5966337426046826942L;


    /**
     * id
     */
    private  Integer id;

    /**
     * 文件大小
     */
    private  Integer fileSize;

    /*
    创建时间
     */
    private Date createTime;


}
