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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Calendar;

import es.um.asio.importer.cnv.model.CvnChanges;
import es.um.asio.importer.cnv.model.beans.CvnRootBean;
import es.um.asio.importer.cnv.config.CvnConfiguration;
import es.um.asio.importer.cnv.service.CvnService;
import es.um.asio.importer.cnv.service.impl.CvnServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CvnConfiguration.class, CvnServiceTest.CvnServiceTestConfiguration.class})
public class CvnServiceTest {        
    
    @Autowired
    private CvnService cvnService;
    
    @Autowired
    private RestTemplate restTemplate;   
  
    private MockRestServiceServer mockServer;
    
    @TestConfiguration
    static class CvnServiceTestConfiguration {
        @Bean
        public CvnService cvnService() {
            return new CvnServiceImpl();
        }
    }
    
    @Before
    public void setUp() {
        ReflectionTestUtils.setField(cvnService, "endPointChanges", "http://curriculumpruebas.um.es/curriculum/rest/v1/auth/changes");
        ReflectionTestUtils.setField(cvnService, "endPointCvn", "http://curriculumpruebas.um.es/curriculum/rest/v1/auth/cvn");
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
    @Test
    public void whenfindAllChangesByDateIsCalled_thenReturnsCvnChanges() {
        mockServer.expect(ExpectedCount.once(),
                requestTo("http://curriculumpruebas.um.es/curriculum/rest/v1/auth/changes?date=2020-03-25"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("application",any(String.class))).andExpect(header("key",any(String.class)))
                .andRespond(withSuccess(givenAJsonWithListOfIds(),MediaType.APPLICATION_JSON));              
        
        CvnChanges cvnChanges = cvnService.findAllChangesByDate(new Calendar.Builder().setDate(2020, 03 -1 , 25).build().getTime());
       
        mockServer.verify();
        assertNotNull(cvnChanges);
        assertThat(cvnChanges.getIds()).isNotEmpty();
    }
    
    @Test
    public void whenfindAllChangesByDateWithNullDateIsCalled_thenReturnsCvnChanges() {
        mockServer.expect(ExpectedCount.once(),
                requestTo("http://curriculumpruebas.um.es/curriculum/rest/v1/auth/changes"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("application",any(String.class))).andExpect(header("key",any(String.class)))
                .andRespond(withSuccess(givenAJsonWithListOfIds(),MediaType.APPLICATION_JSON));              
        
        CvnChanges cvnChanges = cvnService.findAllChangesByDate(null);
        
        mockServer.verify();
        assertNotNull(cvnChanges);
        assertThat(cvnChanges.getIds()).isNotEmpty();
    }
    
    @Test
    public void whenFindByIdIsCalled_thenReturnsCvn() {          
        mockServer.expect(ExpectedCount.once(),
                requestTo("http://curriculumpruebas.um.es/curriculum/rest/v1/auth/cvn?id=1"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("application",any(String.class))).andExpect(header("key",any(String.class)))
                .andRespond(withSuccess(givenACvnXmlWithOneCvnItemBean(),MediaType.APPLICATION_XML));  
        
        CvnRootBean cvn = cvnService.findById(1L);
        
        mockServer.verify();        
        assertNotNull(cvn);
        assertThat(cvn.getCvnItemBean().size()).isEqualTo(1);
    }
    
    
    private String givenACvnXmlWithOneCvnItemBean() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>\r\n" + 
                "<CvnRootBean\r\n" + 
                "    xmlns=\"http://codes.cvn.fecyt.es/beans\">" +
                " <CvnItemBean>\r\n" + 
                "        <Code>000.020.000.000</Code>\r\n" + 
                "        <CvnDateDayMonthYear>\r\n" + 
                "            <Code>000.020.000.020</Code>\r\n" + 
                "            <Value>2020-03-12T00:00:00.000+01:00</Value>\r\n" + 
                "        </CvnDateDayMonthYear>\r\n" + 
                "        <CvnString>\r\n" + 
                "            <Code>000.020.000.070</Code>\r\n" + 
                "            <Value>spa</Value>\r\n" + 
                "        </CvnString>\r\n" + 
                "        <CvnString>\r\n" + 
                "            <Code>000.020.000.080</Code>\r\n" + 
                "            <Value>1.3.0</Value>\r\n" + 
                "        </CvnString>\r\n" + 
                "    </CvnItemBean>\r\n" + 
                "</CvnRootBean>";
    }
    
    private String givenAJsonWithListOfIds() {
        return "{\r\n" + 
                "   \"ids\":[\r\n" + 
                "      1,\r\n" + 
                "      2,\r\n" + 
                "      3,\r\n" + 
                "      4,\r\n" + 
                "      5,\r\n" + 
                "      6\r\n" + 
                "   ]\r\n" + 
                "}";
    }

}
