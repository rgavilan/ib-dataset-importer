package es.um.asio.importer.cvn.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit4.SpringRunner;

import es.um.asio.importer.cvn.model.CvnChanges;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.reader.CvnReader;
import es.um.asio.importer.cvn.service.CvnImportInfoService;
import es.um.asio.importer.cvn.service.CvnService;

@RunWith(SpringRunner.class)
public class CvnReaderTest {
    
    /**
     *  The cvn reader.
    */
    @Autowired
    CvnReader cvnReader;
    
    @MockBean
    CvnService mockCvnService;
    
    @MockBean
    CvnImportInfoService mockCvnImportInfoService;    
  
    
    @TestConfiguration
    static class CvnReaderConfiguration {
        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        public CvnReader cvnReader() {
            return new CvnReader();
        }
    }
    
    @Test
    public void whenReadIsCalled_thenFindAllChangesByDateIsCalledOnce() {        
        cvnReader.read();
        
        verify(mockCvnService, times(1)).findAllChangesByDate(Mockito.any());
    }
    
    @Test
    public void whenReadIsCalledMultipleTimes_thenFindAllChangesByDateIsCalledOnce() {        
        cvnReader.read();
        cvnReader.read();
        cvnReader.read();
        
        verify(mockCvnService, times(1)).findAllChangesByDate(Mockito.any());
    }
    
    @Test
    public void whenFindAllChangesByDateReturnsNull_thenFindByIdIsNotCalled() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenReturn(null);
        cvnReader.read();
        
        verify(mockCvnService, never()).findById(Mockito.any());
    }
    
    @Test
    public void whenFindAllChangesByDateReturnsNullIds_thenFindByIdIsNotCalled() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenAnswer(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();
            cvnChanges.setIds(null);
            return cvnChanges;
        });
        
        cvnReader.read();
        
        verify(mockCvnService, never()).findById(Mockito.any());
    }
    
    @Test
    public void whenFindAllChangesByDateReturnsEmptyIds_thenFindByIdIsNotCalled() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenAnswer(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();
            cvnChanges.setIds(new Long[0]);
            return cvnChanges;
        });
        
        cvnReader.read();
        
        verify(mockCvnService, never()).findById(Mockito.any());
    }
    
    @Test
    public void whenFindAllChangesByDateReturnsAnId_thenFindByIdIsCalledWithId() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenAnswer(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();            
            cvnChanges.setIds(new Long[] {5L});
            return cvnChanges;
        });
        
        cvnReader.read();
        
        verify(mockCvnService, times(1)).findById(Mockito.any());
        verify(mockCvnService, times(1)).findById(5L);
    }
    
    @Test
    public void whenReadIsCalledMultipleTimes_thenFindByIdIsCalledOnlyForEachId() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenAnswer(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();            
            cvnChanges.setIds(new Long[] {1L, 2L, 3L});
            return cvnChanges;
        });
        
        cvnReader.read();
        cvnReader.read();
        cvnReader.read();
        cvnReader.read();
        cvnReader.read();
        
        verify(mockCvnService, times(3)).findById(Mockito.any());
        verify(mockCvnService, times(1)).findById(1L);
        verify(mockCvnService, times(1)).findById(2L);
        verify(mockCvnService, times(1)).findById(3L);
    }
    
    @Test
    public void whenReadIsCalled_thenReturnsACvn() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenAnswer(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();            
            cvnChanges.setIds(new Long[] {1L});
            return cvnChanges;
        });
        Mockito.when(mockCvnService.findById(1L)).thenReturn(new CvnRootBean());
        
        CvnRootBean cvn = cvnReader.read();
        
        assertNotNull(cvn);
    }
    
    @Test
    public void whenReadIsCalled_thenReturnsACvnForEachIdAndNullOnNextCall() {   
        Mockito.when(mockCvnService.findAllChangesByDate(Mockito.any())).thenAnswer(invocation -> {
            CvnChanges cvnChanges = new CvnChanges();            
            cvnChanges.setIds(new Long[] {1L, 2L});
            return cvnChanges;
        });
        Mockito.when(mockCvnService.findById(Mockito.any())).thenReturn(new CvnRootBean());
        
        CvnRootBean cvn1 = cvnReader.read();
        CvnRootBean cvn2 = cvnReader.read();
        CvnRootBean cvn3 = cvnReader.read();
        
        assertNotNull(cvn1);
        assertNotNull(cvn2);
        assertNull(cvn3);
    }
    
    @Test
    public void whenFindDateOfLastImportReturnsNull_thenFindAllChangesByDateIsCalledWithNullDate() {   
        Mockito.when(mockCvnImportInfoService.findDateOfLastImport()).thenReturn(null);
        
        cvnReader.read();
        
        verify(mockCvnService).findAllChangesByDate(null);
    }
    
    @Test
    public void whenFindDateOfLastImportReturnsADate_thenFindAllChangesByDateIsCalledWithThisDate() {   
        Date lastImportDate = new Date();
        Mockito.when(mockCvnImportInfoService.findDateOfLastImport()).thenReturn(lastImportDate);
        
        cvnReader.read();
        
        verify(mockCvnService).findAllChangesByDate(lastImportDate);
    }
    
}
