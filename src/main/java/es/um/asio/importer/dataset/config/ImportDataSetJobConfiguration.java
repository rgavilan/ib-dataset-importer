package es.um.asio.importer.dataset.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            @Qualifier("investigationGroupsStep") final Step s5, @Qualifier("groupContactDatasStep") final Step s6,
            final JobExecutionListener listener) {
        return jobs.get("importProjectsJob").incrementer(new RunIdIncrementer()).listener(listener)
                // Import projects data
                .flow(s1).next(s2).next(s3).next(s4)
                // Import investigation groups data
                .next(s5)
                // Import investigation centers data
                .next(s6).end().build();
    }

}
