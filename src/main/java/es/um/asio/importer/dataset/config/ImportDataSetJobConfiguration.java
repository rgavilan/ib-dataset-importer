package es.um.asio.importer.dataset.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import es.um.asio.importer.constants.Constants;

/**
 * Job that processes XML files and sends them to Kafka topic
 */
@Configuration
public class ImportDataSetJobConfiguration {
    
    /**
     * Generates {@link Job} for imports all XML files
     *
     * @param jobs the jobs
     * @param listener the listener
     * @param flows the flows
     * @return the job
     */
    @Bean
    public Job importDataSetJob(final JobBuilderFactory jobs, final JobExecutionListener listener,
            final List<ImportDataSetFlowConfigurationBase> flows) {  
        return jobs.get(Constants.DATASET_JOB_NAME)
                .incrementer(new RunIdIncrementer()).listener(listener)
                .start(importDataSetSplitFlow(flows.stream().map(ImportDataSetFlowConfigurationBase::getFlow).collect(Collectors.toList()).toArray(new Flow[0])))
                .build().build();
    }    
   
    /**
     * Generates the {@link Flow} with dataset import
     *
     * @param flows the flows
     * @return the flow
     */
    private Flow importDataSetSplitFlow(Flow ...flows) {
        return new FlowBuilder<SimpleFlow>("importDataSetSplitFlow")
            .split(new SimpleAsyncTaskExecutor())
            .add(flows)
            .build();
    }  
}
