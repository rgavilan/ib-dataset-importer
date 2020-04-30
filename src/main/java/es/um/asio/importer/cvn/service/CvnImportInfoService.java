package es.um.asio.importer.cvn.service;

import java.util.Date;

/**
 * Service interface that provides information about CVN imports.
 */
public interface CvnImportInfoService {
    
    /**
     * Find the date of last CVN import.
     *
     * @return the date
     */
    Date findDateOfLastImport();
}
