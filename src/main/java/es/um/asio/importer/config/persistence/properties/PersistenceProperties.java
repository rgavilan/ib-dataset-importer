package es.um.asio.importer.config.persistence.properties;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

/**
 * Persistence related properties.
 */
@ConfigurationProperties("app.persistence")
@Validated
@Getter
@Setter
public class PersistenceProperties {
    /**
     * Datasource related properties.
     */
    @NotNull
    @NestedConfigurationProperty
    private DatasourceProperties datasource;

}
