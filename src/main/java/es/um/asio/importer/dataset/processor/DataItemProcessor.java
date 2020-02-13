package es.um.asio.importer.dataset.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;

/**
 * Implementacion de {@link ItemProcessor} para la clase {@link DataSetData}.
 */
public class DataItemProcessor implements ItemProcessor<DataSetData, InputData<DataSetData>> {

    private static final Logger log = LoggerFactory.getLogger(DataItemProcessor.class);

    @Override
    public InputData<DataSetData> process(final DataSetData data) throws Exception {
        log.info("Processing data ({})", data);
        return new InputData<DataSetData>(data);
    }

}