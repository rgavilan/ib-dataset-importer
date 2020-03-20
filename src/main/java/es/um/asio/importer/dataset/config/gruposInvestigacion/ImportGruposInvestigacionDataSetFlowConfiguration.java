package es.um.asio.importer.dataset.config.gruposInvestigacion;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.gruposInvestigacion.DatosContactoGrupo;
import es.um.asio.domain.gruposInvestigacion.GrupoInvestigacion;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

@Configuration
public class ImportGruposInvestigacionDataSetFlowConfiguration  extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Genera el {@link Flow} de importacion de grupos de investigacion
     *
     * @return
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>("importGruposInvestigacionFlow")
                .start(createStep(GrupoInvestigacion.class,"dataset/Grupos de investigación/Grupos de investigacion.xml"))
                .next(createStep(DatosContactoGrupo.class,"dataset/Grupos de investigación/Datos contacto grupos.xml"))
                .build();         
    }
}