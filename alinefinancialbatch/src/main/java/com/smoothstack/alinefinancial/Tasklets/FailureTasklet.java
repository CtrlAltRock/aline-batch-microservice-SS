package com.smoothstack.alinefinancial.Tasklets;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j(topic = "FailureTasklet")
public class FailureTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("theBigStep has Failed: ");
        errorMessage.append(Time.now());
        log.error(errorMessage.toString());
        return RepeatStatus.FINISHED;
    }
}
