package com.smoothstack.alinefinancial.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic="CustomSkipPolicy")
public class CustomSkipPolicy implements SkipPolicy {

    private StringBuilder skipMessage = new StringBuilder();

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (t instanceof Throwable){
            skipCount++;
            skipMessage.append("skipCount: ");
            skipMessage.append(skipCount);
            log.error(t.toString());
            log.error(skipMessage.toString());
            return true;
        }

        return false;
    }
}
