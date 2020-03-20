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

/**
 * Job que procesa ficheros XML y los manda a la cola Kafka
 */
@Configuration
public class ImportDataSetJobConfiguration {

    /**
     * Genera el {@link Job} de importacion de todo el dataset
     *
     * @return
     */
    @Bean
    public Job importDataSetJob(final JobBuilderFactory jobs, final JobExecutionListener listener,
            final List<ImportDataSetFlowConfigurationBase> flows) {  
        return jobs.get("importDataSetJob")
                .incrementer(new RunIdIncrementer()).listener(listener)
                .start(importDataSetSplitFlow(flows.stream().map(x -> x.getFlow()).collect(Collectors.toList()).toArray(new Flow[0])))
                .build().build();
    }    
   
    private Flow importDataSetSplitFlow(Flow ...flows) {
        return new FlowBuilder<SimpleFlow>("importDataSetSplitFlow")
            .split(new SimpleAsyncTaskExecutor())
            .add(flows)
            .build();
    }  
}
