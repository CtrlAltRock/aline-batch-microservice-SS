package com.smoothstack.alinefinancial.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic="CustomSkipPolicy")
public class CustomSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (t instanceof Throwable){
            skipCount++;
            log.info(t.toString());
            log.info("skipCount: " + skipCount);
            return true;
        }

        return false;
    }
}
