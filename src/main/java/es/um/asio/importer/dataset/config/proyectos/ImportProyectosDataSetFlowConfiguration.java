package es.um.asio.importer.dataset.config.proyectos;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.proyectos.AnualidadProyecto;
import es.um.asio.domain.proyectos.DatosAnualidadProyecto;
import es.um.asio.domain.proyectos.DependenciaProyecto;
import es.um.asio.domain.proyectos.FacturaEmitirProyecto;
import es.um.asio.domain.proyectos.FacturaProyecto;
import es.um.asio.domain.proyectos.FechaProyecto;
import es.um.asio.domain.proyectos.FinanciacionProyecto;
import es.um.asio.domain.proyectos.GastoPrevistoProyecto;
import es.um.asio.domain.proyectos.GastoProyecto;
import es.um.asio.domain.proyectos.ImporteGastoProyecto;
import es.um.asio.domain.proyectos.ImpuestoRepercutidoProyecto;
import es.um.asio.domain.proyectos.IngresoProyecto;
import es.um.asio.domain.proyectos.JustificacionPrevistaProyecto;
import es.um.asio.domain.proyectos.OrigenProyecto;
import es.um.asio.domain.proyectos.Proyecto;
import es.um.asio.domain.proyectos.ProyectoDependenciaProyecto;
import es.um.asio.domain.proyectos.RelacionOrigenProyecto;
import es.um.asio.domain.proyectos.TipoActividad;
import es.um.asio.domain.proyectos.TipoAuditoriaProyecto;
import es.um.asio.domain.proyectos.TipoFinanciacion;
import es.um.asio.domain.proyectos.TipoFuenteFinanciacion;
import es.um.asio.domain.proyectos.TipoGastoGenerico;
import es.um.asio.domain.proyectos.TipoIngresoGenerico;
import es.um.asio.domain.proyectos.TipoMotivoCambioFecha;
import es.um.asio.domain.proyectos.TipoOrigenProyecto;
import es.um.asio.domain.proyectos.TipoRechazoJustificacion;
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
                .start(createStep(AnualidadProyecto.class,"dataset/Proyectos/Anualidades proyectos.xml"))                
                .next(createStep(DatosAnualidadProyecto.class,"dataset/Proyectos/Datos anualidades proyectos.xml"))        
                .next(createStep(DependenciaProyecto.class,"dataset/Proyectos/Dependencias proyectos.xml"))        
                .next(createStep(FacturaEmitirProyecto.class,"dataset/Proyectos/Facturas emitir proyectos.xml"))      
                .next(createStep(FacturaProyecto.class,"dataset/Proyectos/Facturas proyectos.xml"))      
                .next(createStep(FechaProyecto.class,"dataset/Proyectos/Fechas proyectos.xml"))      
                .next(createStep(FinanciacionProyecto.class,"dataset/Proyectos/Financiacion proyectos.xml"))      
                .next(createStep(GastoPrevistoProyecto.class,"dataset/Proyectos/Gastos previstos proyectos.xml"))      
                .next(createStep(GastoProyecto.class,"dataset/Proyectos/Gastos proyectos.xml"))      
                .next(createStep(ImporteGastoProyecto.class,"dataset/Proyectos/Importes proyectos.xml"))      
                .next(createStep(ImpuestoRepercutidoProyecto.class,"dataset/Proyectos/Impuestos repercutidos proyectos.xml"))      
                .next(createStep(IngresoProyecto.class,"dataset/Proyectos/Ingresos proyectos.xml"))      
                .next(createStep(JustificacionPrevistaProyecto.class,"dataset/Proyectos/Justificaciones previstas proyectos.xml"))      
                .next(createStep(OrigenProyecto.class,"dataset/Proyectos/Origenes proyectos.xml"))      
                .next(createStep(Proyecto.class,"dataset/Proyectos/Proyectos.xml"))      
                .next(createStep(ProyectoDependenciaProyecto.class,"dataset/Proyectos/Proyectos dependencias proyectos.xml"))      
                .next(createStep(RelacionOrigenProyecto.class,"dataset/Proyectos/Relaciones origenes proyectos.xml"))      
                .next(createStep(TipoActividad.class,"dataset/Proyectos/Tipos actividades.xml"))      
                .next(createStep(TipoAuditoriaProyecto.class,"dataset/Proyectos/Tipos auditorias proyectos.xml"))      
                .next(createStep(TipoFinanciacion.class,"dataset/Proyectos/Tipos financiacion.xml"))      
                .next(createStep(TipoFuenteFinanciacion.class,"dataset/Proyectos/Tipos fuentes financiacion.xml"))      
                .next(createStep(TipoGastoGenerico.class,"dataset/Proyectos/Tipos gastos genericos.xml"))      
                .next(createStep(TipoIngresoGenerico.class,"dataset/Proyectos/Tipos ingresos genericos.xml"))      
                .next(createStep(TipoMotivoCambioFecha.class,"dataset/Proyectos/Tipos motivos cambios fechas.xml"))      
                .next(createStep(TipoOrigenProyecto.class,"dataset/Proyectos/Tipos origenes proyectos.xml"))      
                .next(createStep(TipoRechazoJustificacion.class,"dataset/Proyectos/Tipos rechazos justificacion.xml"))             
                .build();         
    }
}
