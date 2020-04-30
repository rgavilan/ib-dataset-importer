package es.um.asio.importer.cvn.exception;

/**
 * The Class LastImportRequestException.
 */
public class LastImportRequestException extends RuntimeException {
    
    /**
     * Instantiates a new last import request exception.
     */
    public LastImportRequestException() {
        super();
    }
    
    /**
     * Cvn request exception.
     *
     * @param cause the cause
     */
    public LastImportRequestException(String uri, Throwable cause) {
        super("Failed last import request: " + uri, cause);
    }
}
