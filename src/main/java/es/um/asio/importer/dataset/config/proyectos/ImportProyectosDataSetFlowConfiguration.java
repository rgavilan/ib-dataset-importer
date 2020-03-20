package es.um.asio.importer.dataset.config.proyectos;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.proyectos.FechaProyecto;
import es.um.asio.domain.proyectos.JustificacionPrevistaProyecto;
import es.um.asio.domain.proyectos.OrigenProyecto;
import es.um.asio.domain.proyectos.Proyecto;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

@Configuration
public class ImportProyectosDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase { 
    
    /**
     * Genera el {@link Flow} de importacion de proyectos
     *
     * @return
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>("importProyectosFlow")
                .start(createStep(Proyecto.class,"dataset/Proyectos/Proyectos.xml"))                
                .next(createStep(JustificacionPrevistaProyecto.class,"dataset/Proyectos/Justificaciones previstas proyectos.xml"))
                .next(createStep(OrigenProyecto.class,"dataset/Proyectos/Origenes proyectos.xml"))
                .next(createStep(FechaProyecto.class,"dataset/Proyectos/Fechas proyectos.xml"))                              
                .build();         
    }
}
