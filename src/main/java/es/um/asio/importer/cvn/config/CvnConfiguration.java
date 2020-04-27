package es.um.asio.importer.cvn.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import es.um.asio.importer.cvn.handler.CvnResponseErrorHandler;

@Configuration
@EnableRetry
public class CvnConfiguration {
    
    @Bean
    @Qualifier("cvnRestTemplate")
    public RestTemplate cvnRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();   
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new CvnResponseErrorHandler());
        return restTemplate;
    }
    
    @Bean
    @Qualifier("importRestTemplate")
    public RestTemplate importRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();   
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
