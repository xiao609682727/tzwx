/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.springcrazy.modules.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springcrazy.modules.edu.service.IVideosourceService;
import org.springcrazy.modules.system.service.ILogErrorService;
import org.springcrazy.modules.web.service.IStatDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试用

 * @date 2019-01-08
 */
@Slf4j
@Component
public class TestTask {

    @Autowired
    private IStatDataService statDataService;
    @Autowired
    private IVideosourceService videosourceService;
    @Autowired
    private ILogErrorService errorService;

    /**
     * 每天数据统计数据计算
     */
    public void handleDataByDay(){
        log.info("用户区域统计开始");
        statDataService.updateUserDataByArea();
        log.info("用户区域统计结束");
    }

    /**
     * 管理端首页平台数据统计
     */
    public void updateAdminIndexData(){
        log.info("管理端首页平台数据统计开始");
        statDataService.updateAdminIndexData();
        log.info("管理端首页平台数据统计结束");
    }

    /**
     * 登录与访客记录
     */
    public void updateUserLog(){
        log.info("登录与访客记录统计开始");
        statDataService.updateUserLog();
        log.info("登录与访客记录统计结束");
    }


    /**
     * 更新视频状态
     */
    public void syncVideoInfo(){
        log.info("更新视频状态");
        videosourceService.updateVideoSourseStatus();
        log.info("更新视频状态");
    }

    /**
     * 清理系统日志表一个月前日志
     */
    public void clearSystemLog(){
        log.info("清理系统日志");
        errorService.clearSystemLog();
        log.info("清理系统日志");
    }

    public void run1(String str){
        log.info("run1 执行成功，参数为： {}" + str);
    }

    public void run2(){
        log.info("run2 执行成功");
    }

}
