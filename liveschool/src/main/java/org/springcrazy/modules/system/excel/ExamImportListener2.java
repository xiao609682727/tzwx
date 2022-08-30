package org.springcrazy.modules.system.excel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springcrazy.modules.exam.service.IQuestionService;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExamImportListener2 extends AnalysisEventListener<ExamExcel> {

    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<ExamExcel> list = new ArrayList<>();
    /**
     * 试题questionService
     */
    private  final IQuestionService questionService;

    @Override
    public void invoke(ExamExcel data, AnalysisContext context) {
        data.setQstType(2);
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            questionService.importExam(list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        questionService.importExam(list);
        // 存储完成清理list
        list.clear();
    }
}
