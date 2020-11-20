package es.um.asio.importer.cvn.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;

/**
 * The listener interface for receiving CVN skip events and log them.
 *
 */
public class CvnSkipListener implements SkipListener<CvnRootBean, InputData<DataSetData>> {
    
    /** 
     * Logger. 
    */
    private final Logger logger = LoggerFactory.getLogger(CvnSkipListener.class);
   
 
    /**
     * On skip in read.
     *
     * @param t the t
     */
    @Override
    public void onSkipInRead(Throwable t) {
        logger.warn("Skipped CVN reading {}", t.toString());
    }    

    /**
     * On skip in process.
     *
     * @param item the item
     * @param t the t
     */
    @Override
    public void onSkipInProcess(CvnRootBean item, Throwable t) {
        logger.warn("Skipped CVN processing {}, {}", item, t.toString());        
    }

    /**
     * On skip in write.
     *
     * @param item the item
     * @param t the t
     */
    @Override
    public void onSkipInWrite(InputData<DataSetData> item, Throwable t) {
        logger.warn("Skipped CVN writing {}, {}", item, t.toString());
    }

}
