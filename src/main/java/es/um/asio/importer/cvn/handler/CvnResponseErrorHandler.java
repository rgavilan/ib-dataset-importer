package es.um.asio.importer.cvn.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import es.um.asio.importer.cvn.exception.AuthenticationRequestException;
import es.um.asio.importer.cvn.exception.BadRequestException;
import es.um.asio.importer.cvn.exception.GenericRequestException;
import es.um.asio.importer.cvn.service.CvnService;

/**
 * Class used by the {@link CvnService} to determine response errors
 */
@Component
public class CvnResponseErrorHandler extends DefaultResponseErrorHandler {

    /**
     * Handle cvn request error.
     *
     * @param response the response
     * @param statusCode the status code
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
       if(statusCode == HttpStatus.BAD_REQUEST) {
           throw new BadRequestException();
       }
       else if(statusCode == HttpStatus.FORBIDDEN) {
           throw new AuthenticationRequestException();
       }
       
       try {
           super.handleError(response, statusCode);
        } catch (Exception e) {
            throw new GenericRequestException();
        }
    }

   

}
