package org.springcrazy.common.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;


/**
 * 全局事务统一配置
 *
 */
@Slf4j
@Aspect
@Component
public class TransactionAdviceConfig {
    public static final String aop_pointcut_expression = "execution(* org.springcrazy.modules.*.service.impl.*.*(..))";

    @Autowired
    PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {

        //增删改使用PROPAGATION_REQUIRED 当前有事务则加入该事务，没有事务则创建事务
        RuleBasedTransactionAttribute txAttr_REQUIRED = new RuleBasedTransactionAttribute();
        //事务回滚机制
        txAttr_REQUIRED.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //事务隔离机制
        txAttr_REQUIRED.setIsolationLevel(Isolation.DEFAULT.value());

        //读使用PROPAGATION_SUPPORTS 当前有事务则加入该事务，没有事务则以非实物方式运行
        DefaultTransactionAttribute txAttr_SUPPORTS_READONLY = new DefaultTransactionAttribute();
        //事务回滚机制
        txAttr_SUPPORTS_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        //事务隔离机制
        txAttr_SUPPORTS_READONLY.setIsolationLevel(Isolation.DEFAULT.value());
        txAttr_SUPPORTS_READONLY.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        //增删改
        source.addTransactionalMethod("save*", txAttr_REQUIRED);
        source.addTransactionalMethod("add*", txAttr_REQUIRED);
        source.addTransactionalMethod("insert*", txAttr_REQUIRED);
        source.addTransactionalMethod("create*", txAttr_REQUIRED);
        source.addTransactionalMethod("delete*", txAttr_REQUIRED);
        source.addTransactionalMethod("del*", txAttr_REQUIRED);
        source.addTransactionalMethod("remove*", txAttr_REQUIRED);
        source.addTransactionalMethod("update*", txAttr_REQUIRED);
        source.addTransactionalMethod("upd*", txAttr_REQUIRED);
        source.addTransactionalMethod("edit*", txAttr_REQUIRED);
        source.addTransactionalMethod("exec*", txAttr_REQUIRED);
        source.addTransactionalMethod("set*", txAttr_REQUIRED);
        source.addTransactionalMethod("import*", txAttr_REQUIRED);


        //读
        source.addTransactionalMethod("get*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("query*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("find*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("load*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("list*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("count*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("check*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("select*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("is*", txAttr_SUPPORTS_READONLY);
        source.addTransactionalMethod("export*", txAttr_SUPPORTS_READONLY);


        source.addTransactionalMethod("*", txAttr_SUPPORTS_READONLY);



        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(aop_pointcut_expression);
        log.info("aop_pointcut_expression"+aop_pointcut_expression);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
