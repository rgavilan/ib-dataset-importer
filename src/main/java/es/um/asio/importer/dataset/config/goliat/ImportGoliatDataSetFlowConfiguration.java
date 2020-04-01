package es.um.asio.importer.dataset.config.goliat;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;


import es.um.asio.domain.goliat.DedicacionInvestigador;
import es.um.asio.domain.goliat.NoLaborables;
import es.um.asio.domain.goliat.Timesheet;
import es.um.asio.domain.goliat.WorkDescription;
import es.um.asio.domain.goliat.WorkLog;
import es.um.asio.domain.goliat.WorkPackage;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

/**
 * Class to generate Goliat {@link Flow}
 */
@Configuration
public class ImportGoliatDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Gets the Goliat flow name.
     *
     * @return the flow name
     */
    @Override
    protected String getFlowName() {
        return "importGoliatFlow";
    }
    
    /**
     * Gets Goliat {@link Flow}
     *
     * @return the flow
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(DedicacionInvestigador.class,"Goliat/Dedicacion investigador.xml"))
                .next(createStep(NoLaborables.class,"Goliat/No laborables.xml"))
                .next(createStep(Timesheet.class,"Goliat/Timesheets.xml"))
                .next(createStep(WorkDescription.class,"Goliat/Work descriptions.xml"))
                .next(createStep(WorkLog.class,"Goliat/Work logs.xml"))
                .next(createStep(WorkPackage.class,"Goliat/Work packages.xml"))
                .build();         
    }
}