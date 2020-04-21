package es.um.asio.importer.cnv.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import es.um.asio.importer.cnv.mapper.CvnRootBeanMapper;
import es.um.asio.importer.cnv.model.beans.CvnRootBean;

/**
 * Processor in charge of converts Cvn bean to Cvn domain.
 */
public class CvnToDomainCvnProcessor implements ItemProcessor<CvnRootBean, es.um.asio.domain.cvn.CvnRootBean> {

    /**
     *  The mapper.
    */
    @Autowired
    CvnRootBeanMapper mapper;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public es.um.asio.domain.cvn.CvnRootBean process(CvnRootBean cvnRootBean) throws Exception {
        return mapper.map(cvnRootBean);
    }

}
