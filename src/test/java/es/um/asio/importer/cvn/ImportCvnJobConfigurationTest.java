package es.um.asio.importer.cvn;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.domain.importResult.ImportResult;
import es.um.asio.importer.cvn.config.ImportCvnJobConfiguration;
import es.um.asio.importer.cvn.exception.CvnChangesRequestException;
import es.um.asio.importer.cvn.exception.CvnRequestException;
import es.um.asio.importer.cvn.exception.LastImportRequestException;
import es.um.asio.importer.cvn.mapper.CvnRootBeanMapper;
import es.um.asio.importer.cvn.model.CvnChanges;
import es.um.asio.importer.cvn.model.beans.CvnItemBean;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.service.CvnImportInfoService;
import es.um.asio.importer.cvn.service.CvnService;
import es.um.asio.importer.constants.Constants;
import es.um.asio.importer.listener.JobCompletionNotificationListener;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ImportCvnJobConfigurationTest.ImportCvnJobConfigurationTestConfig.class, ImportCvnJobConfiguration.class })
@EnableBatchProcessing
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD )
public class ImportCvnJobConfigurationTest {
    
    @Configuration
    static class ImportCvnJobConfigurationTestConfig {

        @Bean
        JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
        @Bean
        JobExecutionListener jobExecutionListener() {
            return new JobCompletionNotificationListener();
        }
        @Bean
        public CvnRootBeanMapper cvnRootBeanMapper() {
            return Mappers.getMapper(CvnRootBeanMapper.class);
        }        
    }
    
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
 
    
    @MockBean
    CvnService cvnService;    
  
    @MockBean
    CvnImportInfoService cvnImportInfoService;    
 
    @MockBean
    KafkaTemplate<java.lang.String, InputData<DataSetData>> kafkaTemplate;    
  
    
    @Test
    public void whenJobExecuted_thenSuccess() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        
        JobInstance jobInstance = jobExecution.getJobInstance();
        String exitCode = jobExecution.getExitStatus().getExitCode();
        
        assertThat(jobInstance.getJobName()).isEqualTo(Constants.CVN_JOB_NAME);
        assertThat(exitCode).isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }
    
    @Test
    public void whenJobExecuted_thenSentImportResultToKafka() throws Exception {
        jobLauncherTestUtils.launchJob();
        
        verify(kafkaTemplate).send(anyString(), argThat(inputData -> inputData.getData() instanceof ImportResult));   
    }
    
    @Test
    public void whenJobExecuted_thenSentAllImportedCvnToKafka() throws Exception {
        mockCvnServices();
        
        jobLauncherTestUtils.launchJob();

        verify(kafkaTemplate, times(2)).send(anyString(), argThat(inputData -> inputData.getData() instanceof es.um.asio.domain.cvn.CvnRootBean));  
    }
    
    
    @Test
    public void whenJobExecuted_AndLastImportServiceThrowsLastImportRequestException_thenNoSuccess() throws Exception {
        LastImportRequestException lastImportRequestException = new LastImportRequestException();
        Mockito.when(this.cvnImportInfoService.findDateOfLastImport()).thenThrow(lastImportRequestException);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        
        String exitCode = jobExecution.getExitStatus().getExitCode();
        Throwable jobException = jobExecution.getAllFailureExceptions().get(0).getCause();
        
        assertThat(exitCode).isEqualTo(ExitStatus.FAILED.getExitCode());
        assertThat(jobException).isEqualTo(lastImportRequestException);
    }
    
    @Test
    public void whenJobExecuted_AndFindAllChangesByDateThrowsCvnChangesRequestException_thenNoSuccess() throws Exception {
        CvnChangesRequestException cvnChangesRequestException = new CvnChangesRequestException();
        Mockito.when(this.cvnImportInfoService.findDateOfLastImport()).thenReturn(new Date(0));
        Mockito.when(this.cvnService.findAllChangesByDate(any())).thenThrow(cvnChangesRequestException);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        
        String exitCode = jobExecution.getExitStatus().getExitCode();
        Throwable jobException = jobExecution.getAllFailureExceptions().get(0).getCause();
        
        assertThat(exitCode).isEqualTo(ExitStatus.FAILED.getExitCode());
        assertThat(jobException).isEqualTo(cvnChangesRequestException);
    }
    
    
    @Test
    public void whenJobExecuted_AndFindByIdThrowsCvnRequestException_thenSuccess() throws Exception {
        CvnRequestException cvnRequestException = new CvnRequestException();
        Mockito.when(this.cvnImportInfoService.findDateOfLastImport()).thenReturn(new Date(0));
        Mockito.when(this.cvnService.findAllChangesByDate(any())).then(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();
            cvnChanges.setIds(new Long[] {1L});
            return cvnChanges;
        });
        Mockito.when(this.cvnService.findById(any())).thenThrow(cvnRequestException);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        
        String exitCode = jobExecution.getExitStatus().getExitCode();
        
        assertThat(exitCode).isEqualTo(ExitStatus.COMPLETED.getExitCode());
    }
    
    @Test
    public void whenJobExecuted_thenSentToKafkaAllCvnReceivedFromCvnService() throws Exception {
        CvnRootBean cvnRootBean1 = new CvnRootBean();
        CvnItemBean cvnItemBean1 = new CvnItemBean();
        cvnItemBean1.setCode("1");
        cvnRootBean1.getCvnItemBean().add(cvnItemBean1);
        
        CvnRootBean cvnRootBean2 = new CvnRootBean();
        CvnItemBean cvnItemBean2 = new CvnItemBean();
        cvnItemBean2.setCode("2");
        cvnRootBean2.getCvnItemBean().add(cvnItemBean2);

        Mockito.when(this.cvnImportInfoService.findDateOfLastImport()).thenReturn(new Date(0));
        Mockito.when(this.cvnService.findAllChangesByDate(any())).then(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();
            cvnChanges.setIds(new Long[] {1L, 2L, 3L});
            return cvnChanges;
        });
        Mockito.when(this.cvnService.findById(1L)).thenReturn(cvnRootBean1);
        Mockito.when(this.cvnService.findById(2L)).thenReturn(cvnRootBean2);
        Mockito.when(this.cvnService.findById(3L)).thenThrow(new CvnRequestException());

        
        jobLauncherTestUtils.launchJob();

        
        verify(kafkaTemplate, times(2)).send(anyString(), argThat(inputData -> inputData.getData() instanceof es.um.asio.domain.cvn.CvnRootBean));  

        verify(kafkaTemplate).send(anyString(), argThat(inputData -> inputData.getData() instanceof es.um.asio.domain.cvn.CvnRootBean 
                && ((es.um.asio.domain.cvn.CvnRootBean)inputData.getData())
                    .getCvnItemBean().get(0).getCode().equals("1")));  
        
        verify(kafkaTemplate).send(anyString(), argThat(inputData -> inputData.getData() instanceof es.um.asio.domain.cvn.CvnRootBean 
                && ((es.um.asio.domain.cvn.CvnRootBean)inputData.getData())
                    .getCvnItemBean().get(0).getCode().equals("2")));
    }
    
    
    private void mockCvnServices() {
        final Date lastImport = new Date();
        Mockito.when(this.cvnImportInfoService.findDateOfLastImport()).thenReturn(lastImport);
        Mockito.when(this.cvnService.findAllChangesByDate(lastImport)).then(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();
            cvnChanges.setIds(new Long[] {1L,2L});
            return cvnChanges;
        });
        Mockito.when(this.cvnService.findById(any())).thenAnswer(invocation -> {
            final Long id = invocation.getArgument(0);
            if(id.equals(1L)) {
                return new CvnRootBean();
            }
            else if(id.equals(2L)) {
                return new CvnRootBean();
            }
            return null;
        });
    }
}
