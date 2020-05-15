package es.um.asio.importer.dataset.reader;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.springframework.batch.item.xml.StaxEventItemReader;

/**
 * @inheritDoc 
 * Not throws exception if Resource is invalid
 */
public class XmlEventItemReader<T> extends StaxEventItemReader<T> {

    private boolean invalidResource;
    
    /**
     * @inheritDoc * 
     */
    @Override
    protected void doOpen() throws Exception {
        try {
            super.doOpen();
        } catch (IllegalStateException e) {
            invalidResource = true;
        }
    }

    /**
     * @inheritDoc * 
     */
    @Override
    protected T doRead() throws IOException, XMLStreamException {
        if(this.invalidResource) {
            return null;
        }
        
        return super.doRead();
    }

}
