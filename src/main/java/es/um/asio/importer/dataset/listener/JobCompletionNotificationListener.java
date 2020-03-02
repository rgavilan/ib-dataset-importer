package es.um.asio.importer.dataset.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * Implementacion de {@link JobExecutionListener} para el registro del avance
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("!!! JOB " + jobExecution.getJobInstance().getJobName() + " FINISHED!");
        } else {
            LOGGER.info("Job did not finish. Current status is {}", jobExecution.getStatus());
        }
    }
}
