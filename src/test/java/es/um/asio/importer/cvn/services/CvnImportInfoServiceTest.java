package es.um.asio.importer.cvn.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Date;

import es.um.asio.importer.cvn.config.CvnConfiguration;
import es.um.asio.importer.cvn.exception.LastImportRequestException;
import es.um.asio.importer.cvn.service.CvnImportInfoService;
import es.um.asio.importer.cvn.service.impl.CvnImportInfoServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CvnConfiguration.class, CvnImportInfoServiceTest.CvnImportInfoServiceConfiguration.class})
public class CvnImportInfoServiceTest {        
 
    @Autowired
    public RestTemplate restTemplate;   
  
    @Autowired
    private CvnImportInfoService cvnImportInfoService;
    
    private MockRestServiceServer mockServer;
    
  
    @TestConfiguration
    static class CvnImportInfoServiceConfiguration {
        @Bean
        public CvnImportInfoService cvnImportInfoService() {
            return new CvnImportInfoServiceImpl();
        }
    }
    
    @Before
    public void setUp() {
        ReflectionTestUtils.setField(cvnImportInfoService, "endPointSearch", "http://localhost:8080/import-result/search");
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
    @Test
    public void whenFindDateOfLastImportIsCalled_thenReturnsDate() {          
        mockServer.expect(ExpectedCount.once(),
                requestTo("http://localhost:8080/import-result/search?jobType=CVN&exitStatusCode=COMPLETED&page=0&size=1&sort=startTime,desc"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(givenAnImportResultPaged(),MediaType.APPLICATION_JSON));  
        
        Date date = cvnImportInfoService.findDateOfLastImport();
        
        mockServer.verify();        
        assertNotNull(date);
    }
    
    @Test(expected = LastImportRequestException.class)
    public void whenFindDateOfLastImportReturnsServerError_thenThrowLastImportRequestException() {                   
        mockServer.expect(ExpectedCount.manyTimes(),
                requestTo("http://localhost:8080/import-result/search?jobType=CVN&exitStatusCode=COMPLETED&page=0&size=1&sort=startTime,desc"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());  
        
        cvnImportInfoService.findDateOfLastImport();
    }
    
    
    @Test(expected = LastImportRequestException.class)
    public void whenFindDateOfLastImportReturnsServerError_thenRetry3Times() {       
        mockServer.expect(ExpectedCount.times(3),
                requestTo("http://localhost:8080/import-result/search?jobType=CVN&exitStatusCode=COMPLETED&page=0&size=1&sort=startTime,desc"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());  
        
        cvnImportInfoService.findDateOfLastImport();
    }

    private String givenAnImportResultPaged() {
        return "{\r\n" + 
                "    \"content\": [\r\n" + 
                "        {\r\n" + 
                "            \"startTime\": \"2020-04-17T06:42:30.000+0000\",\r\n" + 
                "            \"exitStatusCode\": \"COMPLETED\",\r\n" + 
                "            \"jobType\": \"CVN\"\r\n" + 
                "        }\r\n" + 
                "    ],\r\n" + 
                "    \"pageable\": {\r\n" + 
                "        \"sort\": {\r\n" + 
                "            \"sorted\": false,\r\n" + 
                "            \"unsorted\": true,\r\n" + 
                "            \"empty\": true\r\n" + 
                "        },\r\n" + 
                "        \"offset\": 0,\r\n" + 
                "        \"pageSize\": 1,\r\n" + 
                "        \"pageNumber\": 0,\r\n" + 
                "        \"paged\": true,\r\n" + 
                "        \"unpaged\": false\r\n" + 
                "    },\r\n" + 
                "    \"size\": 1,\r\n" + 
                "    \"totalElements\": 2,\r\n" + 
                "    \"totalPages\": 2,\r\n" + 
                "    \"last\": false,\r\n" + 
                "    \"number\": 0,\r\n" + 
                "    \"sort\": {\r\n" + 
                "        \"sorted\": false,\r\n" + 
                "        \"unsorted\": true,\r\n" + 
                "        \"empty\": true\r\n" + 
                "    },\r\n" + 
                "    \"numberOfElements\": 1,\r\n" + 
                "    \"first\": true,\r\n" + 
                "    \"empty\": false\r\n" + 
                "}";
    }
}
