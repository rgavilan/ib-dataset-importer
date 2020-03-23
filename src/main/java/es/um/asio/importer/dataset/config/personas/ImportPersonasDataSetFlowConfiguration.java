package es.um.asio.importer.dataset.config.personas;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.personas.Persona;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

@Configuration
public class ImportPersonasDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
      
    @Override
    protected String getFlowName() {
        return "importPersonasFlow";
    }
    
    /**
     * Genera el {@link Flow} de personas
     *
     * @return
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(Persona.class,"Personas/Personas.xml"))
                .build();         
    }
}