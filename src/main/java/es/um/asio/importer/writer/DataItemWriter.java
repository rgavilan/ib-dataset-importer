package es.um.asio.importer.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import es.um.asio.domain.DataSetData;
import es.um.asio.domain.InputData;

/**
 * Implementation of {@link ItemWriter} for {@link InputData<DataSetData>}.
 * Send data to Kafka topic.
 */
public class DataItemWriter implements ItemWriter<InputData<DataSetData>> {
    
    /**
     * Logger
     */
	private final Logger logger = LoggerFactory.getLogger(DataItemWriter.class);
   
    /**
     * Kafka template.
     */
    @Autowired
    private KafkaTemplate<String, InputData<DataSetData>> kafkaTemplate;
    
    /**
     * Topic name
     */
    @Value("${app.kafka.input-topic-name}")
    private String topicName;

    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(List<? extends InputData<DataSetData>> datas) throws Exception {
		for(InputData<DataSetData> data : datas) {
            logger.info("Send data to input-data kafka topic: {}", data.getClass());
            
		    kafkaTemplate.send(topicName, data);
		}
	}

}
