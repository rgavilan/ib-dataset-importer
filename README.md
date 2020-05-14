# ASIO - Importador de datos del DataSet de Murcia

Importador de datos del DataSet de Murcia para el proyecto Backend SGI (ASIO). Se trata de un proceso batch configurado mediante Spring Batch. 

## Jobs disponibles

Se han configurado los siguientes Jobs:

- `importDataSetJob`: job encargado de procesar datos a partir de los XML del dataset
- `importCvnJob`: job encargado de procesar CVN a partir de los servicios web

Estos jobs se encargan de leer los datos correspondientes, generar un JSON con los datos y posteriormente insertarlo en un topic de Kafka.

## OnBoarding

Para iniciar el entorno de desarrollo se necesita cumplir los siguientes requisitos:

* OpenJDK 11 (en caso de querer JDK8: Oracle JDK 8)
* Eclipse JEE 2019-09 con plugins:
** Spring Tools 4
** m2e-apt
** Lombok
* Docker

##  Parámetros de configuración

- app.kafka.input-topic-name: Nombre del topic para los datos de entrada. Valor por defecto: input-data
- app.data.path:Directorio en el que se encuentran los XML para la carga de datos, en caso de estar vacío se tomarán del classpath. Valor por defecto vacío
- spring.kafka.bootstrap-servers: Dirección del servidor bootstrap de Kafka. Valor por defecto: localhost:29092
- app.services.cvn.endpoint: Dirección del servidor para obtener los CVN. Valor por defecto: http://curriculumpruebas.um.es/curriculum/rest/v1/auth
- app.services.cvn.mockup.enabled: Booleano para indicar si se utilizan servicios mock para obtener los CVN. Valor por defecto: true
- app.services.input-processor.endpoint: Dirección del servidor para obtener los datos de importaciones anteriores. Valor por defecto: localhost:9322
- app.services.input-processor.mockup.enabled: Booleano para indicar si se utilizan servicios mock para obtener los resultados de las importaciones anteriores. Valor por defecto: true

## Cómo crear un nuevo Job

Para la creación de Jobs, se deben seguir las instrucciones provistas en la documentación de Spring Batch.

## Ejecución selectiva de jobs

Si se crea un solo empaquetado con varios jobs y solamente se quiere ejecutar uno en cada ejecución, se puede hacer pasando el siguiente parametro a la máquina virtual:

	-Dspring.batch.job.names=job1,job2
	
## Configuración en entornos de (pre)producción

Para la configuración de la ejecución periodica de jobs, se utilizarán las herramientas proporcionadas por los sistemas operativos Windows / Linux en el que se ejecute la aplicación.

Simplemente habría que ordenarle ejecutar el comando necesario. Por ejemplo:

	java -jar -Dspring.batch.job.names=importCvnJob dataset-importer-1.0-SNAPSHOT.jar
	
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
