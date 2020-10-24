package com.bld.project.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.bld.common.utils.StringUtils;
import lombok.Data;

@Data
public class BldListQurys {
    /**
     * 每页查询数量
     */
    @JsonAlias("pageSize")
    private int pageSize;
    /**
     * 查询页数
     */
    @JsonAlias("pageNum")
    private int pageNum;
    /**
     * 查询条件
     */
    @JsonAlias("search")
    private String search;

    public int getLimit() {
        return this.pageSize == 0 ? 50 : pageSize;
    }

    public String getSearch() {
        return StringUtils.isNullString(search) ? "" : search;
    }
}
