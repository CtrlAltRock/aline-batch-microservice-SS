package com.smoothstack.alinefinancial.Config;

import com.smoothstack.alinefinancial.Listeners.StepListener;
import com.smoothstack.alinefinancial.Models.State;
import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.TransactionV2;
import com.smoothstack.alinefinancial.Models.User;
import com.smoothstack.alinefinancial.Processors.StateProcessor;
import com.smoothstack.alinefinancial.Processors.TransactionProcessor;
import com.smoothstack.alinefinancial.Processors.UserProcessor;
import com.smoothstack.alinefinancial.Writers.ConsoleItemWriter;
import com.smoothstack.alinefinancial.Writers.Writers;
import com.smoothstack.alinefinancial.Writers.XmlItemWriter;
import com.smoothstack.alinefinancial.Writers.XmlStateWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Function;

//@RestController
@Slf4j(topic = "FlatFiles")
//@RequiredArgsConstructor
@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    /*@Autowired
    JobRepository jobRepository;

    @Bean
    public JobLauncher simpleJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }*/

    @Autowired
    StepBuilderFactory stepsFactory;

    @Autowired
    JobBuilderFactory jobsFactory;


    /*@Bean
    public Job transactionJob() {
        return jobsFactory.get("transactionJob")
                .start(transactionV2Flow())
                .end()
                .build();
    }*/

    /*@Bean
    public Job userJob() throws Exception {
        return jobsFactory.get("userJob")
                .start(userFlow())
                .end()
                .build();
    }*/

    @Bean
    public Job stateJob() throws Exception {
        return jobsFactory.get("stateJob")
                .incrementer(new RunIdIncrementer())
                .start(stateFlow())
                .end()
                .build();
    }

    @Bean
    public Flow transactionV2Flow() {
        return new FlowBuilder<SimpleFlow>("transactionV2Flow")
                .start(transactionStep())
                .build();
    }

    @Bean
    public Flow userFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("userFlow")
                .start(userStep())
                .build();
    }

    @Bean
    public Flow stateFlow() throws Exception {
        return new FlowBuilder<SimpleFlow>("stateFlow")
                .start(stateStep())
                .build();
    }

    @Bean
    public Step transactionStep() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        return stepsFactory.get("transactionStep")
                .<Transaction, TransactionV2>chunk(1)
                .reader(csvReader())
                .processor(new TransactionProcessor())
                .writer(Writers.compositeItemWriter())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                //.retryLimit(5)
                //.retryPolicy()
                .taskExecutor(taskExecutor)
                .listener(new StepListener())
                .build();
    }

    @Bean
    public Step userStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(6);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        return stepsFactory.get("userStep")
                .<Transaction, User>chunk(50000)
                .reader(csvReader())
                .processor(new UserProcessor())
                //.processor(new StateProcessor())
                //.processor(Writers.XmlUserWriter("XmlUsers.xml"))
                //.writer(new XmlItemWriter())
                .writer(new ConsoleItemWriter())
                //.writer(Writers.writer11())
                .faultTolerant()
                .skipPolicy(new CustomSkipPolicy())
                //.retryLimit(5)
                .taskExecutor(taskExecutor)
                //.listener(new StepListener())
                .build();
    }

    @Bean
    public Step stateStep() throws Exception {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(500);
        taskExecutor.afterPropertiesSet();

        return stepsFactory.get("stateStep")
                .<Transaction, State>chunk(50000)
                .reader(csvReader())
                .processor(new StateProcessor())
                .writer(new XmlStateWriter())
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
                .resource(new FileSystemResource("src/main/FilesToProcess/card_transaction.v1.csv"))
                .linesToSkip(1)
                .delimited()
                .names("user", "card", "year", "month", "day", "time", "amount", "method", "merchant_name", "merchant_city", "merchant_state", "merchant_zip", "mcc", "errors", "fraud")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>() {{
                    setTargetType(Transaction.class);
                }})
                .build();
    }





    /*@PostMapping(value = "/trigger/start")
    public ResponseEntity<?> startBatchOnFile(@RequestBody RequestFileDetails rfd) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        File processFile = new File("src/main/FilesToProcess/" + rfd.getFileNameToProcess());
        if(processFile.exists()) {
            JobParametersBuilder jPB = new JobParametersBuilder();
            jobLauncher.run(transactionJob(), jPB.toJobParameters());
            System.out.println(rfd.toString());
            System.out.println(rfd.getFileNameToProcess());
        }
        return new ResponseEntity<>("gotcha", HttpStatus.OK);
    }*/




}
