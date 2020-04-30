package es.um.asio.importer.cvn.skippolicy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import es.um.asio.importer.cvn.exception.CvnRequestException;


/**
 * Policy for skipping {@link CvnRequestException}
 */
public class CvnRequestExceptionSkipPolicy implements SkipPolicy {

    /**
     * Should skip.
     *
     * @param t the t
     * @param skipCount the skip count
     * @return true, if successful
     * @throws SkipLimitExceededException the skip limit exceeded exception
     */
    @Override
    public boolean shouldSkip(Throwable t, int skipCount) {        
        return t instanceof CvnRequestException;
    }

}
