package es.um.asio.importer.dataset.config.personas;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.personas.Persona;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

/**
 * Class to generate Personas {@link Flow}
 */
@Configuration
public class ImportPersonasDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Gets the Personas flow name.
     *
     * @return the flow name
     */
    @Override
    protected String getFlowName() {
        return "importPersonasFlow";
    }
    
    /**
     * Gets Personas {@link Flow}
     *
     * @return the flow
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(Persona.class,"Personas/Personas.xml"))
                .build();         
    }
}