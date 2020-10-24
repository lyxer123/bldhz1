package com.bld.project.system.block.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author SOFAS
 * @date 2020/8/27
 * @directions    区块链
*/
@Data
public class Block implements Serializable {
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 地址
     */
    private String addr;

    public Block() {

    }

    public Block(String privateKey, String publicKey, String addr) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.addr = addr;
    }
}
