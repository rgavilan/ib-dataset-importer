/*
 * 
 */
package es.um.asio.importer.cnv;

import java.util.Arrays;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.importer.cnv.model.beans.CvnRootBean;
import es.um.asio.importer.cnv.processor.CvnToDomainCvnProcessor;
import es.um.asio.importer.cnv.reader.CvnReader;
import es.um.asio.importer.processor.DataItemProcessor;
import es.um.asio.importer.writer.DataItemWriter;
import es.um.asio.importer.constants.Constants;


/**
 * Job that processes CVNs and sends them to Kafka topic
 */
@Configuration
public class ImportCvnJobConfiguration {
  
    /**
     * Generates {@link Job} for imports CVN files
     *
     * @param jobs the jobs
     * @param listener the listener
     * @param flows the flows
     * @return the job
     */
    @Bean
    public Job importCvnJob(final JobBuilderFactory jobs, @Qualifier("CvnStep") final Step s1, final JobExecutionListener listener) {  
        return jobs.get(Constants.CVN_JOB_NAME)
                .incrementer(new RunIdIncrementer()).listener(listener)
                .start(s1)
                .build();
    }
    
    
    /**
     * Gets CVN step.
     *
     * @param stepBuilderFactory the step builder factory
     * @param reader the reader
     * @param writer the writer
     * @param processor the processor
     * @return the step
     */
    @SuppressWarnings("unchecked")
    @Bean
    @Qualifier("CvnStep")
    public Step cvnStep(final StepBuilderFactory stepBuilderFactory) {       
        return stepBuilderFactory.get("cvnStep").<CvnRootBean, InputData<DataSetData>> chunk(10)
                .reader(getReader())
                .processor(getCompositeProcessor())              
                .writer(getWriter())
                .build();
      }
    
    /**
     * Composite CVN processor.
     *
     * @return the composite item processor
     */
    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })    
    protected CompositeItemProcessor getCompositeProcessor() {
        CompositeItemProcessor processor = new CompositeItemProcessor();
        processor.setDelegates(Arrays.asList(getCvnToDomainCvnProcessor(),getDataItemProcessor()));

        return processor;
    }
    
    /**
     * Gets the data item processor.
     *
     * @return the data item processor
     */
    @Bean
    protected ItemProcessor<DataSetData, InputData<DataSetData>> getDataItemProcessor(){
        return new DataItemProcessor();
    }
    
    /**
     * Gets the cvn to domain cvn processor.
     *
     * @return the cvn to domain cvn processor
     */
    @Bean
    protected ItemProcessor<CvnRootBean, es.um.asio.domain.cvn.CvnRootBean> getCvnToDomainCvnProcessor(){
        return new CvnToDomainCvnProcessor();
    }
   
    /**
     * Gets the instance of {@link CvnReader}
     *
     * @return the processor
     */
    @Bean
    protected ItemReader<CvnRootBean> getReader() {
        return new CvnReader();
    }    
    
     /**
      * Gets the writer.
      *
      * @return the writer
      */
     @Bean
     protected ItemWriter<InputData<DataSetData>> getWriter() {
         return new DataItemWriter();
     }
 
}
