package com.bld.project.newlyadded.service.Impl;

import com.bld.project.newlyadded.entity.BesuFileEntity;
import com.bld.project.newlyadded.mapper.BesuFileMapper;
import com.bld.project.newlyadded.service.BesuFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BesuFileServiceImpl implements BesuFileService {

    @Autowired
    private BesuFileMapper besuFileMapper;

    @Override
    public List<BesuFileEntity> getlist() {
        List<BesuFileEntity> collect = besuFileMapper.list().stream().sorted(
                Comparator.comparing(BesuFileEntity::getCreateTime)
        ).collect(Collectors.toList());
        return collect;
    }
}
