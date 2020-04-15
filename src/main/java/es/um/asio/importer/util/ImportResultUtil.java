package es.um.asio.importer.util;

import org.springframework.batch.core.JobExecution;

import es.um.asio.domain.importResult.ExitStatusCode;
import es.um.asio.domain.importResult.ImportResult;
import es.um.asio.domain.importResult.JobType;
import es.um.asio.importer.constants.Constants;

/**
 * Utils for ImportResult class.
 */
public class ImportResultUtil {
    
    private ImportResultUtil() {
        throw new IllegalStateException("Utility class");
      }

    /**
     * Creates an {@link ImportResult} from {@link JobExecution}.
     *
     * @param jobExecution the job execution
     * @return the import result
     */
    public static ImportResult createFrom(JobExecution jobExecution) {
        JobType jobType = JobType.UNKNOWN;       
        if(jobExecution.getJobInstance().getJobName().equals(Constants.DATASET_JOB_NAME)) {
            jobType = JobType.DATASET;
        }
        else if(jobExecution.getJobInstance().getJobName().equals(Constants.CVN_JOB_NAME)) {
            jobType = JobType.CVN;
        }
            
        ImportResult importResult = ImportResult.builder()
                .startTime(jobExecution.getStartTime())
                .endTime(jobExecution.getEndTime())
                .exitStatusCode(ExitStatusCode.valueOf(jobExecution.getExitStatus().getExitCode()))
                .jobType(jobType)
                .build();
        importResult.setVersion(jobExecution.getId());
        
        return importResult;
    }
}
