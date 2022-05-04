package com.smoothstack.alinefinancial.GeneratorTests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
        properties = {
                "input.path = C:\\Users\\Sam\\Documents\\GitHub\\aline-batch-microservice-SS\\alinefinancialbatch\\src\\main\\FilesToProcess\\testInsufficientBalances.csv"
        })

public class InsufficientBalancesTest {

}
