package es.um.asio.importer.marshaller;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

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
        conversionService.addConverter((String text) -> Float.valueOf(text.replace(',', '.')));
        return conversionService;
    }
}
    
    