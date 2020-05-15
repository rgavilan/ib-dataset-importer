package es.um.asio.importer.marshaller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * Extension of {@link XStreamMarshaller} for data set importing.
 *
 * @param <T>
 */
public class DataSetMarshaller<T> extends XStreamMarshaller {

    /**
     * Constructor. Add DATA_RECORD - Generic type as default aliases
     *
     * @param targetClass
     *            Target class.
     */
    public DataSetMarshaller(final Class<T> targetClass) {
        super();

        final Map<String, Class<T>> aliases = new HashMap<>();
        aliases.put(DATA_RECORD, targetClass);
        this.setAliases(aliases);
    }

    /**
     * Data record tag.
     */
    public static final String DATA_RECORD = "DATA_RECORD";
    
    /** 
     * The entity operation tag. 
     * */
    public static final String ACCION_ENTIDAD = "ACCIONENTIDAD";
    
    /** 
     * Add operation tag. 
     * */
    public static final String ACCION_ENTIDAD_ADD = "ADD";
    
    /** 
     * Modify operation tag. 
     * */
    public static final String ACCION_ENTIDAD_MODIFY = "MODIFY";
    
    /** 
     * Delete operation tag. 
     * */
    public static final String ACCION_ENTIDAD_DELETE = "DELETE";    
    
}
