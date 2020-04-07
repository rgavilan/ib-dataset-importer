package es.um.asio.importer.cnv.service;

import java.util.Date;
import es.um.asio.importer.cnv.model.CvnChanges;
import es.um.asio.importer.cnv.model.beans.CvnRootBean;

/**
 * The Interface CvnService.
 */
public interface CvnService {
       
   
    /**
     * Find all cvn changes by date.
     *
     * @param from the from
     * @return the cvn changes
     */
    CvnChanges findAllChangesByDate(Date from);
    
    /**
     * Find cvn by id.
     *
     * @param id the id
     * @return the cvn
     */
    CvnRootBean findById(Long id);
}
