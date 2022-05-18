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
    public Step xmlStateWriterStep() throws Exception {
        return stepsFactory.get("xmlStateWriterStep")
                .tasklet(new XmlStateWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlCardWriterStep() throws Exception {
        return stepsFactory.get("xmlCardWriterStep")
                .tasklet(new XmlCardWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlUserWriterStep() throws Exception {
        return stepsFactory.get("xmlUserWriterStep")
                .tasklet(new XmlUserWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlMerchantWriterStep() throws Exception {
        return stepsFactory.get("xmlMerchantWriterStep")
                .tasklet(new XmlMerchantWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlDepositsWriterStep() throws Exception {
        return stepsFactory.get("xmlDepositsWriterStep")
                .tasklet(new XmlDepositsWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlTransOver100AndAfter8PMStep() throws Exception {
        return stepsFactory.get("xmlTransOver100AndAfter8PM")
                .tasklet(new XmlTransAfter8And100())
                .build();
    }

    @Bean
    public Step xmlInsufficientBalance() throws Exception {
        return stepsFactory.get("xmlInsufficientBalance")
                .tasklet(new XmlInsufficientBalanceUserTasklet())
                .build();
    }

    @Bean
    public Step xmlUniqueMerchantsStep() throws Exception {
        return stepsFactory.get("xmlUniqueMerchantsStep")
                .tasklet(new XmlUniqueMerchantsTasklet())
                .build();
    }

    @Bean
    public Step xmlTopFiveRecurringMerchantTransactionsStep() throws Exception {
        return stepsFactory.get("xmlTop5RecurringMerchantTransactionsStep")
                .tasklet(new XmlMerchantTopFiveTransactions())
                .build();
    }

    @Bean
    public Step xmlTopTenLargestTransactionsStep() throws Exception {
        return stepsFactory.get("XmlTopTenLargestTransactionStep")
                .tasklet(new XmlTopTenLargestTransactionsWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlTypesOfTransactionsStep() {
        return stepsFactory.get("xmlTypesOfTransactionsStep")
                .tasklet(new XmlTypesOfTransactions())
                .build();
    }

}
