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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.domain.investigationCenter.DatosContactoGrupo;
import es.um.asio.importer.dataset.processor.DataItemProcessor;
import es.um.asio.importer.dataset.writer.DataItemWriter;
import es.um.asio.importer.marshaller.DataConverter;
import es.um.asio.importer.marshaller.DataSetFieldSetMapper;
import es.um.asio.importer.marshaller.DataSetMarshaller;

/**
 * Job que procesa ficheros XML y los manda a la cola Kafka
 */
@Configuration
public class ImportInvestigationCentersDataSetJobConfiguration {

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
    public ItemReader<DataSetData> groupContactDatasReader() {

        final Class<DatosContactoGrupo> targetClass = DatosContactoGrupo.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDGRUPOINVESTIGACION", "idGrupoInvestigacion");
        propertiesBinding.put("NUMERO", "numero");
        propertiesBinding.put("CODTIPOFORMACONTACTO", "codTipoFormaContacto");
        propertiesBinding.put("VALOR", "valor");

        final DataConverter<DatosContactoGrupo> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<DatosContactoGrupo>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<DatosContactoGrupo> ummarshaller = new DataSetMarshaller<>(targetClass);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile("dataset/Grupos de investigación/Datos contacto grupos.xml"));
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
    public ItemProcessor<DataSetData, InputData<DataSetData>> investigationCentersProcessor() {
        return new DataItemProcessor();
    }

    /**
     * Devuelve una instancia de {@link DataItemWriter}
     *
     * @return instancia de {@link DataItemWriter}
     */
    @Bean
    public ItemWriter<InputData<DataSetData>> investigationCentersWriter() {
        return new DataItemWriter();
    }

    /**
     * Genera el primer paso de la importación de grupos de investigación del job
     *
     * @param stepBuilderFactory
     *            Instancia de {@link StepBuilderFactory} para generar el {@link Step}
     * @param reader
     *            El {@link ItemReader} anteriormente configurado para la clase {@link DatosContactoGrupo}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link DatosContactoGrupo}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link DatosContactoGrupo}
     * @return El {@link Step} construido
     */
    @Bean
    @Qualifier("groupContactDatasStep")
    public Step groupContactDatasStep(final StepBuilderFactory stepBuilderFactory, final ItemReader<DataSetData> reader,
            final ItemWriter<InputData<DataSetData>> writer,
            final ItemProcessor<DataSetData, InputData<DataSetData>> processor) {
        // @formatter:off

        return stepBuilderFactory.get("step6").<DataSetData, InputData<DataSetData>> chunk(10)
                .reader(groupContactDatasReader())
                .processor(investigationCentersProcessor())
                .writer(investigationCentersWriter())
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
