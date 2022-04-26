package com.smoothstack.alinefinancial.Listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class StepListener  implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("this is from Before Step Execution" + stepExecution.getJobExecution().getExecutionContext());

        System.out.println(" In side Step - print job paramters" + stepExecution.getJobExecution().getJobParameters());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("this is from After Step Execution" + stepExecution.getJobExecution().getExecutionContext());
        return null;
    }
}
