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
import es.um.asio.domain.investigationGroup.InvestigationGroup;
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
public class ImportInvestigationGroupsDataSetJobConfiguration {

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
    public ItemReader<DataSetData> investigationGroupsReader() {

        final Class<InvestigationGroup> targetClass = InvestigationGroup.class;

        final Map<String, String> propertiesBinding = new HashMap<>();
        propertiesBinding.put("IDGRUPOINVESTIGACION", "investigationGroupId");
        propertiesBinding.put("DESCRIPCION", "description");
        propertiesBinding.put("CODUNIDADADM", "administrationUnitCode");
        propertiesBinding.put("EXCELENCIA", "excellency");
        propertiesBinding.put("FECHACREACION", "creationDate");
        propertiesBinding.put("FECHADESAPARICION", "extinctionDate");

        final DataConverter<InvestigationGroup> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<InvestigationGroup>(targetClass));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<InvestigationGroup> ummarshaller = new DataSetMarshaller<>(targetClass);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile("dataset/Grupos de investigación/Grupos de investigacion.xml"));
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
    public ItemProcessor<DataSetData, InputData<DataSetData>> investigationGroupsProcessor() {
        return new DataItemProcessor();
    }

    /**
     * Devuelve una instancia de {@link DataItemWriter}
     *
     * @return instancia de {@link DataItemWriter}
     */
    @Bean
    public ItemWriter<InputData<DataSetData>> investigationGroupsWriter() {
        return new DataItemWriter();
    }

    /**
     * Genera el primer paso de la importación de grupos de investigación del job
     *
     * @param stepBuilderFactory
     *            Instancia de {@link StepBuilderFactory} para generar el {@link Step}
     * @param reader
     *            El {@link ItemReader} anteriormente configurado para la clase {@link InvestigationGroup}
     * @param writer
     *            El {@link ItemWriter} anteriormente configurado para la clase {@link InvestigationGroup}
     * @param processor
     *            El {@link ItemProcessor} anteriormente configurado para la clase {@link InvestigationGroup}
     * @return El {@link Step} construido
     */
    @Bean
    @Qualifier("investigationGroupsStep")
    public Step investigationGroupsStep(final StepBuilderFactory stepBuilderFactory,
            final ItemReader<DataSetData> reader, final ItemWriter<InputData<DataSetData>> writer,
            final ItemProcessor<DataSetData, InputData<DataSetData>> processor) {
        // @formatter:off

        return stepBuilderFactory.get("step5").<DataSetData, InputData<DataSetData>> chunk(10)
                .reader(investigationGroupsReader())
                .processor(investigationGroupsProcessor())
                .writer(investigationGroupsWriter())
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
