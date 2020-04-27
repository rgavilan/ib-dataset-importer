package es.um.asio.importer.cvn.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.um.asio.importer.cvn.exception.GenericRequestException;
import es.um.asio.importer.cvn.model.CvnChanges;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.service.CvnService;

/**
 * {@inheritDoc}
 */
@Service
@ConditionalOnProperty(prefix = "app.services.cvn.mockup", name = "enabled", havingValue = "false", matchIfMissing = true)
public class CvnServiceImpl implements CvnService {

    /** 
     * The rest template. 
     * */
    @Autowired
    @Qualifier("cvnRestTemplate")
    private RestTemplate restTemplate;
    
    @Value("${app.services.cvn.endpoint-changes}")
    private String endPointChanges;
 
    @Value("${app.services.input-processor.endpoint}")
    private String endPointCvn;
  
    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { GenericRequestException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000) )
    public CvnChanges findAllChangesByDate(Date from) {      
        String uri = endPointChanges;
        if(from != null) {
            String dateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(from);
            uri = uri + "?date=" + dateFormatted;
        }       
        HttpEntity<CvnChanges> entity = new HttpEntity<>(getHeaders());        
        return restTemplate.exchange(uri, HttpMethod.GET, entity, CvnChanges.class).getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Retryable(value = { GenericRequestException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000) )
    public CvnRootBean findById(Long id) {
        String uri = endPointCvn + "?id=" + id.toString();
        HttpEntity<CvnRootBean> entity = new HttpEntity<>(getHeaders());        
        return restTemplate.exchange(uri, HttpMethod.GET, entity, CvnRootBean.class).getBody();
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