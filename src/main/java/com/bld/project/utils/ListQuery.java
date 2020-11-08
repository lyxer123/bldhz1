package com.bld.project.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.bld.common.utils.StringUtils;
import lombok.Data;

/**
 * @author SOFAS
 * @date 2020/5/27
 * @directions  列表查询实体
*/
@Data
public class ListQuery {
    /**
     * 每页查询数量
     */
    @JsonAlias("limit")
    private int limit;
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
    /**
     * 查询id
     */
    @JsonAlias("id")
    private String id;

    @JsonAlias("idOffset")
    private String idOffset;

    @JsonAlias("textOffset")
    private String textOffset;



    public int getLimit() {
        return this.limit == 0 ? 50 : limit;
    }

    public String getSearch() {
        return StringUtils.isNullString(search) ? "" : search;
    }
}
