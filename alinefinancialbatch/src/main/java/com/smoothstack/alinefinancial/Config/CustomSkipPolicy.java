package com.smoothstack.alinefinancial.Config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class CustomSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        if (t instanceof Throwable){
            skipCount++;
            return true;
        }

        return false;
    }
}
