package com.smoothstack.alinefinancial.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "StepListener")
public class StepListener  implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("before Step");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        return null;
    }
}
