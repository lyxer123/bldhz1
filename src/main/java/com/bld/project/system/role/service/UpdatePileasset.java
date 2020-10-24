package com.bld.project.system.role.service;

import com.bld.project.system.role.domain.Pileasset;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class UpdatePileasset {

    public String autoUpdatePileasset1(@Param("pileasset") Pileasset pileasset) {
        String sql = new SQL() {
            {
                UPDATE("pileasset as P");
                if (pileasset.getSn1() != null) {
                    SET("P.SN1 = #{pileasset.sn1}");
                }
                ;
                if (pileasset.getSn2() != null) {
                    SET("P.SN2 = #{pileasset.sn2}");
                }
                ;
                if (pileasset.getSpicapacity() == 0) {
                    SET("P.Spicapacity = #{pileasset.spicapacity}");
                }
                ;
                if (pileasset.getSp() != null) {
                    SET("P.SP = #{pileasset.sp}");
                }
                if (pileasset.getCustomer() != null) {
                    SET("P.Customer = #{pileasset.customer}");
                }
                if (pileasset.getCcid() != null) {
                    SET("P.CCID = #{pileasset.ccid}");
                }
                if (pileasset.getWorkingVersion() != null) {
                    SET("P.WorkingVersion = #{pileasset.workingVersion}");
                }
                if (pileasset.getGps() != null) {
                    SET("P.GPS = #{pileasset.gps}");
                }
                if (pileasset.getImei() != null) {
                    SET("P.IMEI = #{pileasset.imei}");
                }
                if (pileasset.getOp() != null) {
                    SET("P.OP = #{pileasset.op}");
                }
                if (pileasset.getMultiMeter() != null) {
                    SET("P.MultiMeter = #{pileasset.multiMeter}");
                }
                if (pileasset.getPd() != null) {
                    SET("P.PD = #{pileasset.pd}");
                }
                if (pileasset.getSd() != null) {
                    SET("P.SD =  #{pileasset.sd}");
                }

                WHERE("P.ChipID=#{pileasset.chipId}");

            }
        }.toString();
        return sql;
    }
}
