![](./images/logos_feder.png)



| Entregable     | Importador de datos del DataSet de Murcia                                       |
| -------------- | ------------------------------------------------------------ |
| Fecha          | 25/05/2020                                                   |
| Proyecto       | [ASIO](https://www.um.es/web/hercules/proyectos/asio) (Arquitectura Semántica e Infraestructura Ontológica) en el marco de la iniciativa [Hércules](https://www.um.es/web/hercules/) para la Semántica de Datos de Investigación de Universidades que forma parte de [CRUE-TIC](http://www.crue.org/SitePages/ProyectoHercules.aspx) |
| Módulo         | Importador base                                              |
| Tipo           | Software                                                     |
| Objetivo       | Importador de datos del DataSet de Murcia para el proyecto Backend SGI (ASIO). |
| Estado         | **50%** Está completado al 100% con los datos y servicios de los que disponemos actualmente |
| Próximos pasos | Hacer las modificaciones necesarias para actualizarlo con los nuevos servicios CVN desarrollados por la UM con el protocolo OAI-PMH y con las actualizaciones necesarias para actualizar y borrar datasets. |
| Documentación  | [Manual de usuario](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/12-An%C3%A1lisis/Manual%20de%20usuario/Manual%20de%20usuario.md)<br />[Manual de despliegue](https://github.com/HerculesCRUE/ib-asio-composeset/blob/master/README.md)<br />[Documentación técnica](https://github.com/HerculesCRUE/ib-asio-docs-/blob/master/entregables_hito_1/11-Arquitectura/ASIO_Izertis_Arquitectura.md) |

# ASIO - Importador de datos del DataSet de Murcia

|     | Master |
| --- | ------ |
| Quality Gate | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=HerculesCRUE_ib-dataset-importer&metric=alert_status)](https://sonarcloud.io/dashboard?id=HerculesCRUE_ib-dataset-importer) |
| Coverage | [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=HerculesCRUE_ib-dataset-importer&metric=coverage)](https://sonarcloud.io/dashboard?id=HerculesCRUE_ib-dataset-importer) |

Importador de datos del DataSet de Murcia para el proyecto Backend SGI (ASIO). Se trata de un proceso batch configurado mediante Spring Batch.

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11
* Eclipse JEE 2019-09 con plugins:
  * Spring Tools 4
  * m2e-apt
  * Lombok
* Docker

## Metodología de desarrollo

La metodología de desarrollo es Git Flow.

## Entorno de desarrollo Docker

La inicialización de los elementos adicionales al entorno de desarrollo se realiza con docker. 

En el directorio docker-devenv se ha configurado un fichero docker-compose.yml para poder arrancar el entorno de desarrollo.

Para arrancar el entorno:

```bash
docker-compose up -d
```

Para pararlo:

```bash
docker-compose down
```

## Jobs disponibles

Se han configurado los siguientes Jobs:

- `importDataSetJob`: job encargado de procesar datos a partir de los XML del dataset
- `importCvnJob`: job encargado de procesar CVN a partir de los servicios web

Estos jobs se encargan de leer los datos correspondientes, generar un JSON con los datos y posteriormente insertarlo en un topic de Kafka.

##  Parámetros de configuración

* `app.kafka.input-topic-name`: Nombre del topic para los datos de entrada. Valor por defecto: input-data
* `app.data.path`:Directorio en el que se encuentran los XML para la carga de datos, en caso de estar vacío se tomarán del classpath. Valor por defecto vacío
* `spring.kafka.bootstrap-servers`: Dirección del servidor bootstrap de Kafka. Valor por defecto: localhost:29092
* `app.services.cvn.endpoint`: Dirección del servidor para obtener los CVN. Valor por defecto: http://curriculumpruebas.um.es/curriculum/rest/v1/auth
* `app.services.cvn.mockup.enabled`: Booleano para indicar si se utilizan servicios mock para obtener los CVN. Valor por defecto: true
* `app.services.input-processor.endpoint`: Dirección del servidor para obtener los datos de importaciones anteriores. Valor por defecto: localhost:9322
* `app.services.input-processor.mockup.enabled`: Booleano para indicar si se utilizan servicios mock para obtener los resultados de las importaciones anteriores. Valor por defecto: true

## Cómo crear un nuevo Job

Para la creación de Jobs, se deben seguir las instrucciones provistas en la documentación de Spring Batch.

## Ejecución selectiva de jobs

Si se crea un solo empaquetado con varios jobs y solamente se quiere ejecutar uno en cada ejecución, se puede hacer pasando el siguiente parametro a la máquina virtual:

	-Dspring.batch.job.names=job1,job2
	
## Configuración en entornos de (pre)producción

Para la configuración de la ejecución periodica de jobs, se utilizarán las herramientas proporcionadas por los sistemas operativos Windows / Linux en el que se ejecute la aplicación.

Simplemente habría que ordenarle ejecutar el comando necesario. Por ejemplo:

```bash
java -jar -Dspring.batch.job.names=importCvnJob {jar-name}.jar
```

Sustituyendo `{jar-name}` por el nombre del fichero JAR generado.

No es necesario especificar la clase de inicio de la aplicación, ya que el fichero MANIFEST.MF generado ya contiene la información necesaria. Solamente se especificarán los parametros necesarios.

En entornos más complejos, se pueden usar gestores de cron como por ejemplo JobScheduler: https://www.sos-berlin.com/jobscheduler

## Instalación en entorno real

Será preciso configurar las siguientes variables de entorno cuando se instale en un entorno real:

|Variable|Descripción|Valor por defecto|
|---|---|---|
|`APP_PERSISTENCE_DATASOURCE_USERNAME`|Nombre del usuario de acceso a la base de datos| |
|`APP_PERSISTENCE_DATASOURCE_PASSWORD`|Contraseña del usuario de acceso a la base de datos| |
|`APP_PERSISTENCE_DATASOURCE_URL`|URL de acceso a la base de datos MySQL|jdbc:mysql://localhost:3306/asio_jobs?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8|
|`APP_KAFKA_INPUT_TOPIC_NAME`|Nombre del topic de Kafka de entrada|input-data|
|`APP_KAFKA_CREATE_TOPICS`|Flag que indica si debe crear automáticamente los topics de Kafka. Valores admisibles `true` y `false`|false|
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | URL del servicio de Kafka para los productores | localhost:29092 |
|`APP_DATA_PATH`|Ubicación de los ficheros XML de entrada. En caso de estar vacío se toman del classpath| |
| `SPRING_BATCH_INITIALIZE_SCHEMA` | Indica si se deben inicializar los esquemas de Spring Batch. Valores admisibles: `always` y `never` | never |
|`APP_SERVICES_CVN_ENDPOINT`| URL del servicio para obtener CVN | http://curriculumpruebas.um.es/curriculum/rest/v1/auth |
|`APP_SERVICES_CVN_MOCKUP_ENABLED`| Indica si se deben utilizar servicios mock para obtener los CVN. Valores admisibles: `true` y `false` | true |
|`APP_SERVICES_INPUTPROCESSOR_ENDPOINT`| URL del servicio para obtener datos de importaciones anteriores | localhost:9322 |
|`APP_SERVICES_INPUTPROCESSOR_MOCKUP_ENABLED`| Indica si se deben utilizar servicios mock para obtener datos de importaciones anteriores. Valores admisibles: `true` y `false` | true |

## Testing y cobertura

Se incluyen los resultados del testing y cobertura en los siguientes enlaces:

* [Testing](http://herc-iz-front-desa.atica.um.es:8070/dataset-importer/surefire/surefire-report.html)
* [Cobertura](https://sonarcloud.io/component_measures?id=HerculesCRUE_ib-dataset-importer&metric=coverage&view=list)

##  Documentación adicional

* [Compilación](docs/build.md)
* [Generación Docker](docs/docker.md)
