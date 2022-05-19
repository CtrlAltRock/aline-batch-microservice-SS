package com.smoothstack.alinefinancial.steps;

import com.smoothstack.alinefinancial.tasklets.AnalysisTasklet;
import com.smoothstack.alinefinancial.tasklets.FailureTasklet;
import com.smoothstack.alinefinancial.tasklets.ReportTasklet;
import com.smoothstack.alinefinancial.tasklets.XmlWritingTasklets.*;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class Steps {

    @Autowired
    StepBuilderFactory stepsFactory;

    @Bean
    public Step analysisStep() throws Exception {
        return stepsFactory.get("analysisTaskletStep")
                .tasklet(new AnalysisTasklet())
                .build();
    }

    @Bean
    public Step reportStep() throws Exception {
        return stepsFactory.get("reportTaskletStep")
                .tasklet(new ReportTasklet())
                .build();
    }

    @Bean
    public Step chunkFailureStep() throws Exception {
        return stepsFactory.get("chunkFailureStep")
                .tasklet(new FailureTasklet())
                .build();
    }

    // Writer Steps

    @Bean
    public Step xmlStateWriterStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlStateWriterStep")
                .tasklet(new StateWriterTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlCardWriterStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlCardWriterStep")
                .tasklet(new CardWriterTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlUserWriterStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlUserWriterStep")
                .tasklet(new UserWriterTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlMerchantWriterStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlMerchantWriterStep")
                .tasklet(new MerchantWriterTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlDepositsWriterStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlDepositsWriterStep")
                .tasklet(new DepositsWriterTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlTransOver100AndAfter8PMStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlTransOver100AndAfter8PM")
                .tasklet(new TransAfter8And100(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlInsufficientBalance(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlInsufficientBalance")
                .tasklet(new InsufficientBalanceUserTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlUniqueMerchantsStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("xmlUniqueMerchantsStep")
                .tasklet(new UniqueMerchantsTasklet(filePath, fileName))
                .build();
    }


    @Bean
    public Step xmlTopTenLargestTransactionsStep(String filePath, String fileName) throws Exception {
        return stepsFactory.get("XmlTopTenLargestTransactionStep")
                .tasklet(new TopTenLargestTransactionsWriterTasklet(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlTypesOfTransactionsStep(String filePath, String fileName) {
        return stepsFactory.get("xmlTypesOfTransactionsStep")
                .tasklet(new TypesOfTransactions(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlTopFiveZipTransVolStep(String filePath, String fileName) {
        return stepsFactory.get("xmlTopFiveZipTransVolStep")
                .tasklet(new TopFiveZipTransVol(filePath, fileName))
                .build();
    }

    @Bean
    public Step xmlRecurringTransactionsStep(String filePath, String fileName) {
        return stepsFactory.get("xmlRecurringTransactionsStep")
                .tasklet(new RecurringTransactionWriterTasklet(filePath, fileName))
                .build();
    }

}
