package es.um.asio.importer.dataset.config.paginas;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Configuration;

import es.um.asio.domain.paginas.Alegacion;
import es.um.asio.domain.paginas.Articulo;
import es.um.asio.domain.paginas.AutorArticulo;
import es.um.asio.domain.paginas.AutorCapituloLibro;
import es.um.asio.domain.paginas.AutorCongreso;
import es.um.asio.domain.paginas.AutorDiseno;
import es.um.asio.domain.paginas.AutorExposicion;
import es.um.asio.domain.paginas.AutorLibro;
import es.um.asio.domain.paginas.AutorPrologoLibro;
import es.um.asio.domain.paginas.AyudaIniciacion;
import es.um.asio.domain.paginas.AyudaPostdoctoral;
import es.um.asio.domain.paginas.CapituloLibro;
import es.um.asio.domain.paginas.ComiteCongreso;
import es.um.asio.domain.paginas.ComiteEditorial;
import es.um.asio.domain.paginas.Congreso;
import es.um.asio.domain.paginas.ContratoPostdoctoral;
import es.um.asio.domain.paginas.Convocatoria;
import es.um.asio.domain.paginas.DiplomaEstudiosAvanzados;
import es.um.asio.domain.paginas.DiplomaEstudiosAvanzadosExterno;
import es.um.asio.domain.paginas.DirectorContratoPostdoctoral;
import es.um.asio.domain.paginas.DirectorDiplomaEstudiosAvanzados;
import es.um.asio.domain.paginas.DirectorDiplomaEstudiosAvanzadosExterno;
import es.um.asio.domain.paginas.DirectorTesinaExterna;
import es.um.asio.domain.paginas.DirectorTesisExterna;
import es.um.asio.domain.paginas.Diseno;
import es.um.asio.domain.paginas.EditorLibro;
import es.um.asio.domain.paginas.EmpresaExplotacionPatente;
import es.um.asio.domain.paginas.Estancia;
import es.um.asio.domain.paginas.EstanciaBreve;
import es.um.asio.domain.paginas.Exposicion;
import es.um.asio.domain.paginas.InventorPatente;
import es.um.asio.domain.paginas.InvestigadorProyectoCooperacionInternacional;
import es.um.asio.domain.paginas.Libro;
import es.um.asio.domain.paginas.MiembroComiteCongreso;
import es.um.asio.domain.paginas.PaisPatente;
import es.um.asio.domain.paginas.PalabraClaveArticulo;
import es.um.asio.domain.paginas.PalabraClaveComiteCongreso;
import es.um.asio.domain.paginas.PalabraClaveCongreso;
import es.um.asio.domain.paginas.PalabraClaveLibro;
import es.um.asio.domain.paginas.PalabraClavePrologoLibro;
import es.um.asio.domain.paginas.Patente;
import es.um.asio.domain.paginas.PrologoLibro;
import es.um.asio.domain.paginas.ProyectoCooperacionInternacional;
import es.um.asio.domain.paginas.RequisitoContratoPostdoctoral;
import es.um.asio.domain.paginas.SolicitudCambioGrupoInvestigacion;
import es.um.asio.domain.paginas.TesinaExterna;
import es.um.asio.domain.paginas.TesisDirigidaUMUNoRegistrada;
import es.um.asio.domain.paginas.TesisExterna;
import es.um.asio.importer.dataset.config.ImportDataSetFlowConfigurationBase;

/**
 * Class to generate Paginas {@link Flow}
 */
@Configuration
public class ImportPaginasDataSetFlowConfiguration extends ImportDataSetFlowConfigurationBase {
    
    /**
     * Gets the Paginas flow name.
     *
     * @return the flow name
     */
    @Override
    protected String getFlowName() {
        return "importPaginasFlow";
    }
    
    /**
     * Gets Paginas {@link Flow}
     *
     * @return the flow
     */
    @Override
    public Flow getFlow() {
        return new FlowBuilder<SimpleFlow>(getFlowName())
                .start(createStep(Alegacion.class,"Paginas/Alegaciones.xml"))
                .next(createStep(Articulo.class,"Paginas/Articulos.xml"))
                .next(createStep(AutorArticulo.class,"Paginas/Autores articulos.xml"))
                .next(createStep(AutorCapituloLibro.class,"Paginas/Autores captitulos libros.xml"))
                .next(createStep(AutorCongreso.class,"Paginas/Autores congresos.xml"))
                .next(createStep(AutorDiseno.class,"Paginas/Autores diseños.xml"))
                .next(createStep(AutorExposicion.class,"Paginas/Autores exposicion.xml"))
                .next(createStep(AutorLibro.class,"Paginas/Autores libros.xml"))
                .next(createStep(AutorPrologoLibro.class,"Paginas/Autores prologo libros.xml"))
                .next(createStep(AyudaIniciacion.class,"Paginas/Ayudas iniciacion.xml"))
                .next(createStep(AyudaPostdoctoral.class,"Paginas/Ayudas postdoctorales.xml"))
                .next(createStep(CapituloLibro.class,"Paginas/Capitulos libros.xml"))
                .next(createStep(ComiteCongreso.class,"Paginas/Comite congresos.xml"))
                .next(createStep(ComiteEditorial.class,"Paginas/Comite editorial.xml"))
                .next(createStep(Congreso.class,"Paginas/Congresos.xml"))
                .next(createStep(ContratoPostdoctoral.class,"Paginas/Contratos postdoctorales.xml"))                
                .next(createStep(Convocatoria.class,"Paginas/Convocatorias.xml"))
                .next(createStep(DiplomaEstudiosAvanzados.class,"Paginas/Diploma estudios avanzados.xml"))
                .next(createStep(DiplomaEstudiosAvanzadosExterno.class,"Paginas/Diploma estudios avanzados externo.xml"))
                .next(createStep(DirectorContratoPostdoctoral.class,"Paginas/Directores contratos postdoctorales.xml"))
                .next(createStep(DirectorDiplomaEstudiosAvanzados.class,"Paginas/Directores diploma estudios avanzados.xml"))
                .next(createStep(DirectorDiplomaEstudiosAvanzadosExterno.class,"Paginas/Directores diploma estudios avanzados externo.xml"))
                .next(createStep(DirectorTesinaExterna.class,"Paginas/Directores tesinas externas.xml"))
                .next(createStep(DirectorTesisExterna.class,"Paginas/Directores tesis externas.xml"))
                .next(createStep(Diseno.class,"Paginas/Diseños.xml"))
                .next(createStep(EditorLibro.class,"Paginas/Editores libros.xml"))
                .next(createStep(EmpresaExplotacionPatente.class,"Paginas/Empresas explota patentes.xml"))
                .next(createStep(Estancia.class,"Paginas/Estancias.xml"))
                .next(createStep(EstanciaBreve.class,"Paginas/Estancias breves.xml"))
                .next(createStep(Exposicion.class,"Paginas/Exposiciones.xml"))
                .next(createStep(InventorPatente.class,"Paginas/Inventores patentes.xml"))
                .next(createStep(InvestigadorProyectoCooperacionInternacional.class,"Paginas/Investigadores proyectos cooperacion internacional.xml"))
                .next(createStep(Libro.class,"Paginas/Libros.xml"))
                .next(createStep(MiembroComiteCongreso.class,"Paginas/Miembros comite congresos.xml"))
                .next(createStep(PaisPatente.class,"Paginas/Paises patentes.xml"))
                .next(createStep(PalabraClaveArticulo.class,"Paginas/Palabras clave articulos.xml"))
                .next(createStep(PalabraClaveComiteCongreso.class,"Paginas/Palabras clave comite congresos.xml"))
                .next(createStep(PalabraClaveCongreso.class,"Paginas/Palabras clave congresos.xml"))
                .next(createStep(PalabraClaveLibro.class,"Paginas/Palabras clave libros.xml"))
                .next(createStep(PalabraClavePrologoLibro.class,"Paginas/Palabras clave prologo libros.xml"))
                .next(createStep(Patente.class,"Paginas/Patentes.xml"))
                .next(createStep(PrologoLibro.class,"Paginas/Prologo libros.xml"))              
                .next(createStep(ProyectoCooperacionInternacional.class,"Paginas/Proyectos cooperacion internacional.xml"))
                .next(createStep(RequisitoContratoPostdoctoral.class,"Paginas/Requisitos contratos postdoctorales.xml"))
                .next(createStep(SolicitudCambioGrupoInvestigacion.class,"Paginas/Solicitudes cambios grupos investigacion.xml"))
                .next(createStep(TesisDirigidaUMUNoRegistrada.class,"Paginas/Tesis dirigidas UMU no registradas.xml"))
                .next(createStep(TesinaExterna.class,"Paginas/Tesinas externas.xml"))
                .next(createStep(TesisExterna.class,"Paginas/Tesis externas.xml"))     
                .build();         
    }
}