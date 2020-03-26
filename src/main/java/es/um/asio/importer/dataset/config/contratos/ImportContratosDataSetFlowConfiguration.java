package es.um.asio.importer.dataset.config.contratos;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.contratos.AnualidadContratoProyecto;
import es.um.asio.domain.contratos.ContratoProyecto;
import es.um.asio.domain.contratos.DesgloseGastoContratoProyecto;
import es.um.asio.domain.contratos.DesgloseGastoProrrogaContrato;
import es.um.asio.domain.contratos.EquipoContratoProyecto;
import es.um.asio.domain.contratos.ImpuestoContratoProyecto;
import es.um.asio.domain.contratos.ImpuestoProrrogaContrato;
import es.um.asio.domain.contratos.PatenteContratoProyecto;
import es.um.asio.domain.contratos.ProrrogaContratoProyecto;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

/**
 * Class to generate Contratos {@link Flow}
 */
@Configuration
public class ImportContratosDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Gets the Contratos flow name.
     *
     * @return the flow name
     */
    @Override
    protected String getFlowName() {
        return "importContratosFlow";
    }
    
    /**
     * Gets Contratos {@link Flow}
     *
     * @return the flow
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(AnualidadContratoProyecto.class,"dataset/Contratos/Anualidades contratos proyectos.xml"))                
                .next(createStep(ContratoProyecto.class,"dataset/Contratos/Contratos proyectos.xml"))
                .next(createStep(DesgloseGastoContratoProyecto.class,"dataset/Contratos/Desglose gastos contratos proyectos.xml"))
                .next(createStep(DesgloseGastoProrrogaContrato.class,"dataset/Contratos/Desglose gastos prorrogas contratos.xml"))
                .next(createStep(EquipoContratoProyecto.class,"dataset/Contratos/Equipos contratos proyectos.xml"))
                .next(createStep(ImpuestoContratoProyecto.class,"dataset/Contratos/Impuestos contratos proyectos.xml"))
                .next(createStep(ImpuestoProrrogaContrato.class,"dataset/Contratos/Impuestos prorrogas contratos.xml"))
                .next(createStep(PatenteContratoProyecto.class,"dataset/Contratos/Patentes contratos proyectos.xml"))
                .next(createStep(ProrrogaContratoProyecto.class,"dataset/Contratos/Prorrogas contratos proyectos.xml"))

                .build();         
    }
}