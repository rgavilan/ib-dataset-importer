package es.um.asio.importer.cvn.reader;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import es.um.asio.importer.cvn.model.CvnChanges;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.service.CvnImportInfoService;
import es.um.asio.importer.cvn.service.CvnService;

/**
 *  Gets the {@link ItemReader} implementation that retrieves the CVN-XML files and transform to {@link CvnRootBean}
 */
public class CvnReader implements ItemReader<CvnRootBean> {
    
    private Queue<Long> cvnsIdsChanges;
   
    /** 
     * The cvn service.
     */
    @Autowired
    private CvnService cvnService;
    
    /** 
     * The cvn import info service.
     */
    @Autowired
    private CvnImportInfoService cvnImportInfoService;
    

    /**
     *  Retrieves the CVN-XML files and transform to {@link Cvn}
     *
     * @return the Cvn
     */
    @Override
    public CvnRootBean read()  {
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
        Date dateFrom = cvnImportInfoService.findDateOfLastImport();
        cvnsIdsChanges = new LinkedList<>();
        CvnChanges cvnChanges = cvnService.findAllChangesByDate(dateFrom);
        if(cvnChanges!=null && cvnChanges.getIds()!=null && cvnChanges.getIds().length > 0) {
            cvnsIdsChanges.addAll(Arrays.asList(cvnChanges.getIds()));
        }
    }
    
    /**
     * Find next cvn.
     *
     * @return the cvn
     */
    private CvnRootBean findNextCvn() {
        CvnRootBean cvn = null;
        Long cvnId = cvnsIdsChanges.poll();
        if(cvnId != null) {
            cvn = cvnService.findById(cvnId);
        }     
        return cvn;
    }

}
