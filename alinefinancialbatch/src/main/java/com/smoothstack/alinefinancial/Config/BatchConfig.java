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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;

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
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(512);
        taskExecutor.setMaxPoolSize(1024);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }



    @Bean
    public Flow splitFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .start(theBigStep())
                .from(theBigStep()).on("FAILED").to(failureFlow())
                .from(theBigStep()).on("COMPLETED").to(xmlWriterFlow())
                .next(analysisFlow())
                .next(reportFlow())
                .build();
    }

    @Bean
    public Step theBigStep() throws Exception {
        return stepsFactory.get("theBigStep")
                .<Transaction, Transaction>chunk(50000)
                .reader(csvReader())
                .processor(compositeItemProcessor())
                .writer(new ConsoleItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                .retryLimit(1)
                .retry(Exception.class)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public FlatFileItemReader<Transaction> csvReader() {
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("csvReader")
                .saveState(false)
                .resource(new FileSystemResource("src/main/FilesToProcess/card_transaction.v1.csv"))
                //.resource(new FileSystemResource("src/main/FilesToProcess/test2.csv"))
                //.resource(new FileSystemResource("src/main/FilesToProcess/recurringMerchantTestFile.csv"))
                //.resource(new FileSystemResource("src/main/FilesToProcess/badFormatFile.csv"))
                .linesToSkip(1)
                .delimited()
                .names("user", "card", "year", "month", "day", "time", "amount", "method", "merchant_name", "merchant_city", "merchant_state", "merchant_zip", "mcc", "errors", "fraud")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }

    @Bean
    public CompositeItemProcessor<Transaction, Transaction> compositeItemProcessor() throws Exception {
        CompositeItemProcessor<Transaction, Transaction> compositeProcessor
                = new CompositeItemProcessor<>();

        List<ItemProcessor<Transaction, Transaction>> processors = Arrays.asList(
                new MerchantProcessor(), new UserProcessor(), new StateProcessor(), new AnalysisProcessor(), new CardProcessor() );

        compositeProcessor.setDelegates(processors);
        compositeProcessor.afterPropertiesSet();

        return compositeProcessor;
    }

    @Bean
    public Flow xmlWriterFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlCardFlow")
                .split(taskExecutor())
                .add(xmlCardFlow(), xmlMerchantFlow(), xmlStateFlow(), xmlUserFlow(), xmlDepositsFlow())
                .build();
    }

    @Bean
    public Flow xmlCardFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlCardFlow")
                .start(xmlCardWriterStep())
                .build();
    }

    @Bean
    public Flow xmlMerchantFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlMerchantFlow")
                .start(xmlMerchantWriterStep())
                .build();
    }

    @Bean
    public Flow xmlUserFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlUserFlow")
                .start(xmlUserWriterStep())
                .build();
    }

    @Bean
    public Flow xmlStateFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlStateFlow")
                .start(xmlStateWriterStep())
                .build();
    }

    @Bean
    public Flow xmlDepositsFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("xmlDepositsFlow")
                .start(xmlDepositsWriterStep())
                .build();
    }

    @Bean
    public Flow analysisFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("analysisFlow")
                .start(analysisStep())
                .build();
    }

    @Bean
    public Flow reportFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("reportFlow")
                .start(reportStep())
                .build();
    }

    @Bean
    public Flow failureFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("failureFlow")
                .start(chunkFailureStep())
                .build();
    }

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
    public Step xmlDepositsWriterStep() {
        return stepsFactory.get("xmlDepositsWriterStep")
                .tasklet(new XmlDepositsWriterTasklet())
                .build();
    }


}
