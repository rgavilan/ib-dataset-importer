package es.um.asio.importer.cvn.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.um.asio.importer.cvn.exception.CvnChangesRequestException;
import es.um.asio.importer.cvn.exception.CvnRequestException;
import es.um.asio.importer.cvn.model.CvnChanges;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.reponseextractor.CvnResponseExtractor;
import es.um.asio.importer.cvn.service.CvnService;

/**
 * {@inheritDoc}
 */
@Service
@ConditionalOnProperty(prefix = "app.services.cvn.mockup", name = "enabled", havingValue = "false", matchIfMissing = true)
public class CvnServiceImpl implements CvnService {
    
    /** 
     * The logger. 
     */
    private static final Logger logger = LoggerFactory.getLogger(CvnServiceImpl.class);

    /** 
     * The rest template. 
     * */
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${app.services.cvn.endpoint-changes}")
    private String endPointChanges;
 
    @Value("${app.services.cvn.endpoint-cvn}")
    private String endPointCvn;
  
    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { CvnChangesRequestException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000) )
    public CvnChanges findAllChangesByDate(Date from) {      
        String uri = endPointChanges;
        if(from != null) {
            String dateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(from);
            uri = uri + "?date=" + dateFormatted;
        }       
        HttpEntity<CvnChanges> entity = new HttpEntity<>(getHeaders());

        try {
            return restTemplate.exchange(uri, HttpMethod.GET, entity, CvnChanges.class).getBody();
        } catch (RestClientException restClientException) {
            logger.error("Error in cvn-findAllChangesByDate request {}.", uri);
            throw new CvnChangesRequestException(uri, restClientException);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { CvnRequestException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000) )
    public CvnRootBean findById(Long id) {
        String uri = endPointCvn + "?id=" + id.toString();
        ResponseExtractor<CvnRootBean> cvnResponseExtractor =  new CvnResponseExtractor(restTemplate.getMessageConverters());
        
        try {
            return restTemplate.execute(uri, HttpMethod.GET, clientHttpRequest -> clientHttpRequest.getHeaders().addAll(getHeaders()), cvnResponseExtractor);
        } catch (RestClientException restClientException) {
            logger.error("Error in cvn request {}.", uri);
            throw new CvnRequestException(uri, restClientException);
        }
    }
    
    /**
     * Gets the headers from cvn service.
     *
     * @return the headers
     */
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("application", "asio");   
        headers.set("key", "asiokey");      
        return headers;
    }
    
 }