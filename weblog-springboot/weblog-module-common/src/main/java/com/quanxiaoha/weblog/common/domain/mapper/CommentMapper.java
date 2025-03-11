package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanxiaoha.weblog.common.domain.dos.CommentDO;
import com.quanxiaoha.weblog.common.enums.CommentStatusEnum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

/**
 * @Author: 木萨·塔布提
 * @Date: 2024/4/3 20:40
 * @Version: v1.0.0
 * @Description: TODO
 **/
public interface CommentMapper extends BaseMapper<CommentDO> {
    /**
     * 根据路由地址、状态查询对应的评论
     * @param routerUrl
     * @return
     */
    default List<CommentDO> selectByRouterUrlAndStatus(String routerUrl, Integer status) {
        return selectList(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getRouterUrl, routerUrl) // 按路由地址查询
                .eq(CommentDO::getStatus, status) // 按状态查询
                .orderByDesc(CommentDO::getCreateTime) // 按创建时间倒序
        );
    }

    /**
     * 分页查询
     * @param current
     * @param size
     * @param startDate
     * @param endDate
     * @return
     */
    default Page<CommentDO> selectPageList(Long current, Long size, String routerUrl,
                                           LocalDate startDate, LocalDate endDate, Integer status) {
        // 分页对象(查询第几页、每页多少数据)
        Page<CommentDO> page = new Page<>(current, size);

        // 构建查询条件
        LambdaQueryWrapper<CommentDO> wrapper = Wrappers.<CommentDO>lambdaQuery()
                .like(StringUtils.isNotBlank(routerUrl), CommentDO::getRouterUrl, routerUrl) // like 模糊查询
                .eq(Objects.nonNull(status), CommentDO::getStatus, status) // 评论状态
                .ge(Objects.nonNull(startDate), CommentDO::getCreateTime, startDate) // 大于等于 startDate
                .le(Objects.nonNull(endDate), CommentDO::getCreateTime, endDate)  // 小于等于 endDate
                .orderByDesc(CommentDO::getCreateTime); // 按创建时间倒叙

        return selectPage(page, wrapper);
    }

    /**
     * 根据 reply_comment_id 查询评论
     * @param replyCommentId
     * @return
     */
    default List<CommentDO> selectByReplyCommentId(Long replyCommentId) {
        return selectList(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getReplyCommentId, replyCommentId)
                .orderByDesc(CommentDO::getCreateTime)
        );
    }

    /**
     * 根据 parent_comment_id 删除
     * @param id
     * @return
     */
    default int deleteByParentCommentId(Long id) {
        return delete(Wrappers.<CommentDO>lambdaQuery()
                .eq(CommentDO::getParentCommentId, id));
    }
    
    /**
     * 查询多个文章的评论数量
     * @param articleIds 文章ID列表
     * @return 文章ID -> 评论数量的映射
     */
    default Map<Long, Long> selectArticleCommentCounts(List<Long> articleIds, Integer status) {
        if (CollectionUtils.isEmpty(articleIds)) {
            return new HashMap<>();
        }
        
        Map<Long, Long> result = new HashMap<>();
        for (Long articleId : articleIds) {
            // 构建文章详情页的路由地址
            String routerUrl = "/article/" + articleId;
            // 查询符合条件的评论数量
            Long count = selectCount(Wrappers.<CommentDO>lambdaQuery()
                    .eq(CommentDO::getRouterUrl, routerUrl)
                    .eq(CommentDO::getStatus, status));
            // 将结果放入 Map
            result.put(articleId, count);
        }
        return result;
    }
    
    /**
     * 查询多个文章的评论数量（默认查询已审核通过的评论）
     * @param articleIds 文章ID列表
     * @return 文章ID -> 评论数量的映射
     */
    default Map<Long, Long> selectArticleCommentCounts(List<Long> articleIds) {
        return selectArticleCommentCounts(articleIds, CommentStatusEnum.NORMAL.getCode());
    }
}
