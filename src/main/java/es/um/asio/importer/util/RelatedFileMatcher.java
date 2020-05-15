package es.um.asio.importer.util;

import java.util.regex.Pattern;

import com.google.common.io.Files;


/**
 * Utility class in charge of search related file names
 */
public class RelatedFileMatcher { 
   
    private RelatedFileMatcher() {
        throw new IllegalStateException("Utility class");
      }
    
    /**
     * If the match succeeds then candidate is a related file name to fileName
     *
     * @param fileName the file name
     * @param candidate the candidate file name
     * @return true, if successful
     */
    public static boolean match(String fileName, String candidate) {
        if(fileName.equals(candidate)) {
            return true;
        }
        
        String extension = Files.getFileExtension(fileName);
        String candidateExtension = Files.getFileExtension(candidate);
        if(!extension.equals(candidateExtension)) {
            return false;
        }
        
        String fileNameWithoutExtension = Files.getNameWithoutExtension(fileName);
        String candidateNameWithoutExtension = Files.getNameWithoutExtension(candidate);
        
        Pattern pattern = Pattern.compile("("+ fileNameWithoutExtension + ").*[0-9]$");
        return pattern.matcher(candidateNameWithoutExtension).matches();
    }

}
