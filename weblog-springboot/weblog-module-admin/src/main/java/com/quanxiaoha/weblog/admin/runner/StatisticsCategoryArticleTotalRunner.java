package com.quanxiaoha.weblog.admin.runner;

import com.quanxiaoha.weblog.admin.service.AdminStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: 木萨·塔布提
 * @Date: 2024/3/28 20:40
 * @Version: v1.0.0
 * @Description: TODO
 **/
@Component
@Slf4j
public class StatisticsCategoryArticleTotalRunner implements CommandLineRunner {

    @Autowired
    private AdminStatisticsService statisticsService;

    @Override
    @Async("threadPoolTaskExecutor")
    public void run(String... args) throws Exception {
        log.info("==> 开始统计各灵感下灵感笔记数量...");
        statisticsService.statisticsCategoryArticleTotal();
        log.info("==> 结束统计各灵感下灵感笔记数量...");
    }
}