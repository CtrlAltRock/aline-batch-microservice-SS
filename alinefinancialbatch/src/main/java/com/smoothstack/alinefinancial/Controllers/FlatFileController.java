package com.smoothstack.alinefinancial.Controllers;


import com.smoothstack.alinefinancial.Config.BatchConfig;
import com.smoothstack.alinefinancial.Models.RequestFileDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@Configuration
@EnableBatchProcessing
@RestController
@Slf4j(topic = "FlatFiles")
@RequiredArgsConstructor
public class FlatFileController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private Job userJob;



    @PostMapping(value = "users/trigger/start")
    public ResponseEntity<?> startBatchOnFile(@RequestBody RequestFileDetails rfd) throws Exception {
        File processFile = new File("src/main/FilesToProcess/" + rfd.getFileNameToProcess());
        JobExecution jobExecution = null;
        if (processFile.exists()) {
            JobParametersBuilder jpb = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis());
            jobExecution = jobLauncher.run(userJob, jpb.toJobParameters());
        }
        return new ResponseEntity<>(jobExecution, HttpStatus.OK);
    }

}
