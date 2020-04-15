package es.um.asio.importer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.domain.importResult.ExitStatus;
import es.um.asio.domain.importResult.ExitStatusCode;

/**
 * Implementation of {@link JobExecutionListenerSupport} responsible for sending the exit status result
 * to Kafka topic.
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    /**
     * Kafka template.
     */
    @Autowired
    private KafkaTemplate<String, InputData<DataSetData>> kafkaTemplate;

    /**
     * Topic name
     */
    @Value("${app.kafka.input-topic-name}")
    private String topicName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        ExitStatus existStatus = new ExitStatus(ExitStatusCode.valueOf(jobExecution.getExitStatus().getExitCode()));
        existStatus.setVersion(jobExecution.getId());
        InputData<DataSetData> inputData = new InputData<>(existStatus);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Writing: {}", jobExecution.getExitStatus());
        }
        kafkaTemplate.send(topicName, inputData);

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("JOB {} FINISHED!!!", jobExecution.getJobInstance().getJobName());
        } else {
            LOGGER.info("Job did not finish. Current status is {}", jobExecution.getStatus());
        }
    }
}
