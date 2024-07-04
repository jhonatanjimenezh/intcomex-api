# Intcomex API

## Descripción

Intcomex API es un microservicio desarrollado en Java utilizando Spring Boot, diseñado para manejar la gestión de categorías y productos en una base de datos. La API permite la creación, actualización, eliminación y consulta de categorías y productos, con capacidades de paginación y búsqueda.

## Características

- Crear, actualizar, eliminar y listar categorías.
- Crear, actualizar, eliminar y listar productos.
- Paginación de productos.
- Búsqueda de productos por ID y obtención de la imagen de la categoría asociada.
- Integración de tests unitarios y de integración para asegurar la calidad del código.
- Scripts en Postman para automatizar la creación de datos y pruebas.

## Requisitos

- Java 17
- Spring Boot 3.3.1
- Gradle 8
- Docker
- Postman (para la ejecución de los scripts de pruebas)

## Metodología y Principios de Desarrollo
- **Arquitectura Hexagonal**: Utilizada para separar las preocupaciones en capas claramente definidas, facilitando la escalabilidad y mantenimiento.
- **Desarrollo Guiado por Pruebas (TDD)**: Adoptado para garantizar que el diseño y desarrollo del software estén dirigidos por pruebas automatizadas.
- **Desarrollo Guiado por el Dominio (DDD)**: Empleando un enfoque centrado en el dominio para mejorar la claridad y la lógica del negocio.
- **Principios SOLID**: Seguidos para promover un diseño de software orientado a objetos limpio y mantenible.

## Instalación y Configuración

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu_usuario/intcomex-api.git
   cd intcomex-api
   ```

2. Configura el archivo de propiedades para pruebas en `src/main/resources/application-test.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:h2:mem:testdb
       driverClassName: org.h2.Driver
       username: sa
       password:
     h2:
       console:
         enabled: true
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
   ```

3. Construye el proyecto con Gradle:
   ```bash
   ./gradlew build
   ```

4. Ejecuta el proyecto:
   ```bash
   ./gradlew bootRun
   ```

## Despliegue con Docker

1. Construye la imagen Docker:
   ```bash
   docker build -t intcomex-api .
   ```

2. Ejecuta el contenedor:
   ```bash
   docker run -d -p 8080:8080 intcomex-api
   ```

## Ejecución de Pruebas

### Pruebas Unitarias

Para ejecutar las pruebas unitarias, utiliza el siguiente comando:
```bash
./gradlew test
```

### Pruebas de Integración

Las pruebas de integración están configuradas para ejecutarse automáticamente con Gradle. Puedes ejecutar todas las pruebas (unitarias e integración) con:
```bash
./gradlew integrationTest
```

## Uso de Postman

Se ha configurado una colección de Postman para automatizar la creación de datos y pruebas.

### Importación de la Colección

1. Abre Postman.
2. Ve a `File -> Import`.
3. Selecciona el archivo `intcomex.postman_collection.json` proporcionado en este repositorio.
4. La colección `intcomex` aparecerá en tus colecciones de Postman.

### Ejecución de los Scripts

1. En Postman, selecciona la colección `intcomex`.
2. Selecciona la carpeta `test` que está dentro de la colección `intcomex`.
3. Configuración `Run Folder`
3. Selecciona los requests en este orden `create_category_servidores`, `create_category_cloud`, `create_products_batch`, `send_product`, `create_product`.
4. Configura las iteraciones y otras opciones según tus necesidades.
5. Ejecuta la colección. Esto creará las categorías y 100,000 productos en lotes.

## Endpoints Principales

### Categorías

- **Crear Categoría**
    - `POST /intcomex-api/category`
    - Request Body:
      ```json
      {
        "name": "SERVIDORES",
        "imageUrl": "http://intcomex.s3.aws.com"
      }
      ```

- **Actualizar Categoría**
    - `PUT /intcomex-api/category/{id}`
    - Request Body:
      ```json
      {
        "name": "CLOUD",
        "imageUrl": "http://intcomex.s3.aws.com"
      }
      ```

- **Eliminar Categoría**
    - `DELETE /intcomex-api/category/{id}`

- **Listar Categorías**
    - `GET /intcomex-api/category`

### Productos

- **Crear Producto**
    - `POST /intcomex-api/product`
    - Request Body:
      ```json
      {
        "name": "Product 1",
        "price": 100,
        "category": {
          "id": 1
        }
      }
      ```

- **Actualizar Producto**
    - `PUT /intcomex-api/product/{id}`
    - Request Body:
      ```json
      {
        "name": "Updated Product",
        "price": 150,
        "category": {
          "id": 1
        }
      }
      ```

- **Eliminar Producto**
    - `DELETE /intcomex-api/product/{id}`

- **Listar Productos**
    - `GET /intcomex-api/product?page=0&size=10`

- **Buscar Producto por ID**
    - `GET /intcomex-api/product/{id}`

## Estructura del Proyecto

El proyecto sigue una arquitectura hexagonal para Spring Boot:

- **adapter:** Contiene las implementaciones de los puertos.
- **application:** Contiene los casos de uso y los puertos. 
- **domain:** Define los modelos de dominio.
- **config:** Contiene configuraciones y manejadores de excepciones.
