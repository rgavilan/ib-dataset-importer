package es.um.asio.importer.cvn.exception;


/**
 * The Class CvnChangesRequestException.
 */
public class CvnChangesRequestException extends RuntimeException {

    /**
     * Instantiates a new cvn changes request exception.
     */
    public CvnChangesRequestException() {
        super();
    }
    
    /**
     * Instantiates a new cvn changes request exception.
     *
     * @param cause the cause
     */
    public CvnChangesRequestException(String uri, Throwable cause) {
        super("Failed cvn changes request: " + uri, cause);
    }

}
