package com.smoothstack.alinefinancial.Config;

import com.smoothstack.alinefinancial.Flows.Flows;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Processors.*;
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

    @Autowired
    Flows flows;

    @Bean
    public Job theOnlyJob() throws Exception {
        return jobsFactory.get("theOnlyJob")
                .incrementer(new RunIdIncrementer())
                .start(theFlow())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(12);
        taskExecutor.setMaxPoolSize(12);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }



    @Bean
    public Flow theFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .start(theBigStep())
                .from(theBigStep()).on("FAILED").to(flows.failureFlow())
                .from(theBigStep()).on("COMPLETED").to(flows.analysisFlow())
                .from(flows.analysisFlow()).on("FAILED").to(flows.failureFlow())
                .from(flows.analysisFlow()).on("COMPLETED").to(xmlWriterFlow())
                .from(xmlWriterFlow()).on("FAILED").to(flows.failureFlow())
                .from(xmlWriterFlow()).on("COMPLETED").to(flows.reportFlow())
                .from(flows.reportFlow()).on("FAILED").to(flows.failureFlow())
                .from(flows.reportFlow()).on("COMPLETED").stop()
                .build();
    }

    @Bean
    public Step theBigStep() throws Exception {

        return stepsFactory.get("theBigStep")
                .<Transaction, Transaction>chunk(20000)
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
                //.resource(new FileSystemResource("src/main/FilesToProcess/recurringMerchantTestFile.csv"))
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
        return new FlowBuilder<SimpleFlow>("xmlWriterFlow")
                .split(taskExecutor())
                .add(flows.xmlCardFlow(), flows.xmlMerchantFlow(), flows.xmlStateFlow(),
                        flows.xmlUserFlow(), flows.xmlDepositsFlow(), flows.xmlInsufficientBalanceFlow(),
                        flows.xmlTransOver100AndAfter8PMFlow(), flows.xmlUniqueMerchantsFlow(),
                        flows.xmlTopFiveRecurringMerchantTransactionsFlow())
                .build();
    }
}
