package com.quanxiaoha.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanxiaoha.weblog.common.domain.dos.StatisticsArticlePVDO;

import java.time.LocalDate;
import java.util.List;

/**
 * @author: 木萨·塔布提
 * @url: blog.arnasoft.site
 * @date: 2023-08-22 17:06
 * @description: 每日文章 PV 访问量统计
 **/
public interface StatisticsArticlePVMapper extends BaseMapper<StatisticsArticlePVDO> {

    /**
     * 对指定日期的文章 PV 访问量进行 +1
     * @param date
     * @return
     */
    default int increasePVCount(LocalDate date) {
        return update(null, Wrappers.<StatisticsArticlePVDO>lambdaUpdate()
                .setSql("pv_count = pv_count + 1")
                .eq(StatisticsArticlePVDO::getPvDate, date));
    }

    /**
     * 查询最近一周的文章 PV 访问量记录
     * @return
     */
    default List<StatisticsArticlePVDO> selectLatestWeekRecords() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(6); // 获取一周前的日期（包括今天共7天）
        
        return selectList(Wrappers.<StatisticsArticlePVDO>lambdaQuery()
                .ge(StatisticsArticlePVDO::getPvDate, oneWeekAgo) // 大于等于一周前的日期
                .le(StatisticsArticlePVDO::getPvDate, today) // 小于等于当前日期
                .orderBy(true, true, StatisticsArticlePVDO::getPvDate)); // 按日期升序排序
    }
}
