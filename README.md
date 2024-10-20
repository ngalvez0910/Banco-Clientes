# Descripción del programa

En esta práctica, desarrollaremos un servicio bancario donde registraremos a los clientes con sus respectivos usuarios y tarjetas. Implementaremos diferentes técnicas de programación para garantizar la integridad y robustez del sistema, además de aplicar los principios SOLID.


## Tecnologías y Enfoques Utilizados

* Arquitectura orientada al dominio.
* Railway Oriented Programming (ROP).
* Manejo de errores orientados al dominio.
* Patrón repositorio con caché multinivel.
* Uso de inyección de dependencias.


## Base de Datos

Este proyecto utiliza tres bases de datos para gestionar la información de clientes y tarjetas de crédito:

- *Base de datos local (SQLite):* Se ocupará de gestionar los clientes. Su conexión se establecerá mediante un archivo *application.properties* y se utilizará el pool de conexiones *Hikari* para optimizar el acceso a la base de datos.
- *Base de datos remota (PostgreSQL con Docker):* Se ocupará de gestionar las tarjetas de crédito de dichos clientes. Su conexión se establecerá mediante un archivo *dockerCompose.yml*.
- *API REST:* Gestionará los usuarios de los clientes mediante el endpoint: *https://jsonplaceholder.typicode.com/*


## Importación y Exportación de Datos

El sistema permite la importación y exportación de datos para facilitar la gestión de la información de clientes y tarjetas en los siguientes formatos:

- *CSV*
- *JSON*


## Lenguajes y Tecnologías

- *Java*
- *PostgreSQL*
- *SQLite*
- *Docker*
- *TestContainers*
- *Jackson*
- *Apache Commons CSV*
- *WebFlux*
- *SLF4J con Logback*
- *JUnit*
- *Mockito*
- *Gradle*
- *Git*
- *GitFlow*
- *Postman*
- *Dagger*
- *Retrofit*
- *Hikari*


## Calidad y Pruebas

El proyecto implementa diversas prácticas y herramientas para asegurar la calidad y el correcto funcionamiento del código. A continuación se describen los principales enfoques utilizados:

- *Pruebas Unitarias*
- *Pruebas de Integración*
- *Mockito*
- *JUnit*
- *TestContainers*
- *Cobertura de Código*


## Enlace al video

*https://youtu.be/cUto9sCNOqo*


## Autores del programa

<table align="center">
  <tr>
    <td align="center">
      <a href="https://github.com/ngalvez0910">
        <img src="https://avatars.githubusercontent.com/u/145333876" width="70" height="70" style="border-radius: 50%;" alt="Natalia González Álvarez"/>
        <br/>
        <sub><b>Natalia</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/germangfc">
        <img src="https://avatars.githubusercontent.com/u/147338370" width="70" height="70" style="border-radius: 50%;" alt="German"/>
        <br/>
        <sub><b>German</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/jaimeleon10">
        <img src="https://avatars.githubusercontent.com/u/113149992" width="70" height="70" style="border-radius: 50%;" alt="Jaime León"/>
        <br/>
        <sub><b>Jaime</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Alba448">
        <img src="https://avatars.githubusercontent.com/u/146001599" width="70" height="70" style="border-radius: 50%;" alt="Alba García"/>
        <br/>
        <sub><b>Alba</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/wolverine307mda">
        <img src="https://avatars.githubusercontent.com/u/146002100" width="70" height="70" style="border-radius: 50%;" alt="Mario de Domingo Alvarez"/>
        <br/>
        <sub><b>Mario</b></sub>
      </a>
    </td>
  </tr>
</table>
