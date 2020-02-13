package es.um.asio.importer.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuracion general de los job. En esta clase se definiría:
 * <ul>
 * <li>Datasource a utilizar para la base de datos de control de jobs (si no hay ninguna otra definicion de datasource
 * en otro modulo)</li>
 * <li>Otros bean necesarios</li>
 * </ul>
 *
 * En caso de que hubiese mas de un datasource, se debera desambiguar utilizando la anotacion {@link Primary} si es
 * posible. Si no, se debera
 * extender la clase {@link DefaultBatchConfigurer} como aparece en la documentacion de Spring Batch
 */
@EnableBatchProcessing
@Configuration
public class BatchConfig {

}
