package es.um.asio.importer.marshaller;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import es.um.asio.domain.cvn.Cvn;

/**
 * Class that allows to obtain {@link Cvn} from a CVN-XML.
 */
public class CvnUnmarshaller {
    
    /**
     * Unmarshals CVN-XML in {@link Cvn}
     *
     * @param cvnXml the CVN-XML
     * @return the cvn
     * @throws JAXBException the JAXB exception
     */
    public Cvn unmarshal(String cvnXml) throws JAXBException {        
        JAXBContext jaxbContext = JAXBContext.newInstance(Cvn.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader cvnReader = new StringReader(cvnXml);
        return (Cvn)unmarshaller.unmarshal(cvnReader);
    }
}
