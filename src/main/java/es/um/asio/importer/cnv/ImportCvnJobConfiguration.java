package es.um.asio.importer.cnv;

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
import es.um.asio.importer.cnv.model.beans.CvnRootBean;
import es.um.asio.importer.cnv.reader.CvnReader;
import es.um.asio.importer.processor.DataItemProcessor;
import es.um.asio.importer.writer.DataItemWriter;

/**
 * Job that processes CVNs and sends them to Kafka topic
 */
//@Configuration
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
        return jobs.get("importCvnJob")
                .incrementer(new RunIdIncrementer()).listener(listener)
                .start(s1)
                .build();
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
     * Gets the processor.
     *
     * @return the processor
     */
    @SuppressWarnings("unchecked")
    @Bean
    protected ItemProcessor<CvnRootBean, InputData<DataSetData>> getProcessor() {
       return (ItemProcessor<CvnRootBean, InputData<DataSetData>>) new DataItemWriter();
    }    
   
  
     /**
      * Gets the writer.
      *
      * @return the writer
      */
     @SuppressWarnings("unchecked")
     @Bean
     protected ItemWriter<InputData<DataSetData>> getWriter() {
         return (ItemWriter<InputData<DataSetData>>) new DataItemProcessor();
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
     public Step cvnStep(final StepBuilderFactory stepBuilderFactory, final ItemReader<CvnRootBean> reader) {       
         return stepBuilderFactory.get("cvnStep").<CvnRootBean, InputData<DataSetData>> chunk(10)
                 .reader(reader)
                 .processor(getProcessor())
                 .writer(getWriter())
                 .build();
       }
}
