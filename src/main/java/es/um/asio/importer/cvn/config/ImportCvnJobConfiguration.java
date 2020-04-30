package es.um.asio.importer.cvn.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.importer.cvn.listener.CvnSkipListener;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.processor.CvnItemProcessor;
import es.um.asio.importer.cvn.reader.CvnReader;
import es.um.asio.importer.cvn.skippolicy.CvnRequestExceptionSkipPolicy;
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
    @Bean
    @Qualifier("CvnStep")
    public Step cvnStep(final StepBuilderFactory stepBuilderFactory) {       
        return stepBuilderFactory.get("cvnStep").<CvnRootBean, InputData<DataSetData>> chunk(10)
                .reader(getReader())
                .processor(getCvnItemProcessor())              
                .writer(getWriter())
                .faultTolerant()
                .skipPolicy(new CvnRequestExceptionSkipPolicy())
                .listener(new CvnSkipListener())
                .build();
      }
     
    
    /**
     * Gets the cvn item processor.
     *
     * @return the cvn item processor
     */
    @Bean
    protected ItemProcessor<CvnRootBean, InputData<DataSetData>> getCvnItemProcessor(){
        return new CvnItemProcessor();
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
