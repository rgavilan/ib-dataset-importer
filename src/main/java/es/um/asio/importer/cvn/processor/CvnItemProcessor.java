package es.um.asio.importer.cvn.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.importer.cvn.mapper.CvnRootBeanMapper;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;

/**
 * Processor in charge of converts {@link CvnRootBean} to into {@link InputData<DataSetData>} and
 * add version number
 */
public class CvnItemProcessor implements ItemProcessor<CvnRootBean,InputData<DataSetData>> {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(CvnItemProcessor.class);

    
    /**
     *  The mapper.
    */
    @Autowired
    CvnRootBeanMapper mapper;
    
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
     * {@inheritDoc}
     */
    @Override
    public InputData<DataSetData> process(CvnRootBean cvnRootBean) throws Exception {       
        DataSetData domainCvn = mapper.map(cvnRootBean);        
        domainCvn.setVersion(jobExecutionId);
        
        logger.info("Processing CVN data cvn");
        return new InputData<>(domainCvn);
    }

}
