package es.um.asio.importer.dataset.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
import es.um.asio.domain.project.PlannedJustificationsProject;
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
public class ImportProjectsDataSetJobConfiguration {

    /**
     * Data directory path
     */
    @Value("${app.data.path}")
    private String dataPath;

    /**
     * Devuelve la implementacion de {@link ItemReader} necesaria para la funcionalidad.
     * Utiliza la clase {@link FlatFileItemReader} de Spring Batch para la logica de lectura.
     *
     * @return Implementacion de {@link ItemReader}
     */
    @Bean
    @Primary
    public ItemReader<DataSetData> projectReader() {

        final Class<Project> targetClass = Project.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDPROYECTO", "projectId");
        propertiesBinding.put("NOMBRE", "name");
        propertiesBinding.put("PROYECTOFINALISTA", "finalistProject");
        propertiesBinding.put("LIMITATIVO", "limitative");

        final DataConverter<Project> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<Project>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<Project> ummarshaller = new DataSetMarshaller<>(targetClass);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile("dataset/Proyectos/Proyectos.xml"));
        reader.setUnmarshaller(ummarshaller);
        reader.setFragmentRootElementName(DataSetMarshaller.DATA_RECORD);

        return reader;
    }

    /**
     * Devuelve la implementacion de {@link ItemReader} necesaria para la funcionalidad.
     * Utiliza la clase {@link FlatFileItemReader} de Spring Batch para la logica de lectura.
     *
     * @return Implementacion de {@link ItemReader}
     */
    @Bean
    public ItemReader<DataSetData> plannedJustificationsProjectsReader() {

        final Class<PlannedJustificationsProject> targetClass = PlannedJustificationsProject.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDPROYECTO", "projectId");
        propertiesBinding.put("NUMEROJUSTIFICACIONPREVISTA", "expectedJustificationNumber");
        propertiesBinding.put("CODTIPOORIGEN", "typeOriginCode");
        propertiesBinding.put("IDORIGENPROYECTO", "originProjectId");
        propertiesBinding.put("TIPOJUSTIFICACION", "justificationType");
        propertiesBinding.put("CLASEJUSTIFICACION", "justificationClass");
        propertiesBinding.put("FECHAINICIODEVENGO", "startDateIncome");
        propertiesBinding.put("FECHAFINDEVENGO", "endDateIncome");
        propertiesBinding.put("FECHAINICIOCONTABILIZACION", "startDateAccounting");
        propertiesBinding.put("FECHAINICIOPAGO", "startDatePayment");
        propertiesBinding.put("FECHAFINPAGO", "endDatePayment");
        propertiesBinding.put("FECHAPREVISTA", "expectedDate");
        propertiesBinding.put("OBSERVACIONES", "remarks");

        final DataConverter<PlannedJustificationsProject> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<PlannedJustificationsProject>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<PlannedJustificationsProject> ummarshaller = new DataSetMarshaller<>(targetClass);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile("dataset/Proyectos/Justificaciones previstas proyectos.xml"));
        reader.setUnmarshaller(ummarshaller);
        reader.setFragmentRootElementName(DataSetMarshaller.DATA_RECORD);

        return reader;
    }

    /**
     * Devuelve la implementacion de {@link ItemReader} necesaria para la funcionalidad.
     * Utiliza la clase {@link FlatFileItemReader} de Spring Batch para la logica de lectura.
     *
     * @return Implementacion de {@link ItemReader}
     */
    @Bean
    public ItemReader<DataSetData> projectOriginsReader() {

        final Class<ProjectOrigins> targetClass = ProjectOrigins.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("CODTIPOORIGEN", "typeOriginCode");
        propertiesBinding.put("IDORIGENPROYECTO", "originProjectId");
        propertiesBinding.put("DESCRIPCION", "description");

        final DataConverter<ProjectOrigins> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<ProjectOrigins>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<ProjectOrigins> ummarshaller = new DataSetMarshaller<>(targetClass);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile("dataset/Proyectos/Origenes proyectos.xml"));
        reader.setUnmarshaller(ummarshaller);
        reader.setFragmentRootElementName(DataSetMarshaller.DATA_RECORD);

        return reader;
    }

    /**
     * Devuelve la implementacion de {@link ItemReader} necesaria para la funcionalidad.
     * Utiliza la clase {@link FlatFileItemReader} de Spring Batch para la logica de lectura.
     *
     * @return Implementacion de {@link ItemReader}
     */
    @Bean
    public ItemReader<DataSetData> dateProjectsReader() {

        final Class<DateProjects> targetClass = DateProjects.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDPROYECTO", "projectId");
        propertiesBinding.put("NUMERO", "number");
        propertiesBinding.put("FECHAINICIOEXPEDIENTE", "recordStartDate");
        propertiesBinding.put("FECHAINICIOPROYECTO", "projectStartDate");
        propertiesBinding.put("FECHAFINPROYECTO", "projectEndDate");
        propertiesBinding.put("CODTIPOMOTIVOCAMBIOFECHA", "dateChangeReasonTypeCode");
        propertiesBinding.put("MOTIVOCAMBIOFECHA", "dateChangeReason");

        final DataConverter<DateProjects> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<DateProjects>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<DateProjects> ummarshaller = new DataSetMarshaller<>(targetClass);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile("dataset/Proyectos/Fechas proyectos.xml"));
        reader.setUnmarshaller(ummarshaller);
        reader.setFragmentRootElementName(DataSetMarshaller.DATA_RECORD);

        return reader;
    }

    /**
     * Devuelve una instancia de {@link DataItemProcessor}
     *
     * @return Instancia de {@link DataItemProcessor}
     */
    @Bean
    @Primary
    public ItemProcessor<DataSetData, InputData<DataSetData>> projectsProcessor() {
        return new DataItemProcessor();
    }

    /**
     * Devuelve una instancia de {@link DataItemWriter}
     *
     * @return instancia de {@link DataItemWriter}
     */
    @Bean
    @Primary
    public ItemWriter<InputData<DataSetData>> projectsWriter() {
        return new DataItemWriter();
    }

    /**
     * Genera el primer paso de la ejecucion del job
     *
     * @param stepBuilderFactory
     *            Instancia de {@link StepBuilderFactory} para generar el {@link Step}
     * @param reader
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Project}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Project}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Project}
     * @return El {@link Step} construido
     */
    @Bean
    @Qualifier("projectStep")
    public Step projectStep(final StepBuilderFactory stepBuilderFactory, final ItemReader<DataSetData> reader,
            final ItemWriter<InputData<DataSetData>> writer,
            final ItemProcessor<DataSetData, InputData<DataSetData>> processor) {
        // @formatter:off

        return stepBuilderFactory.get("step1").<DataSetData, InputData<DataSetData>> chunk(10)
                .reader(projectReader())
                .processor(projectsProcessor())
                .writer(projectsWriter())
                .build();

        // @formatter:on

    }

    /**
     * Genera el segundo paso de la ejecucion del job
     *
     * @param stepBuilderFactory
     *            Instancia de {@link StepBuilderFactory} para generar el {@link Step}
     * @param reader
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Project}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Project}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Project}
     * @return El {@link Step} construido
     */
    @Bean
    @Qualifier("plannedJustificationsProjectsStep")
    public Step plannedJustificationsProjectsStep(final StepBuilderFactory stepBuilderFactory,
            final ItemReader<DataSetData> reader, final ItemWriter<InputData<DataSetData>> writer,
            final ItemProcessor<DataSetData, InputData<DataSetData>> processor) {
        // @formatter:off

        return stepBuilderFactory.get("step2").<DataSetData, InputData<DataSetData>> chunk(10)
                .reader(plannedJustificationsProjectsReader())
                .processor(processor)
                .writer(writer)
                .build();

        // @formatter:on

    }

    /**
     * Genera el tercer paso de la ejecucion del job
     *
     * @param stepBuilderFactory
     *            Instancia de {@link StepBuilderFactory} para generar el {@link Step}
     * @param reader
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Project}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Project}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Project}
     * @return El {@link Step} construido
     */
    @Bean
    @Qualifier("projectOriginsStep")
    public Step projectOriginsStep(final StepBuilderFactory stepBuilderFactory, final ItemReader<DataSetData> reader,
            final ItemWriter<InputData<DataSetData>> writer,
            final ItemProcessor<DataSetData, InputData<DataSetData>> processor) {
        // @formatter:off

        return stepBuilderFactory.get("step3").<DataSetData, InputData<DataSetData>> chunk(10)
                .reader(projectOriginsReader())
                .processor(processor)
                .writer(writer)
                .build();

        // @formatter:on

    }

    /**
     * Genera el tercer paso de la ejecucion del job
     *
     * @param stepBuilderFactory
     *            Instancia de {@link StepBuilderFactory} para generar el {@link Step}
     * @param reader
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Project}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Project}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Project}
     * @return El {@link Step} construido
     */
    @Bean
    @Qualifier("dateProjectsStep")
    public Step dateProjectsStep(final StepBuilderFactory stepBuilderFactory, final ItemReader<DataSetData> reader,
            final ItemWriter<InputData<DataSetData>> writer,
            final ItemProcessor<DataSetData, InputData<DataSetData>> processor) {
        // @formatter:off

        return stepBuilderFactory.get("step4").<DataSetData, InputData<DataSetData>> chunk(10)
                .reader(dateProjectsReader())
                .processor(processor)
                .writer(writer)
                .build();

        // @formatter:on

    }

    /**
     * Gets the file.
     *
     * @return {@link Resource}.
     */
    private Resource getFile(final String filePath) {
        Resource file;

        if (StringUtils.isBlank(this.dataPath)) {
            file = new ClassPathResource(filePath);
        } else {
            file = new FileSystemResource(new File(this.dataPath, filePath));
        }

        return file;
    }

}
