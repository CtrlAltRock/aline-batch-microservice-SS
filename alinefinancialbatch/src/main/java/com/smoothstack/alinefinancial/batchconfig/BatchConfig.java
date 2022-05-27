package com.smoothstack.alinefinancial.batchconfig;

import com.smoothstack.alinefinancial.enums.JobStatus;
import com.smoothstack.alinefinancial.enums.XmlFile;
import com.smoothstack.alinefinancial.flows.Flows;
import com.smoothstack.alinefinancial.models.Transaction;
import com.smoothstack.alinefinancial.processors.*;
import com.smoothstack.alinefinancial.writers.ConsoleItemWriter;
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
import org.springframework.beans.factory.annotation.Value;
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
    private StepBuilderFactory stepsFactory;

    @Autowired
    private JobBuilderFactory jobsFactory;

    @Autowired
    private Flows flows;

    @Value("${inFile}")
    private String filename;

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
                .from(theBigStep()).on(JobStatus.FAILED.toString()).to(flows.failureFlow())
                .from(theBigStep()).on(JobStatus.COMPLETED.toString()).to(flows.analysisFlow())
                .from(flows.analysisFlow()).on(JobStatus.FAILED.toString()).to(flows.failureFlow())
                .from(flows.analysisFlow()).on(JobStatus.COMPLETED.toString()).to(xmlWriterFlow())
                .from(xmlWriterFlow()).on(JobStatus.FAILED.toString()).to(flows.failureFlow())
                .from(xmlWriterFlow()).on(JobStatus.COMPLETED.toString()).to(flows.reportFlow())
                .from(flows.reportFlow()).on(JobStatus.FAILED.toString()).to(flows.failureFlow())
                .from(flows.reportFlow()).on(JobStatus.COMPLETED.toString()).stop()
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
                .resource(new FileSystemResource(filename))
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
                .add(   flows.xmlCardFlow(XmlFile.FILEPATH.toString(), XmlFile.CARDS.toString()),
                        flows.xmlMerchantFlow(XmlFile.FILEPATH.toString(), XmlFile.MERCHANTS.toString()),
                        flows.xmlStateFlow(XmlFile.FILEPATH.toString(), XmlFile.STATES.toString()),
                        flows.xmlUserFlow(XmlFile.FILEPATH.toString(), XmlFile.USERS.toString()),
                        flows.xmlDepositsFlow(XmlFile.FILEPATH.toString(),XmlFile.DEPOSITS.toString()),
                        flows.xmlInsufficientBalanceFlow(XmlFile.FILEPATH.toString(), XmlFile.BALANCES.toString()),
                        flows.xmlTransOver100AndAfter8PMFlow(XmlFile.FILEPATH.toString(),XmlFile.TRANSACTIONAFTER8PMOVER100.toString()),
                        flows.xmlUniqueMerchantsFlow(XmlFile.FILEPATH.toString(), XmlFile.MERCHANTCOUNT.toString()),
                        flows.xmlTopTenLargestTransactionsFlow(XmlFile.FILEPATH.toString(), XmlFile.TOPTENLARGESTTRANSACTIONS.toString()),
                        flows.xmlTypesOfTranasctionsFlow(XmlFile.FILEPATH.toString(), XmlFile.TYPESOFTRANSACTIONS.toString()),
                        flows.xmlTopFiveZipTransVolFlow(XmlFile.FILEPATH.toString(), XmlFile.TOPZIPTRANSVOL.toString()),
                        flows.xmlRecurringTransactionsFlow(XmlFile.FILEPATH.toString(), XmlFile.RECURRINGTRANSACTION.toString()),
                        flows.xmlTopFiveCitiesTransVolFlow(XmlFile.FILEPATH.toString(), XmlFile.TOPFIVECITIESHIGHESTVOLUME.toString()),
                        flows.xmlStatesNoFraudFlow(XmlFile.FILEPATH.toString(), XmlFile.STATESNOFRAUD.toString()),
                        flows.xmlBottomFiveMonthOnlineCountFlow(XmlFile.FILEPATH.toString(), XmlFile.MONTHONLINECOUNT.toString())
                )
                .build();
    }
}
