package com.smoothstack.alinefinancial.Processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class Processors {
    @Bean
    public static CompositeItemProcessor<Object, Object> compositeItemProcessor() {
        CompositeItemProcessor compProcessor = new CompositeItemProcessor<>();
        //compProcessor.setDelegates(new TransactionProcessor());
        return compProcessor;
    }

}
