package es.um.asio.importer.cvn.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import es.um.asio.importer.cnv.model.CvnChanges;
import es.um.asio.importer.cnv.model.beans.CvnRootBean;
import es.um.asio.importer.cnv.reader.CvnReader;
import es.um.asio.importer.cnv.service.CvnService;

public class CvnReaderTest {
    
    CvnReader cvnReader;
    CvnService mockCvnService;
    
    @Before
    public void setUp() {
        mockCvnService = mock(CvnService.class);
        cvnReader = new CvnReader(mockCvnService);
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
    
}
