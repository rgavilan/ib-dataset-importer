package es.um.asio.importer.cnv.service.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.um.asio.domain.cvn.CvnRootBean;
import es.um.asio.importer.cnv.model.CvnChanges;
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
    
    @Value("${app.services.cvn-service}")
    private String baseUrl;
    
    public CvnServiceImpl() {}
    public CvnServiceImpl(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }
  
    /**
     * {@inheritDoc}
     */
    @Override
    public CvnChanges findAllChangesByDate(Date from) {      
        String uri = baseUrl + "/changes";
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
    public CvnRootBean findById(Long id) {
        String uri = baseUrl + "/cvn?id=" + id.toString();
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