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
package org.springcrazy.modules.quartz.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springcrazy.core.mp.support.Condition;
import org.springcrazy.modules.quartz.entity.QuartzJob;
import org.springcrazy.modules.quartz.mapper.QuartzJobMapper;
import org.springcrazy.modules.quartz.utils.QuartzManage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**

 * @date 2019-01-07
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JobRunner implements ApplicationRunner {

    private final QuartzJobMapper quartzJobMapper;
    private final QuartzManage quartzManage;


    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        log.info("--------------------注入定时任务---------------------");
        QuartzJob quartzJob = new QuartzJob();
        quartzJob.setIsPause("0");
        QueryWrapper<QuartzJob> queryWrapper = Condition.getQueryWrapper(quartzJob);
        List<QuartzJob> quartzJobs = quartzJobMapper.selectList(queryWrapper);
        quartzJobs.forEach(quartzManage::addJob);
        log.info("--------------------定时任务注入完成---------------------");
    }
}
