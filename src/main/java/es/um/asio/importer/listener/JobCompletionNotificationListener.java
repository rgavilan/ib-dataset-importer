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
import es.um.asio.importer.util.ImportResultUtil;

/**
 * Implementation of {@link JobExecutionListenerSupport} responsible for sending the exit status result
 * to Kafka topic.
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

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
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job {} start", jobExecution.getJobInstance().getJobName());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void afterJob(JobExecution jobExecution) {        
        
        InputData<DataSetData> inputData = new InputData<>(ImportResultUtil.createFrom(jobExecution));      
       
        kafkaTemplate.send(topicName, inputData);

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Job {} has finished successfully", jobExecution.getJobInstance().getJobName());
        } else {
            logger.warn("Job {} has NOT finished successfully. Status is {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus());
        }
    }
}
