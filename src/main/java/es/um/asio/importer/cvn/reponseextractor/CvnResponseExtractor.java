package es.um.asio.importer.cvn.reponseextractor;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import es.um.asio.importer.cvn.model.beans.CvnRootBean;


/**
 * The Class CvnResponseExtractor.
 */
public class CvnResponseExtractor extends HttpMessageConverterExtractor<CvnRootBean> {

    /**
     * Instantiates a new cvn response extractor.
     *
     * @param messageConverters the message converters
     */
    public CvnResponseExtractor (List<HttpMessageConverter<?>> messageConverters) {
        super(CvnRootBean.class, messageConverters);
    }

    /**
     * Extract data.
     *
     * @param response the response
     * @return the cvn root bean
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public CvnRootBean extractData(ClientHttpResponse response) throws IOException {

        if (response.getStatusCode() == HttpStatus.OK) {
            response.getHeaders().set("Content-Type", MediaType.APPLICATION_XML.toString());
            return super.extractData(response);             
        } else {
            return null;
        }
    }
}