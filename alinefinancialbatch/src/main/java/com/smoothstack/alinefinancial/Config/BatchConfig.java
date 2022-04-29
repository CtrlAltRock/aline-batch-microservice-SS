package com.smoothstack.alinefinancial.Config;

import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Processors.*;
import com.smoothstack.alinefinancial.Tasklets.*;
import com.smoothstack.alinefinancial.Writers.ConsoleItemWriter;

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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j(topic = "BatchConfig")
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    StepBuilderFactory stepsFactory;

    @Autowired
    JobBuilderFactory jobsFactory;

    @Bean
    public Job allJobs() throws Exception {
        return jobsFactory.get("allJobs")
                .incrementer(new RunIdIncrementer())
                .start(splitFlow())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("taskExecutor");
    }

    @Bean
    public Flow splitFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(merchantFlow(), userFlow(), stateFlow(), cardFlow())
                .build();
    }

    @Bean
    public Flow userFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("userFlow")
                .start(userStep())
                .next(xmlUserWriterStep())
                .build();
    }

    @Bean
    public Flow stateFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("stateFlow")
                .start(stateStep())
                .next(xmlStateWriterStep())
                .build();
    }

    @Bean
    public Flow cardFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("cardFlow")
                .start(cardStep())
                .next(xmlCardWriterStep())
                .build();
    }

    @Bean
    public Flow merchantFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("merchantFlow")
                .start(merchantStep())
                .next(xmlMerchantWriterStep())
                .build();
    }

    @Bean
    public Flow transactionFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("transactionFlow")
                .start(transactionStep())
                .next(xmlTransactionWriterStep())
                .build();
    }

    @Bean
    public Step xmlStateWriterStep() {
        return stepsFactory.get("xmlStateWriterStep")
                .tasklet(new XmlStateWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlCardWriterStep() {
        return stepsFactory.get("xmlCardWriterStep")
                .tasklet(new XmlCardWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlUserWriterStep() {
        return stepsFactory.get("xmlUserWriterStep")
                .tasklet(new XmlUserWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlMerchantWriterStep() {
        return stepsFactory.get("xmlMerchantWriterStep")
                .tasklet(new XmlMerchantWriterTasklet())
                .build();
    }

    @Bean
    public Step xmlTransactionWriterStep() {
        return stepsFactory.get("xmlTransactionWriterStep")
                .tasklet(new XmlTransactionWriterTasklet())
                .build();
    }

    @Bean
    public Step merchantStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        //noinspection unchecked
        return stepsFactory.get("merchantStep")
                .<Transaction, Object>chunk(50000)
                .reader(csvReader())
                .processor(new MerchantProcessor())
                .writer(new ConsoleItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .retryLimit(1)
                .retry(IllegalArgumentException.class)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step userStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        //noinspection unchecked
        return stepsFactory.get("userStep")
                .<Transaction, Object>chunk(50000)
                .reader(csvReader())
                .processor(new UserProcessor())
                .writer(new ConsoleItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .retryLimit(1)
                .retry(IllegalArgumentException.class)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step stateStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        //noinspection unchecked
        return stepsFactory.get("stateStep")
                .<Transaction, Object>chunk(50000)
                .reader(csvReader())
                .processor(new StateProcessor())
                .writer(new ConsoleItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .retryLimit(1)
                .retry(IllegalArgumentException.class)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step cardStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        //noinspection unchecked
        return stepsFactory.get("cardStep")
                .<Transaction, Object>chunk(50000)
                .reader(csvReader())
                .processor(new CardProcessor())
                .writer(new ConsoleItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .retryLimit(1)
                .retry(IllegalArgumentException.class)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step transactionStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        //noinspection unchecked
        return stepsFactory.get("transactionStep")
                .<Transaction, Object>chunk(50000)
                .reader(csvReader())
                .processor(new TransactionProcessor())
                .writer(new ConsoleItemWriter())
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
                .name("csvReader")
                .resource(new FileSystemResource("src/main/FilesToProcess/card_transaction.v1.csv"))
                //.resource(new FileSystemResource("src/main/FilesToProcess/test2.csv"))
                .linesToSkip(1)
                .delimited()
                .names("user", "card", "year", "month", "day", "time", "amount", "method", "merchant_name", "merchant_city", "merchant_state", "merchant_zip", "mcc", "errors", "fraud")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }
}
