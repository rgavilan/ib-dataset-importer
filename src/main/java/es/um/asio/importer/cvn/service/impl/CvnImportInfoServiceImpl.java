package es.um.asio.importer.cvn.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.um.asio.abstractions.dto.ImportResultDto;
import es.um.asio.importer.cvn.exception.LastImportRequestException;
import es.um.asio.importer.cvn.service.CvnImportInfoService;
import es.um.asio.importer.util.RestPageImpl;


/**
 * Service that provides information about CVN imports.
 */
@Service
@ConditionalOnProperty(prefix = "app.services.input-processor.mockup", name = "enabled", havingValue = "false", matchIfMissing = true)
public class CvnImportInfoServiceImpl implements CvnImportInfoService {
   
   /** 
    * The logger. 
    */
   private static final Logger logger = LoggerFactory.getLogger(CvnImportInfoServiceImpl.class);

    /** 
     * The rest template. 
     * */
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${app.services.input-processor.endpoint-import-search}")
    private String endPointSearch;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { LastImportRequestException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000) )
    public Date findDateOfLastImport() {
        String uri = endPointSearch + "?jobType=CVN&exitStatusCode=COMPLETED&page=0&size=1&sort=startTime,desc";    
        ParameterizedTypeReference<RestPageImpl<ImportResultDto>> responseType = new ParameterizedTypeReference<RestPageImpl<ImportResultDto>>() { };

        RestPageImpl<ImportResultDto> importResultsPaged;
        
        try {
            importResultsPaged = restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody(); 
        } catch (RestClientException restClientException) {
            logger.error("Error in findDateOfLastImport");
            throw new LastImportRequestException(uri, restClientException);
        }
        
        if(importResultsPaged==null || importResultsPaged.isEmpty()) {
            return null;
        }        
        return importResultsPaged.getContent().get(0).getStartTime();
    }
}
