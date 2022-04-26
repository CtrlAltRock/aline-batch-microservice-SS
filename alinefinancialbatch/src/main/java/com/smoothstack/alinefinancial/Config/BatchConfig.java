package com.smoothstack.alinefinancial.Config;

import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Processors.CardProcessor;
import com.smoothstack.alinefinancial.Processors.MerchantProcessor;
import com.smoothstack.alinefinancial.Processors.StateProcessor;
import com.smoothstack.alinefinancial.Processors.UserProcessor;
import com.smoothstack.alinefinancial.Writers.XmlItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "FlatFiles")
@Configuration
@EnableBatchProcessing
public class BatchConfig {


    @Autowired
    StepBuilderFactory stepsFactory;

    @Autowired
    JobBuilderFactory jobsFactory;

    @Bean
    public Job allJobs() throws Exception {
        return jobsFactory.get("stateJob")
                .incrementer(new RunIdIncrementer())
                .start(bigFlow())
                .end()
                .build();
    }


    @Bean
    public Flow bigFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("stateFlow")
                .start(bigStep())
                .build();
    }

    @Bean
    public Step bigStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        return stepsFactory.get("bigStep")
                .<Transaction, Object>chunk(50000)
                .reader(csvReader())
                .processor(compositeItemProcessor())
                .writer(new XmlItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .retryLimit(1)
                .retry(IllegalArgumentException.class)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> csvReader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("csvflatfileitemreader")
                //.resource(new FileSystemResource("src/main/FilesToProcess/card_transaction.v1.csv"))
                .resource(new FileSystemResource("src/main/FilesToProcess/test2.csv"))
                .linesToSkip(1)
                .delimited()
                .names("user", "card", "year", "month", "day", "time", "amount", "method", "merchant_name", "merchant_city", "merchant_state", "merchant_zip", "mcc", "errors", "fraud")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }

    @Bean
    public CompositeItemProcessor<Transaction, Object> compositeItemProcessor() throws Exception {
        CompositeItemProcessor<Transaction, Object> compositeProcessor = new CompositeItemProcessor<>();

        List<ItemProcessor<Transaction, Object>> processors = Arrays.asList(
                new UserProcessor(),
                new CardProcessor(),
                new MerchantProcessor(),
                new StateProcessor()
        );

        compositeProcessor.setDelegates(processors);
        compositeProcessor.afterPropertiesSet();

        return compositeProcessor;
    }
}
