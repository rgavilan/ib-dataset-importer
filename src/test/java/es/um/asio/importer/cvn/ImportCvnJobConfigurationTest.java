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
import es.um.asio.importer.cnv.config.ImportCvnJobConfiguration;
import es.um.asio.importer.cnv.mapper.CvnRootBeanMapper;
import es.um.asio.importer.cnv.model.CvnChanges;
import es.um.asio.importer.cnv.model.beans.CvnRootBean;
import es.um.asio.importer.cnv.service.CvnImportInfoService;
import es.um.asio.importer.cnv.service.CvnService;
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
        ExitStatus exitStatus = jobExecution.getExitStatus();
        
        assertThat(jobInstance.getJobName()).isEqualTo(Constants.CVN_JOB_NAME);
        assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
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
