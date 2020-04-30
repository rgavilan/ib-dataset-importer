package es.um.asio.importer.cvn.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class that represents the CVNs that have changed
 */
@Getter
@Setter
@ToString(includeFieldNames = true)
public class CvnChanges{  
    
    /**
     * The cvns identifiers that have changed.
     */
    private Long[] ids;
}
