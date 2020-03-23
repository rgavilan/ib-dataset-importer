package es.um.asio.importer.dataset.config.recursosHumanos;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.recursosHumanos.ConvocatoriaRecursosHumanos;
import es.um.asio.domain.recursosHumanos.IdiomaSolicitudRecursosHumanos;
import es.um.asio.domain.recursosHumanos.MeritosSolicitudRecursosHumanos;
import es.um.asio.domain.recursosHumanos.RegistroRecursosHumanosConcedido;
import es.um.asio.domain.recursosHumanos.RegistroRecursosHumanosProvisional;
import es.um.asio.domain.recursosHumanos.SolicitudRecursosHumanos;
import es.um.asio.domain.recursosHumanos.SolicitudRecursosHumanosConcedido;
import es.um.asio.domain.recursosHumanos.SolicitudRegistroRecursosHumanosProvisional;
import es.um.asio.domain.recursosHumanos.SubprogramaRecursosHumanos;
import es.um.asio.domain.recursosHumanos.TipoRecursosHumanos;
import es.um.asio.domain.recursosHumanos.UnidadRecursosHumanos;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

@Configuration
public class ImportRecursosHumanosDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
      
    @Override
    protected String getFlowName() {
        return "importRecursosHumanosFlow";
    }
    
    /**
     * Genera el {@link Flow} de recursos humanos
     *
     * @return
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(ConvocatoriaRecursosHumanos.class,"dataset/Recursos Humanos/Convocatorias recursos humanos.xml"))                
                .next(createStep(IdiomaSolicitudRecursosHumanos.class,"dataset/Recursos Humanos/Idiomas solicitudes recursos humanos.xml"))
                .next(createStep(MeritosSolicitudRecursosHumanos.class,"dataset/Recursos Humanos/Meritos solicitudes recursos humanos.xml"))
                .next(createStep(RegistroRecursosHumanosConcedido.class,"dataset/Recursos Humanos/Registro recurso humano concedido.xml"))     
                .next(createStep(RegistroRecursosHumanosProvisional.class,"dataset/Recursos Humanos/Registro recurso humano provisional.xml"))
                .next(createStep(SolicitudRecursosHumanosConcedido.class,"dataset/Recursos Humanos/Solicitudes recurso humano concendido.xml"))
                .next(createStep(SolicitudRecursosHumanos.class,"dataset/Recursos Humanos/Solicitudes recursos humanos.xml"))
                .next(createStep(SolicitudRegistroRecursosHumanosProvisional.class,"dataset/Recursos Humanos/Solicitudes registro recurso humano provisional.xml"))
                .next(createStep(SubprogramaRecursosHumanos.class,"dataset/Recursos Humanos/Subprogramas recursos humanos.xml"))
                .next(createStep(TipoRecursosHumanos.class,"dataset/Recursos Humanos/Tipos recursos humanos.xml"))
                .next(createStep(UnidadRecursosHumanos.class,"dataset/Recursos Humanos/Unidades recursos humanos.xml"))
                .build();         
    }
}