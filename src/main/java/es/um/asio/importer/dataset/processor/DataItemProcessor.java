package es.um.asio.importer.dataset.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;

/**
 * Implementation of {@link ItemProcessor} to transform {@link DataSetData} into {@link InputData<DataSetData>} and
 * add version number
 */
public class DataItemProcessor implements ItemProcessor<DataSetData, InputData<DataSetData>> {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(DataItemProcessor.class);

    /**
     * The job execution id. 
     */
    private long jobExecutionId;

    /**
     * @inheritDoc
     */
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecutionId = stepExecution.getJobExecutionId();
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public InputData<DataSetData> process(final DataSetData data) throws Exception {
        data.setVersion(jobExecutionId);
        logger.info("Processing DataSetData {}", data.getClass());
        return new InputData<>(data);
    }

}