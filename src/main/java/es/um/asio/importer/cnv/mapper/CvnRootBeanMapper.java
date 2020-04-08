package es.um.asio.importer.cnv.mapper;

import org.mapstruct.Mapper;

import es.um.asio.importer.cnv.model.beans.CvnRootBean;


/**
 *  MapStruct Mapper for {@link CvnRootBean}.
 */
@Mapper
public interface CvnRootBeanMapper {
    
    /**
     * Convert entity to domain CvnRootBean.
     *
     * @param entity
     *            the entity
     * @return the domain entity
     */
    es.um.asio.domain.cvn.CvnRootBean map(CvnRootBean cvnRootBean);
}
