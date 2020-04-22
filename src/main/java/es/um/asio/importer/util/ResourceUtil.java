package es.um.asio.importer.util;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ResourceUtil {
    
    private ResourceUtil() {
        throw new IllegalStateException("Utility class");
      }    
    
    /**
     * Returns the Resource that represents {@link filePath}, and all Resources related with {@link filePath}.
     * Typically, related resources contains same file name as {@link filePath} but end with number.
     * Example:
     * test/filename.xml
     * Related files: 
     * test/filename2.xml
     * test/filename3.xml
     *
     * @return {@link Resource}.
     * @throws IOException 
     */
    public static Resource[] getRelatedResources(final String filePath, final boolean isClassPathResource) throws IOException {    
        ArrayList<Resource> resources = new ArrayList<>();
        Resource baseResource = getFile(filePath, isClassPathResource);        
        String fileName = baseResource.getFilename();
        var filesInDirectorypattern = getParentPath(baseResource).toString().concat("*");
        for (Resource resource : new PathMatchingResourcePatternResolver().getResources(filesInDirectorypattern)) {
            if(RelatedFileMatcher.match(fileName, resource.getFilename())){
                resources.add(resource);
            }
        }        
        return resources.toArray(new Resource[resources.size()]);
    }


    /**
     * Gets the parent path.
     *
     * @param baseResource the base resource
     * @return the parent path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static URI getParentPath(final Resource baseResource) throws IOException {
        var uri = baseResource.getURI();
        return uri.getPath().endsWith("/") ? uri.resolve("..") : uri.resolve(".");
    }
          
    
    /**
     * Gets the file.
     *
     * @return {@link Resource}.
     */
    public static Resource getFile(final String filePath, final boolean isClassPathResource) {
        Resource file;

        if (isClassPathResource) {
            file = new ClassPathResource(filePath);
        } else {
            file = new FileSystemResource(filePath);
        }
        
        return file;
    }
}
