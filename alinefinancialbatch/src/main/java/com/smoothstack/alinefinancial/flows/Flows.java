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
    public Flow xmlCardFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlCardFlow")
                .start(steps.xmlCardWriterStep())
                .build();
    }

    @Bean
    public Flow xmlMerchantFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlMerchantFlow")
                .start(steps.xmlMerchantWriterStep())
                .build();
    }

    @Bean
    public Flow xmlUserFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlUserFlow")
                .start(steps.xmlUserWriterStep())
                .build();
    }

    @Bean
    public Flow xmlStateFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlStateFlow")
                .start(steps.xmlStateWriterStep())
                .build();
    }

    @Bean
    public Flow xmlDepositsFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlDepositsFlow")
                .start(steps.xmlDepositsWriterStep())
                .build();
    }

    @Bean
    public Flow xmlTransOver100AndAfter8PMFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlTransOver100AndAfter8PMFlow")
                .start(steps.xmlTransOver100AndAfter8PMStep())
                .build();
    }

    @Bean
    public Flow xmlInsufficientBalanceFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlInsufficientBalanceFlow")
                .start(steps.xmlInsufficientBalance())
                .build();
    }

    @Bean
    public Flow xmlUniqueMerchantsFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlUniqueMerchantsFlow")
                .start(steps.xmlUniqueMerchantsStep())
                .build();
    }

    @Bean
    public Flow xmlTopFiveRecurringMerchantTransactionsFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlTopFiveRecurringMerchantTransactionsFlow")
                .start(steps.xmlTopFiveRecurringMerchantTransactionsStep())
                .build();
    }

    @Bean
    public Flow xmlTopTenLargestTransactionsFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("XmlTopTenLargestTransactionsFlow")
                .start(steps.xmlTopTenLargestTransactionsStep())
                .build();
    }

    @Bean
    public Flow xmlTypesOfTranasctionsFlow() {
        return new FlowBuilder<SimpleFlow>("xmlTypesOfTransactionsFlow")
                .start(steps.xmlTypesOfTransactionsStep())
                .build();
    }


}
