package com.smoothstack.alinefinancial.GeneratorTests;

import com.smoothstack.alinefinancial.Caches.UserCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class UsersGenerationTest {

    private static UserCache userCache = UserCache.getInstance();

    @Test
    public void generate() {
        assertTrue(userCache.getGeneratedUsers().isEmpty());
    }

}
