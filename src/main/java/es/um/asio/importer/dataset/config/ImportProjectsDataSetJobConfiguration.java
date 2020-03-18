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
import es.um.asio.domain.project.FechaProyecto;
import es.um.asio.domain.project.JustificacionPrevistaProyecto;
import es.um.asio.domain.project.Proyecto;
import es.um.asio.domain.project.OrigenProyecto;
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

        final Class<Proyecto> targetClass = Proyecto.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDPROYECTO", "idProyecto");
        propertiesBinding.put("NOMBRE", "nombre");
        propertiesBinding.put("PROYECTOFINALISTA", "proyectoFinalista");
        propertiesBinding.put("LIMITATIVO", "limitativo");

        final DataConverter<Proyecto> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<Proyecto>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<Proyecto> ummarshaller = new DataSetMarshaller<>(targetClass);
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

        final Class<JustificacionPrevistaProyecto> targetClass = JustificacionPrevistaProyecto.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDPROYECTO", "idProyecto");
        propertiesBinding.put("NUMEROJUSTIFICACIONPREVISTA", "numeroJustificacionPrevista");
        propertiesBinding.put("CODTIPOORIGEN", "codTipoOrigen");
        propertiesBinding.put("IDORIGENPROYECTO", "idOrigenProyecto");
        propertiesBinding.put("TIPOJUSTIFICACION", "tipoJustificacion");
        propertiesBinding.put("CLASEJUSTIFICACION", "claseJustificacion");
        propertiesBinding.put("FECHAINICIODEVENGO", "fechaInicioDevengo");
        propertiesBinding.put("FECHAFINDEVENGO", "fechaFinDevengo");
        propertiesBinding.put("FECHAINICIOCONTABILIZACION", "fechaInicioContabilizacion");
        propertiesBinding.put("FECHAINICIOPAGO", "fechaInicioPago");
        propertiesBinding.put("FECHAFINPAGO", "fechaFinPago");
        propertiesBinding.put("FECHAPREVISTA", "fechaPrevista");
        propertiesBinding.put("OBSERVACIONES", "observaciones");

        final DataConverter<JustificacionPrevistaProyecto> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<JustificacionPrevistaProyecto>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<JustificacionPrevistaProyecto> ummarshaller = new DataSetMarshaller<>(targetClass);
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

        final Class<OrigenProyecto> targetClass = OrigenProyecto.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("CODTIPOORIGEN", "codTipoOrigen");
        propertiesBinding.put("IDORIGENPROYECTO", "idOrigenProyecto");
        propertiesBinding.put("DESCRIPCION", "descripcion");

        final DataConverter<OrigenProyecto> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<OrigenProyecto>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<OrigenProyecto> ummarshaller = new DataSetMarshaller<>(targetClass);
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

        final Class<FechaProyecto> targetClass = FechaProyecto.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDPROYECTO", "idProyecto");
        propertiesBinding.put("NUMERO", "numero");
        propertiesBinding.put("FECHAINICIOEXPEDIENTE", "fechaInicioExpediente");
        propertiesBinding.put("FECHAINICIOPROYECTO", "fechaInicioProyecto");
        propertiesBinding.put("FECHAFINPROYECTO", "fechaFinProyecto");
        propertiesBinding.put("CODTIPOMOTIVOCAMBIOFECHA", "codTipoMotivoCambioFecha");
        propertiesBinding.put("MOTIVOCAMBIOFECHA", "motivoCambioFecha");

        final DataConverter<FechaProyecto> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<FechaProyecto>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<FechaProyecto> ummarshaller = new DataSetMarshaller<>(targetClass);
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
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Proyecto}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Proyecto}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Proyecto}
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
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Proyecto}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Proyecto}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Proyecto}
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
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Proyecto}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Proyecto}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Proyecto}
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
     *            El {@link ItemReader} anteriormente configurado para la clase {@link Proyecto}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link Proyecto}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link Proyecto}
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
