package es.um.asio.importer.cnv.reader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import es.um.asio.domain.cvn.Cvn;
import es.um.asio.importer.cnv.model.CvnChanges;
import es.um.asio.importer.cnv.service.CvnService;

/**
 *  Gets the {@link ItemReader} implementation that retrieves the CVN-XML files and transform to {@link Cvn}
 */
public class CvnReader implements ItemReader<Cvn> {
    
    private Queue<Long> cvnsIdsChanges;
   
    /** 
     * The cvn service.
     */
    @Autowired
    private CvnService cvnService;
    
    public CvnReader() {}
    public CvnReader(CvnService cvnService) {
        this.cvnService = cvnService;
    }

    /**
     *  Retrieves the CVN-XML files and transform to {@link Cvn}
     *
     * @return the Cvn
     */
    @Override
    public Cvn read()  {       
        if(cvnChangesIsNotInitialized()) {
            fetchCvnChanges();
        }
        
        return findNextCvn();
    }
    
    /**
     * Cvn changes is not initialized.
     *
     * @return true, if successful
     */
    private boolean cvnChangesIsNotInitialized() {
        return cvnsIdsChanges == null;
    }
    
    /**
     * Fetch cvn changes.
     */
    private void fetchCvnChanges(){
        cvnsIdsChanges = new LinkedList<>();
        CvnChanges cvnChanges = cvnService.findAllChangesByDate(null);
        if(cvnChanges!=null && cvnChanges.getIds()!=null && cvnChanges.getIds().length > 0) {
            cvnsIdsChanges.addAll(Arrays.asList(cvnChanges.getIds()));
        }
    }
    
    /**
     * Find next cvn.
     *
     * @return the cvn
     */
    private Cvn findNextCvn() {
        Cvn cvn = null;
        Long cvnId = cvnsIdsChanges.poll();
        if(cvnId != null) {
            cvn = cvnService.findById(cvnId);
        }     
        return cvn;
    }

}
