package com.smoothstack.alinefinancial;

import com.smoothstack.alinefinancial.Config.BatchConfig;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {BatchConfig.class})
@TestPropertySource(
		properties = {
				"input.path = C:\\Users\\Sam\\Documents\\GitHub\\aline-batch-microservice-SS\\alinefinancialbatch\\src\\main\\FilesToProcess\\test2.csv",
				"output.path = C:\\Users\\Sam\\Documents\\GitHub\\aline-batch-microservice-SS\\alinefinancialbatch\\src\\test\\ProcessedOutTestFiles\\"
		})
class AlinefinancialApplicationTests {

	private static final String EXPECTED_OUTPUT = "src/test/resources/output/expected-output.json";

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@After
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		return paramsBuilder.toJobParameters();
	}

	@Test
	public void givenReferenceOutput_whenJobExecuted_thenSuccess() throws Exception {


		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		JobInstance actualJobInstance = jobExecution.getJobInstance();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualJobInstance.getJobName(), is("transactionJob"));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
//		AssertFile.assertFileEquals(expectedResult, actualResult);
	}

	@Test
	public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {


		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("transaction step");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
//		AssertFile.assertFileEquals(expectedResult, actualResult);
	}

	@Test
	public void whenStep2Executed_thenSuccess() {

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("xmlWriterStep");
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualExitStatus.getExitCode(), is("COMPLETED"));
//		actualStepExecutions.forEach(stepExecution -> {assertThat(stepExecution.getWriteCount(), is(8));});
	}

}
