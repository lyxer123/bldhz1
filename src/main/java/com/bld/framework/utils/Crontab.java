package com.bld.framework.utils;

import com.bld.project.system.role.domain.Pileasset;
import com.bld.project.system.role.service.IRoleService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class Crontab extends QuartzJobBean {

    @Autowired
    private IRoleService iRoleService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Pileasset> pileassets = iRoleService.listPileasset();
        for (int i = 0; i < pileassets.size(); i++) {

        }
    }
}
