package es.um.asio.importer.marshaller;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import es.um.asio.abstractions.domain.Operation;

/**
 * Extension of {@link BeanWrapperFieldSetMapper}. Sets generic type as target type automatically.
 *
 * @param <T>
 */
public class DataSetFieldSetMapper<T> extends BeanWrapperFieldSetMapper<T> {

    /**
     * Constructor.
     *
     * @param targetClass
     *            Target class.
     */
    public DataSetFieldSetMapper(final Class<T> targetClass) {
        super();
        this.setTargetType(targetClass);        
        this.setConversionService(createConversionService());
    }
    
    /**
     * Creates the conversion service.
     *
     * @return the conversion service
     */
    private ConversionService createConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);
        
        conversionService.addConverter(new Converter<String, Float>() {
            @Override
            public Float convert(String text) {               
                return Float.valueOf(text.replace(',', '.'));
            }
        });
        
        
        DefaultConversionService.addDefaultConverters(conversionService);
        conversionService.addConverter(new Converter<String, Operation>() {
            @Override
            public Operation convert(String text) {      
                switch (text) {
                    case DataSetMarshaller.ACCION_ENTIDAD_ADD:
                        return Operation.INSERT;
                    case DataSetMarshaller.ACCION_ENTIDAD_MODIFY:
                        return Operation.UPDATE;
                    case DataSetMarshaller.ACCION_ENTIDAD_DELETE:
                        return Operation.DELETE;
                    default:
                        return Operation.INSERT;
                }
            }
        });
        return conversionService;
    }
}
    
    