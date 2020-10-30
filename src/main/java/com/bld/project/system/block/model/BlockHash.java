package com.bld.project.system.block.model;

import com.bld.project.utils.BldListQurys;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class BlockHash extends BldListQurys implements Serializable {
    private Long id;
    /**
     * 设备标识
     */
    private String chipId;
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 收款地址
     */
    private String toWallet;
    /**
     * 出款地址
     */
    private String fromWallet;
    /**
     * 金额
     */
    private Long money;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     *  最后一次修改时间
     */
    private Date updateTime;

    private String deviceWallet;
    private String deviceToken;

    public BlockHash() { }

    public BlockHash(String chipId, String toWallet, String fromWallet) {
        this.chipId = chipId;
        this.toWallet = toWallet;
        this.fromWallet = fromWallet;
    }

    public BlockHash(String chipId, String hash, String toWallet, String fromWallet, Long money, Date addTime) {
        this.chipId = chipId;
        this.hash = hash;
        this.toWallet = toWallet;
        this.fromWallet = fromWallet;
        this.money = money;
        this.addTime = addTime;
    }
}
