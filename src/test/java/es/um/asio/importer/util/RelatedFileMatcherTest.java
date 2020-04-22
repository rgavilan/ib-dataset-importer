package es.um.asio.importer.util;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class RelatedFileMatcherTest {
        
    @Test
    public void whenFileNameIsExactMatching_thenReturnsTrue() {       
        
        assertTrue(RelatedFileMatcher.match("test", "test"));
        assertTrue(RelatedFileMatcher.match("test.jpg", "test.jpg"));
    }
    
    @Test
    public void whenFileNameIsNotExactMatching_thenReturnsFalse() {       
        
        assertFalse(RelatedFileMatcher.match("test", "testtest"));
        assertFalse(RelatedFileMatcher.match("test.jpg", "test.xml"));
    }    
    
    @Test
    public void whenFileNameIsEqualsButEndsWithANumber_thenReturnsTrue() {       
        
        assertTrue(RelatedFileMatcher.match("test", "test23"));
        assertTrue(RelatedFileMatcher.match("test.jpg", "test44.jpg"));
    }
    
    @Test
    public void whenFileNameIsEqualsAndEndsWithANumberButExtensionIsDifferent_thenReturnsFalse() {       
        
        assertFalse(RelatedFileMatcher.match("test", "test23.xml"));
        assertFalse(RelatedFileMatcher.match("test.jpg", "test44.xml"));
    }    
    
    @Test
    public void whenFileNameIsEqualsButStartsWithANumber_thenReturnsFalse() {       
        
        assertFalse(RelatedFileMatcher.match("test", "23test"));
        assertFalse(RelatedFileMatcher.match("test.jpg", "23test.jpg"));
    }
}
