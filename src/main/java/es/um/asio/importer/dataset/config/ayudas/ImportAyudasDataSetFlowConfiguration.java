package es.um.asio.importer.dataset.config.ayudas;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.ayudas.AmortizacionFinanciacionRegistroAyudaDefinitiva;
import es.um.asio.domain.ayudas.AnualidadFinanciacionRegistroAyudaDefinitiva;
import es.um.asio.domain.ayudas.ConvocatoriaAyuda;
import es.um.asio.domain.ayudas.DesgloseGastoRegistroAyudaDefinitiva;
import es.um.asio.domain.ayudas.DesgloseGastoRegistroAyudaProvisional;
import es.um.asio.domain.ayudas.DesgloseGastoSolitudAyuda;
import es.um.asio.domain.ayudas.EntidadColaboradoraSolicitudAyuda;
import es.um.asio.domain.ayudas.EquipoSolicitudAyuda;
import es.um.asio.domain.ayudas.FinanciacionRegistroAyudaDefinitiva;
import es.um.asio.domain.ayudas.FinanciacionUnidadAyuda;
import es.um.asio.domain.ayudas.FuenteUnidadAyuda;
import es.um.asio.domain.ayudas.PartidaConvocatoria;
import es.um.asio.domain.ayudas.ReferenciaUnescoSolicitudAyuda;
import es.um.asio.domain.ayudas.RegistroAyudaDefinitiva;
import es.um.asio.domain.ayudas.RegistroAyudaProvisional;
import es.um.asio.domain.ayudas.SolicitudAyuda;
import es.um.asio.domain.ayudas.SubprogramaAyuda;
import es.um.asio.domain.ayudas.TipoAyuda;
import es.um.asio.domain.ayudas.TipoGastoUnidadAyuda;
import es.um.asio.domain.ayudas.UnidadAyuda;
import es.um.asio.domain.ayudas.UnidadGestoraUnidadAyuda;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

/**
 * Class to generate Ayudas {@link Flow}
 */
@Configuration
public class ImportAyudasDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Gets the Ayudas flow name.
     *
     * @return the flow name
     */
    @Override
    protected String getFlowName() {
        return "importAyudasFlow";
    }
    
    /**
     * Gets Ayudas {@link Flow}
     *
     * @return the flow
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(AmortizacionFinanciacionRegistroAyudaDefinitiva.class,"dataset/Ayudas/Amortizaciones financiacion registros ayudas definitivas.xml"))                
                .next(createStep(AnualidadFinanciacionRegistroAyudaDefinitiva.class,"dataset/Ayudas/Anualidades financiacion registros ayudas definitivas.xml"))
                .next(createStep(ConvocatoriaAyuda.class,"dataset/Ayudas/Convocatorias ayudas.xml"))
                .next(createStep(DesgloseGastoRegistroAyudaDefinitiva.class,"dataset/Ayudas/Desglose gastos registro ayudas definitivas.xml"))
                .next(createStep(DesgloseGastoRegistroAyudaProvisional.class,"dataset/Ayudas/Desglose gastos registro ayudas provisionales.xml"))
                .next(createStep(DesgloseGastoSolitudAyuda.class,"dataset/Ayudas/Desglose gastos solicitudes ayuda.xml"))
                .next(createStep(EntidadColaboradoraSolicitudAyuda.class,"dataset/Ayudas/Entidades colaboradoras solicitudes ayudas.xml"))
                .next(createStep(EquipoSolicitudAyuda.class,"dataset/Ayudas/Equipos solicitudes ayuda.xml"))
                .next(createStep(FinanciacionRegistroAyudaDefinitiva.class,"dataset/Ayudas/Financiacion registros ayudas definitivas.xml"))
                .next(createStep(FinanciacionUnidadAyuda.class,"dataset/Ayudas/Financiacion unidades ayudas.xml"))
                .next(createStep(FuenteUnidadAyuda.class,"dataset/Ayudas/Fuentes unidades ayuda.xml"))
                .next(createStep(PartidaConvocatoria.class,"dataset/Ayudas/Partidas convocatorias.xml"))
                .next(createStep(ReferenciaUnescoSolicitudAyuda.class,"dataset/Ayudas/Referencias unesco solicitudes ayuda.xml"))
                .next(createStep(RegistroAyudaDefinitiva.class,"dataset/Ayudas/Registros ayudas definitivas.xml"))
                .next(createStep(RegistroAyudaProvisional.class,"dataset/Ayudas/Registro ayudas provisionales.xml"))
                .next(createStep(SolicitudAyuda.class,"dataset/Ayudas/Solicitudes ayudas.xml"))
                .next(createStep(SubprogramaAyuda.class,"dataset/Ayudas/Subprogramas ayudas.xml"))
                .next(createStep(TipoAyuda.class,"dataset/Ayudas/Tipos ayudas.xml"))
                .next(createStep(TipoGastoUnidadAyuda.class,"dataset/Ayudas/Tipos gastos unidades ayudas.xml"))
                .next(createStep(UnidadAyuda.class,"dataset/Ayudas/Unidades ayudas.xml"))
                .next(createStep(UnidadGestoraUnidadAyuda.class,"dataset/Ayudas/Unidades gestoras unidades ayudas.xml"))
                .build();         
    }
}