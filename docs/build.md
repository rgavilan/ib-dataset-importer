# Compilación

Se indicará a continuación los pasos que hay que seguir para llevar a cabo la generación del artefacto.

## Prerrequisitos

Se precisa disponer los siguientes elementos configurados:

* OpenJDK 11
* Maven 3.6.x

## Compilación

Para realizar la compilación se ejecutará el siguiente comando:

```bash
mvn clean package
```

En caso de querer generar al mismo tiempo JavaDoc y Sources el comando siguiente: 

```bash
mvn clean package javadoc:jar source:jar
```

También sería posible instalar o desplegar los artefactos sustituyendo `package` por `install` o `deploy` respectivamente.

Los artefactos se generarán dentro del directorio `target`:

* Artefacto: dataset-importer-{version}.jar
* JavaDoc: dataset-importer-{version}-javadoc.jar
* Sources: dataset-importer-{version}-sources.jar
