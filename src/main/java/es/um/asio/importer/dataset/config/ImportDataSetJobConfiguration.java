package es.um.asio.importer.dataset.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.domain.project.DateProjects;
import es.um.asio.domain.project.PlannedJustificationsProjects;
import es.um.asio.domain.project.Project;
import es.um.asio.domain.project.ProjectOrigins;
import es.um.asio.importer.dataset.processor.DataItemProcessor;
import es.um.asio.importer.dataset.writer.DataItemWriter;
import es.um.asio.importer.marshaller.DataConverter;
import es.um.asio.importer.marshaller.DataSetFieldSetMapper;
import es.um.asio.importer.marshaller.DataSetMarshaller;

/**
 * Job que procesa ficheros XML y los manda a la cola Kafka
 */
@Configuration
public class ImportDataSetJobConfiguration {

    /**
     * Genera el {@link Job} de importacion de proyectos
     *
     * @param jobs
     *            Instancia de {@link JobBuilderFactory} en la que se registran los job
     * @param s1
     *            Primer paso de la ejecucion
     * @param listener
     *            {@link JobExecutionListener} en el que se registra el avance y finalizacion
     * @return
     */
    @Bean
    public Job importProjectsJob(final JobBuilderFactory jobs, @Qualifier("projectStep") final Step s1,
            @Qualifier("plannedJustificationsProjectsStep") final Step s2,
            @Qualifier("projectOriginsStep") final Step s3, @Qualifier("dateProjectsStep") final Step s4,
            @Qualifier("investigationGroupsStep") final Step s5, final JobExecutionListener listener) {
        return jobs.get("importProjectsJob").incrementer(new RunIdIncrementer()).listener(listener)
                // Import projects data
                .flow(s1).next(s2).next(s3).next(s4)
                // Import investigation groups data
                .next(s5).end().build();
    }

}
