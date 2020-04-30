package es.um.asio.importer.cvn.service.mockup;

import java.util.Date;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import es.um.asio.importer.cvn.service.CvnImportInfoService;

/**
 * Fake CvnImportInfoService implementation
 */
@Service
@ConditionalOnProperty(prefix = "app.services.input-processor.mockup", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CvnImportInfoServiceMockupImpl implements CvnImportInfoService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Date findDateOfLastImport() {        
        return null;
    }

}
