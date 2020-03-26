package es.um.asio.importer.dataset.config.patentes;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.patentes.CostePatente;
import es.um.asio.domain.patentes.EmpresaExplotacionPatente;
import es.um.asio.domain.patentes.EmpresaTitularPatente;
import es.um.asio.domain.patentes.InventorPatente;
import es.um.asio.domain.patentes.Patente;
import es.um.asio.domain.patentes.ProteccionPatente;
import es.um.asio.domain.patentes.SectorIndustrialPatente;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

/**
 * Class to generate Patentes {@link Flow}
 */
@Configuration
public class ImportPatentesDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Gets the Patentes flow name.
     *
     * @return the flow name
     */
    @Override
    protected String getFlowName() {
        return "importPatentesFlow";
    }
    
    /**
     * Gets Patentes {@link Flow}
     *
     * @return the flow
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(CostePatente.class,"dataset/Patentes/Costes patentes.xml"))                
                .next(createStep(EmpresaExplotacionPatente.class,"dataset/Patentes/Empresas explotan patentes.xml"))
                .next(createStep(EmpresaTitularPatente.class,"dataset/Patentes/Empresas titulares patentes.xml"))
                .next(createStep(InventorPatente.class,"dataset/Patentes/Inventores patentes.xml"))     
                .next(createStep(Patente.class,"dataset/Patentes/Patentes.xml"))
                .next(createStep(ProteccionPatente.class,"dataset/Patentes/Protecciones patentes.xml"))
                .next(createStep(SectorIndustrialPatente.class,"dataset/Patentes/Sectores industriales patentes.xml"))
                .build();         
    }
}