package es.um.asio.importer.cvn.service.mockup;

import java.io.StringReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import es.um.asio.importer.cvn.model.CvnChanges;
import es.um.asio.importer.cvn.model.beans.CvnRootBean;
import es.um.asio.importer.cvn.model.beans.ObjectFactory;
import es.um.asio.importer.cvn.service.CvnService;

/**
 * Fake CvnService implementation
 */
@Service
@ConditionalOnProperty(prefix = "app.services.cvn.mockup", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CvnServiceMockupImp implements CvnService {

    /**
     * {@inheritDoc}
     */
    @Override
    public CvnChanges findAllChangesByDate(Date from) {        
        CvnChanges cvnChanges = new CvnChanges();
        cvnChanges.setIds(new Long[]{1L, 2L});
        return cvnChanges;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CvnRootBean findById(Long id) {        
        return createCvnRootBean(id);
    }
    
    /**
     * Creates fake cvn root bean.
     *
     * @param id the id
     * @return the cvn root bean
     */
    @SuppressWarnings("unchecked")
    private CvnRootBean createCvnRootBean(Long id) 
    {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller  unmarshaller = jaxbContext.createUnmarshaller();
                        
            String cvnXml = xmlCvn1;
            if(id.equals(2L)) {
                cvnXml = xmlCvn2;
            }
            StringReader reader = new StringReader(cvnXml);
            return ((JAXBElement<CvnRootBean>)unmarshaller.unmarshal(reader)).getValue();
        } catch (Exception e) {
            return null;
        }        
    }
    
    private static String xmlCvn1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?><CVN xmlns=\"http://codes.cvn.fecyt.es/beans\"><CvnItemBean><Code>000.020.000.000</Code><CvnDateDayMonthYear><Code>000.020.000.020</Code><Value>2020-03-12T00:00:00.000+01:00</Value></CvnDateDayMonthYear><CvnString><Code>000.020.000.070</Code><Value>spa</Value></CvnString><CvnString><Code>000.020.000.080</Code><Value>1.3.0</Value></CvnString></CvnItemBean><CvnItemBean><Code>020.010.010.000</Code><CvnDateDayMonthYear><Code>020.010.010.130</Code><Value>2014-07-28T00:00:00.000+02:00</Value></CvnDateDayMonthYear><CvnEntityBean><Code>020.010.010.090</Code><Id>8435246600011002700000001455226</Id><Name>Universidad de Murcia</Name></CvnEntityBean><CvnString><Code>020.010.010.050</Code><Value>724</Value></CvnString><CvnString><Code>020.010.010.060</Code><Value>ES62</Value></CvnString><CvnString><Code>020.010.010.080</Code><Value>ESPINARDO</Value></CvnString><CvnString><Code>020.010.010.110</Code><Value>000</Value></CvnString><CvnString><Code>020.010.010.140</Code><Value>010</Value></CvnString><CvnTitleBean><Code>020.010.010.030</Code><Name>GRADO EN FARMACIA</Name></CvnTitleBean></CvnItemBean><CvnItemBean><Code>020.010.020.000</Code><CvnAuthorBean><Code>020.010.020.170</Code><CvnFamilyNameBean><Code>020.010.020.170</Code><FirstFamilyName>LUNA MALDONADO</FirstFamilyName></CvnFamilyNameBean><GivenName>AURELIO DE FATIMA ISIDORO</GivenName><Signature>LUNA MALDONADO, A.D.F.I.</Signature><SignatureOrder>1</SignatureOrder></CvnAuthorBean><CvnAuthorBean><Code>020.010.020.180</Code><CvnFamilyNameBean><Code>020.010.020.180</Code><FirstFamilyName>NAVARRO ZARAGOZA</FirstFamilyName></CvnFamilyNameBean><GivenName>JAVIER</GivenName><Signature>NAVARRO ZARAGOZA, J.</Signature><SignatureOrder>1</SignatureOrder></CvnAuthorBean><CvnDateDayMonthYear><Code>020.010.020.140</Code><Value>2018-12-20T00:00:00.000+01:00</Value></CvnDateDayMonthYear><CvnEntityBean><Code>020.010.020.040</Code><Id>8435246600011002700000001455226</Id><Name>Universidad de Murcia</Name></CvnEntityBean><CvnEntityBean><Code>020.010.020.100</Code><Id>8435246600011002700000001455226</Id><Name>Universidad de Murcia</Name></CvnEntityBean><CvnString><Code>020.010.020.060</Code><Value>724</Value></CvnString><CvnString><Code>020.010.020.070</Code><Value>ES62</Value></CvnString><CvnString><Code>020.010.020.120</Code><Value>000</Value></CvnString><CvnString><Code>020.010.020.160</Code><Value>Desarrollo y validación de un método para el análisis de opiáceos y cocaína en tejido óseo humano</Value></CvnString><CvnTitleBean><Code>020.010.020.010</Code><Name>PROGRAMA DE DOCTORADO EN INTEGRACIÓN Y MODULACIÓN DE SEÑALES EN BIOMEDICINA</Name></CvnTitleBean></CvnItemBean></CVN>";
    private static String xmlCvn2 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?><CVN xmlns=\"http://codes.cvn.fecyt.es/beans\">   <CvnItemBean><Code>050.010.000.000</Code><CvnAuthorBean><Code>050.010.000.080</Code><CvnFamilyNameBean><Code>050.010.000.080</Code><FirstFamilyName>LUNA MALDONADO</FirstFamilyName></CvnFamilyNameBean><GivenName>AURELIO DE FATIMA ISIDORO</GivenName><Signature>LUNA MALDONADO, A.D.F.I.</Signature><SignatureOrder>1</SignatureOrder></CvnAuthorBean><CvnDateDayMonthYear><Code>050.010.000.140</Code><Value>2014-10-01T00:00:00.000+02:00</Value></CvnDateDayMonthYear><CvnDouble><Code>050.010.000.130</Code><Value>17.0</Value></CvnDouble><CvnDuration><Code>050.010.000.150</Code><Value>P4Y3M1DT0H0M0S</Value></CvnDuration><CvnEntityBean><Code>050.010.000.090</Code><Id>8435246600011002700000001455226</Id><Name>Universidad de Murcia</Name></CvnEntityBean><CvnExternalPKBean><Code>050.010.000.030</Code><Type>110</Type><Value>E008-06</Value></CvnExternalPKBean><CvnString><Code>050.010.000.010</Code><Value>BIOETICA Y DERECHO MEDICO, CIENCIAS FORENSES, CIENCIAS VICTIMOLÓGICAS Y CRIMINOLÓGICAS, CRIMINOGENESIS, CRIMINOLOGIA, DELINCUENCIA, DELITOS VIOLENTOS, DERECHO DE LA SEGURIDAD, DERECHO JUVENIL Y DERECHO DE MENORES, DERECHO PENAL, DIAGNOSTICO POSTMORTEM, DISTRIBUCION POSTMORTEM, DROGAS DE ABUSO, EPIDEMIOLOGIA, EUTANASIA, GRANDES CATASTROFES, IDENTIFICACION, INADAPTACION JUVENIL, INFARTO DE MIOCARDIO, INFORMACION BIOMEDICA, METALES PESADOS, ODONTOLOGIA FORENSE, PATOLOGIA FORENSE, PSIQUIATRIA FORENSE, RADICALES LIBRES, RESTOS CADAVERICOS, SITUACIONES DE RIESGO, SUFRIMIENTO CEREBRAL, TANATOQUIMIA, TERRORISMO Y CRIMEN ORGANIZADO, TOXICOLOGIA, TOXICOLOGIA AMBIENTAL, TRANSPLANTE DE ORGANOS, VICTIMOLOGÍA</Value></CvnString><CvnString><Code>050.010.000.020</Code><Value>MEDICINA LEGAL Y TOXICOLOGIA</Value></CvnString><CvnString><Code>050.010.000.040</Code><Value>724</Value></CvnString><CvnString><Code>050.010.000.050</Code><Value>ES62</Value></CvnString><CvnString><Code>050.010.000.070</Code><Value>MURCIA</Value></CvnString><CvnString><Code>050.010.000.110</Code><Value>000</Value></CvnString><CvnString><Code>050.010.000.200</Code><Value>CIENCIAS FORENSES, CRIMINOGENESIS, CRIMINOLOGIA, DELINCUENCIA, DELITOS VIOLENTOS, DERECHO DE LA SEGURIDAD, DERECHO JUVENIL Y DERECHO DE MENORES, DERECHO PENAL, DIAGNOSTICO POSTMORTEM, DISTRIBUCION POSTMORTEM, EPIDEMIOLOGIA, EUTANASIA, GRANDES CATASTROFES, IDENTIFICACION, INFARTO DE MIOCARDIO, INFORMACION BIOMEDICA, METALES PESADOS, PSIQUIATRIA FORENSE, RADICALES LIBRES, RESTOS CADAVERICOS, SITUACIONES DE RIESGO, SUFRIMIENTO CEREBRAL, TANATOQUIMIA, TERRORISMO Y CRIMEN ORGANIZADO, TOXICOLOGIA AMBIENTAL, TRANSPLANTE DE ORGANOS, VICTIMOLOGÍA</Value></CvnString></CvnItemBean><CvnItemBean><Code>060.030.010.000</Code><CvnDateDayMonthYear><Code>060.030.010.130</Code><Value>2015-01-01T00:00:00.000+01:00</Value></CvnDateDayMonthYear><CvnDuration><Code>060.030.010.140</Code><Value>P3Y11M20DT0H0M0S</Value></CvnDuration><CvnEntityBean><Code>060.030.010.080</Code><Name>UNIVERSIDAD DE MURCIA</Name></CvnEntityBean><CvnString><Code>060.030.010.010</Code><Value>Análisis de la relación entre los valores toxicológicos de sangre, plasma y tejido óseo</Value></CvnString><CvnString><Code>060.030.010.020</Code><Value>724</Value></CvnString><CvnString><Code>060.030.010.030</Code><Value>ES62</Value></CvnString><CvnString><Code>060.030.010.150</Code><Value>MURCIA</Value></CvnString><CvnString><Code>060.030.010.060</Code><Value>710</Value></CvnString><CvnString><Code>060.030.010.100</Code><Value>070</Value></CvnString></CvnItemBean><CvnItemBean><Code>030.010.000.000</Code><CvnDateDayMonthYear><Code>030.010.000.250</Code><Value>2017-01-01T00:00:00.000+01:00</Value></CvnDateDayMonthYear><CvnDouble><Code>030.010.000.220</Code><Value>6.0</Value></CvnDouble><CvnDouble><Code>030.010.000.240</Code><Value>2.0</Value></CvnDouble><CvnEntityBean><Code>030.010.000.080</Code><Id>8435246600011002700000001455226</Id><Name>Universidad de Murcia</Name></CvnEntityBean><CvnString><Code>030.010.000.010</Code><Value>016</Value></CvnString><CvnString><Code>030.010.000.040</Code><Value>724</Value></CvnString><CvnString><Code>030.010.000.050</Code><Value>ES62</Value></CvnString><CvnString><Code>030.010.000.070</Code><Value>ESPINARDO</Value></CvnString><CvnString><Code>030.010.000.110</Code><Value>000</Value></CvnString><CvnString><Code>030.010.000.130</Code><Value>CIENCIAS SOCIO-SANITARIAS</Value></CvnString><CvnString><Code>030.010.000.160</Code><Value>TOXICOLOGÍA</Value></CvnString><CvnString><Code>030.010.000.170</Code><Value>700</Value></CvnString><CvnString><Code>030.010.000.190</Code><Value>010</Value></CvnString><CvnString><Code>030.010.000.200</Code><Value>CUARTO</Value></CvnString><CvnTitleBean><Code>030.010.000.020</Code><Name>GRADO EN FARMACIA</Name></CvnTitleBean></CvnItemBean></CVN>";
}