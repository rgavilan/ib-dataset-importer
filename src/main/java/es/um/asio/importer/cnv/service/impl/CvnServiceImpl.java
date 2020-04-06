package es.um.asio.importer.cnv.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.um.asio.domain.cvn.Cvn;
import es.um.asio.domain.cvn.CvnChanges;
import es.um.asio.importer.cnv.service.CvnService;

/**
 * {@inheritDoc}
 */
@Service
public class CvnServiceImpl implements CvnService {

    /** 
     * The rest template. 
     * */
    @Autowired
    private RestTemplate restTemplate;
    
    public CvnServiceImpl() {}
    public CvnServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    public CvnChanges findAllChangesByDate(Date from) {      
        String uri = "http://curriculumpruebas.um.es/curriculum/rest/v1/auth/changes";
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
    public Cvn findById(Long id) {
        String uri = "http://curriculumpruebas.um.es/curriculum/rest/v1/auth/cvn?id=" + id.toString();
        HttpEntity<Cvn> entity = new HttpEntity<>(getHeaders());        
        return restTemplate.exchange(uri, HttpMethod.GET, entity, Cvn.class).getBody();
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