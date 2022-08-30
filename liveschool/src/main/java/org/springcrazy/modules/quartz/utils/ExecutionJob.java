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
package org.springcrazy.modules.quartz.utils;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcrazy.core.tool.utils.Exceptions;
import org.springcrazy.core.tool.utils.Func;
import org.springcrazy.core.tool.utils.SpringUtil;
import org.springcrazy.modules.quartz.entity.QuartzJob;
import org.springcrazy.modules.quartz.entity.QuartzLog;
import org.springcrazy.modules.quartz.service.IQuartzJobService;
import org.springcrazy.modules.quartz.service.IQuartzLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**

 */
@Async
public class ExecutionJob extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 该处仅供参考 */
    private final static ThreadPoolExecutor EXECUTOR = SpringUtil.getBean(ThreadPoolTaskExecutor.class).getThreadPoolExecutor();

    @Override
    public void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        // 获取spring bean
        IQuartzJobService quartzJobService = SpringUtil.getBean(IQuartzJobService.class);
        IQuartzLogService quartzLogService = SpringUtil.getBean(IQuartzLogService.class);

        QuartzLog log = new QuartzLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            logger.info("任务准备执行，任务名称：{}", quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
                    quartzJob.getParams());
            Future<?> future = EXECUTOR.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态
            log.setIsSuccess(QuartzLog.SUCCESS);
            logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.getJobName(), times);
            // 判断是否存在子任务
            if(Func.isNotBlank(quartzJob.getSubTask())){
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                for (String id : tasks) {
                    quartzJobService.execution(quartzJobService.getById(Integer.parseInt(id)));
                }
            }
        } catch (Exception e) {
            logger.error("任务执行失败，任务名称：{}" + quartzJob.getJobName(), e);
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setIsSuccess(QuartzLog.FAIL);
            log.setExceptionDetail(Exceptions.getStackTraceAsString(e));
            // 任务如果失败了则暂停
            if( Func.equals(quartzJob.getPauseAfterFailure(),"2")){
                quartzJob.setIsPause("1");
                //更新状态
                quartzJobService.updateById(quartzJob);
            }
            /*if(quartzJob.getEmail() != null){
                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
                // 邮箱报警
                EmailVo emailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
                emailService.send(emailVo, emailService.find());
            }*/
        } finally {
            quartzLogService.save(log);
        }
    }

    /*private EmailVo taskAlarm(QuartzJob quartzJob, String msg) {
        EmailVo emailVo = new EmailVo();
        emailVo.setSubject("定时任务【"+ quartzJob.getJobName() +"】执行失败，请尽快处理！");
        Map<String, Object> data = new HashMap<>(16);
        data.put("task", quartzJob);
        data.put("msg", msg);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/taskAlarm.ftl");
        emailVo.setContent(template.render(data));
        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
        emailVo.setTos(emails);
        return emailVo;
    }*/
}
