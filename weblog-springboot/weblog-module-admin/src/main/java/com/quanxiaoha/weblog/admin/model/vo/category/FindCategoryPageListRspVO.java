package com.quanxiaoha.weblog.admin.model.vo.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: 木萨·塔布提
 * @url: blog.arnasoft.site
 * @date: 2023-09-15 14:07
 * @description: 灵感分页
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCategoryPageListRspVO {
    /**
     * 灵感 ID
     */
    private Long id;

    /**
     * 灵感名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 文章总数
     */
    private Integer articlesTotal;

}
