package es.um.asio.importer.dataset.config.actividades;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.actividades.Actividad;
import es.um.asio.domain.actividades.FacturaEmitidaActividad;
import es.um.asio.domain.actividades.GrupoActividades;
import es.um.asio.domain.actividades.GrupoActividadesProyecto;
import es.um.asio.domain.actividades.ImpuestoRepercutidoActividad;
import es.um.asio.domain.actividades.TipoActividad;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

@Configuration
public class ImportActividadesDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
      
    @Override
    protected String getFlowName() {
        return "importActividadesFlow";
    }
    
    /**
     * Genera el {@link Flow} de actividades
     *
     * @return
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(Actividad.class,"dataset/Actividades/Actividades.xml"))                
                .next(createStep(FacturaEmitidaActividad.class,"dataset/Actividades/Facturas emitidas actividades.xml"))
                .next(createStep(GrupoActividadesProyecto.class,"dataset/Actividades/Grupos actividades proyectos.xml"))
                .next(createStep(GrupoActividades.class,"dataset/Actividades/Grupos actividades.xml"))     
                .next(createStep(ImpuestoRepercutidoActividad.class,"dataset/Actividades/Impuestos repercutidos actividades.xml"))
                .next(createStep(TipoActividad.class,"dataset/Actividades/Tipos actividades.xml"))
                .build();         
    }
}