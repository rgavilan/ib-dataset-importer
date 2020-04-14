package es.um.asio.importer.dataset.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.importer.dataset.processor.DataItemProcessor;
import es.um.asio.importer.dataset.writer.DataItemWriter;
import es.um.asio.importer.marshaller.DataConverter;
import es.um.asio.importer.marshaller.DataSetFieldSetMapper;
import es.um.asio.importer.marshaller.DataSetMarshaller;

/**
 * Base class to generate a {@link Flow} that converts and send XML to Kafka topic
 */
public abstract class ImportDataSetFlowConfigurationBase {
   
    @Value("${app.data.path}")
    private String dataPath;    

    /**
     *  The step builder factory. 
     *  */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    /**
     * Generates the import {@link Flow}
     *
     * @return
     */
    public abstract Flow getFlow();
    
  
    /**
     * Gets the {@link Flow} name 
     *
     * @return the flow name
     */
    protected abstract String getFlowName();
 
   
    /**
     * Creates an instance of {@link Step} for {@link type}
     *
     * @param <T> the generic type
     * @param type the type
     * @param filePath the XML file path
     * @return the step
     */
    protected <T extends DataSetData> Step createStep(Class<T> type, String filePath) {
        return this.stepBuilderFactory.get(getFlowName().concat("-").concat(type.getSimpleName()).concat("Step"))
                .<DataSetData, InputData<DataSetData>> chunk(1000)
                .reader(baseReader(type, filePath))
                .processor(getProcessor())
                .writer(getWriter())
                .build();
    }    
   
    /**
     * Gets an instance of {@link DataItemProcessor}
     *
     * @return the processor
     */
    @Bean
    protected ItemProcessor<DataSetData, InputData<DataSetData>> getProcessor() {
        return new DataItemProcessor();
    }    
    
    /**
     * Gets an instance of {@link DataItemWriter}
     *
     * @return the processor
     */
    @Bean
    protected ItemWriter<InputData<DataSetData>> getWriter() {
        return new DataItemWriter();
    }
  
    /**
     * Gets the {@link ItemReader} implementation that reads the XML file and transform to {@link type}
     *
     * @param <T> the generic type
     * @param type the type
     * @param filePath the file path
     * @return the item reader
     */
    protected <T extends DataSetData> ItemReader<DataSetData> baseReader(Class<T> type, String filePath) {        
        final Map<String, String> propertiesBinding = new HashMap<>();
        for (Field field : type.getDeclaredFields()) {
            String fieldName = field.getName();
            propertiesBinding.put(fieldName, fieldName);
        }        
        propertiesBinding.put(DataSetMarshaller.ACCION_ENTIDAD, "operation");   

        final DataConverter<T> converter = new DataConverter<>();
        converter.setFieldSetMapper(new DataSetFieldSetMapper<T>(type));
        converter.setPropertiesBinding(propertiesBinding);

        final DataSetMarshaller<T> ummarshaller = new DataSetMarshaller<>(type);
        ummarshaller.setConverters(converter);

        final StaxEventItemReader<DataSetData> reader = new StaxEventItemReader<>();
        reader.setResource(this.getFile(filePath));
        reader.setUnmarshaller(ummarshaller);
        reader.setFragmentRootElementName(DataSetMarshaller.DATA_RECORD);

        return reader;
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
