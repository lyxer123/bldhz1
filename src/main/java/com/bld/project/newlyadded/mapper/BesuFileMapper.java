package com.bld.project.newlyadded.mapper;

import com.bld.project.newlyadded.entity.BesuFileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BesuFileMapper {

    /*新增*/
    void  insertFile(long fileSize);

    List<BesuFileEntity> list();

}
