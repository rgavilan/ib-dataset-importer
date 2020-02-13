package es.um.asio.importer.marshaller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.validation.BindException;

import com.google.common.collect.Iterables;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import lombok.Setter;

/**
 * XStream {@link Converter} implementation.
 * 
 * @param <T>
 */
public class DataConverter<T> implements Converter {

    /**
     * Field set mapper.
     */
    @Setter
    private FieldSetMapper<T> fieldSetMapper;

    /**
     * Field set factory.
     */
    @Setter
    private FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();

    /**
     * Properties binding.
     */
    @Setter
    private Map<String, String> propertiesBinding = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") final Class type) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {

        final Map<String, String> values = new HashMap<>();

        String nodeName;

        while (reader.hasMoreChildren()) {
            reader.moveDown(); // Go to next children
            nodeName = reader.getNodeName();
            values.put(this.propertiesBinding.containsKey(nodeName) ? this.propertiesBinding.get(nodeName) : nodeName,
                    reader.getValue());
            reader.moveUp(); // Go back to parent
        }

        try {
            return this.fieldSetMapper
                    .mapFieldSet(this.fieldSetFactory.create(Iterables.toArray(values.values(), String.class),
                            Iterables.toArray(values.keySet(), String.class)));
        } catch (final BindException e) {
            throw new UnmarshallingFailureException("Error unmarshalling XML", e);
        }
    }

}
