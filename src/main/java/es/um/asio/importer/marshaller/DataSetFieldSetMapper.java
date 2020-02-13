package es.um.asio.importer.marshaller;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;

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
    }
}
