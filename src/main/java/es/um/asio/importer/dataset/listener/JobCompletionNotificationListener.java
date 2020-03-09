package es.um.asio.importer.dataset.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import es.um.asio.importer.dataset.writer.DataItemWriter;

/**
 * Implementacion de {@link JobExecutionListener} para el registro del avance
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    /**
     * Kafka template.
     */
    @Autowired
    private KafkaTemplate<String, ExitStatus> kafkaTemplate;

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
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Writing: {}", jobExecution.getExitStatus());
            }

            kafkaTemplate.send(topicName, jobExecution.getExitStatus());
            LOGGER.info("!!! JOB " + jobExecution.getJobInstance().getJobName() + " FINISHED!");
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Writing: {}", jobExecution.getExitStatus());
            }
            kafkaTemplate.send(topicName, jobExecution.getExitStatus());
            LOGGER.info("Job did not finish. Current status is ", jobExecution.getStatus());
        }
    }
}
