package es.um.asio.importer.cvn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import es.um.asio.importer.marshaller.CvnUnmarshaller;
import es.um.asio.domain.cvn.Cvn;

/**
 * The Class CvnUnmarshallerTest.
 */
class CvnUnmarshallerTest {

    /**
     * When unmarshaller CVNXML then cvn is not null.
     *
     * @throws JAXBException the JAXB exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void whenUnmarshallerCVNXML_thenCvnIsNotNull() throws JAXBException, IOException {        
        CvnUnmarshaller cvnUnmarshaller = new CvnUnmarshaller();
        String cvnXml = givenACvnXml();
        
        Cvn cvn = cvnUnmarshaller.unmarshal(cvnXml);
        
        assertNotNull(cvn);
    }
    
    /**
     * When unmarshaller CVNXML then fill item beans.
     *
     * @throws JAXBException the JAXB exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    void whenUnmarshallerCVNXML_thenFillItemBeans() throws JAXBException, IOException {
        CvnUnmarshaller cvnUnmarshaller = new CvnUnmarshaller();
        String cvnXml = givenACvnXml();
        
        Cvn cvn = cvnUnmarshaller.unmarshal(cvnXml);
        
        assertThat(cvn.getCvnItemBean()).isNotEmpty();
    }
    
    /**
     * Given A cvn xml.
     *
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String givenACvnXml() throws IOException {
        String cvnFilePath = new File(getClass().getClassLoader().getResource("CVN.xml").getFile()).getAbsolutePath();
        return new String (Files.readAllBytes(Paths.get(cvnFilePath)));
    }
}
