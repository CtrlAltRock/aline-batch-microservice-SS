package com.smoothstack.alinefinancial.flows;

import com.smoothstack.alinefinancial.steps.Steps;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class Flows {

    @Autowired
    Steps steps;

    @Bean
    public Flow reportFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("reportFlow")
                .start(steps.reportStep())
                .build();
    }

    @Bean
    public Flow analysisFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("analysisFlow")
                .start(steps.analysisStep())
                .build();
    }

    @Bean
    public Flow failureFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("failureFlow")
                .start(steps.chunkFailureStep())
                .build();
    }

    @Bean
    public Flow xmlCardFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlCardFlow")
                .start(steps.xmlCardWriterStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlMerchantFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlMerchantFlow")
                .start(steps.xmlMerchantWriterStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlUserFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlUserFlow")
                .start(steps.xmlUserWriterStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlStateFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlStateFlow")
                .start(steps.xmlStateWriterStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlDepositsFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlDepositsFlow")
                .start(steps.xmlDepositsWriterStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlTransOver100AndAfter8PMFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlTransOver100AndAfter8PMFlow")
                .start(steps.xmlTransOver100AndAfter8PMStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlInsufficientBalanceFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlInsufficientBalanceFlow")
                .start(steps.xmlInsufficientBalance(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlUniqueMerchantsFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlUniqueMerchantsFlow")
                .start(steps.xmlUniqueMerchantsStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlTopTenLargestTransactionsFlow(String filePath, String fileName) throws Exception {
        return new FlowBuilder<SimpleFlow>("XmlTopTenLargestTransactionsFlow")
                .start(steps.xmlTopTenLargestTransactionsStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlTypesOfTranasctionsFlow(String filePath, String fileName) {
        return new FlowBuilder<SimpleFlow>("xmlTypesOfTransactionsFlow")
                .start(steps.xmlTypesOfTransactionsStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlTopFiveZipTransVolFlow(String filePath, String fileName) {
        return new FlowBuilder<SimpleFlow>("xmlTopFiveZipTransVolFlow")
                .start(steps.xmlTopFiveZipTransVolStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlRecurringTransactionsFlow(String filePath, String fileName) {
        return new FlowBuilder<SimpleFlow>("xmlRecurringTransactionsFlow")
                .start(steps.xmlRecurringTransactionsStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlTopFiveCitiesTransVolFlow(String filePath, String fileName) {
        return new FlowBuilder<SimpleFlow>("xmlTopFiveCitiesTransVolFlow")
                .start(steps.xmlTopFiveCitiesTransVolStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlStatesNoFraudFlow(String filePath, String fileName) {
        return new FlowBuilder<SimpleFlow>("xmlStatesNoFraudFlow")
                .start(steps.xmlStatesNoFraudStep(filePath, fileName))
                .build();
    }

    @Bean
    public Flow xmlBottomFiveMonthOnlineCountFlow(String filePath, String fileName) {
        return new FlowBuilder<SimpleFlow>("xmlBottomFiveMonthOnlineCountFlow")
                .start(steps.xmlBottomFiveMonthOnlineCountStep(filePath, fileName))
                .build();
    }

}
