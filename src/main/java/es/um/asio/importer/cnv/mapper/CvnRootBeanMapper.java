package es.um.asio.importer.cnv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "entityId", ignore = true)
    es.um.asio.domain.cvn.CvnRootBean map(CvnRootBean cvnRootBean);
}
