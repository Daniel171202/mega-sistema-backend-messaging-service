# Microservicio de Mensajería

## Descripción General

El Servicio de Mensajería es un microservicio componente de la arquitectura de backend de Mega Sistema que proporciona capacidades de mensajería vía Telegram a otros servicios. Este microservicio permite al sistema enviar mensajes a los usuarios a través de bots de Telegram, y también proporciona un mecanismo para que los usuarios registren sus cuentas de Telegram con el sistema mediante una interfaz de bot.

## Características

- Envío de mensajes a usuarios individuales a través de Telegram
- Envío de mensajes por lotes a múltiples usuarios simultáneamente
- Registro de IDs de Telegram de los usuarios con sus cuentas mediante un bot de Telegram
- Integración con Pocketbase para almacenamiento y recuperación de datos de usuarios
- Descubrimiento de servicios a través de Eureka

## Arquitectura

El servicio está construido con:
- Spring Boot 3.4.4
- Spring Cloud (Cliente Eureka)
- Spring WebFlux para programación reactiva
- SDK Drednote Telegram Bot para integración con Telegram
- Java 17

## Requisitos Previos

Para ejecutar este servicio, necesitas:

- JDK 17+
- Maven 3.6+ (o usa el wrapper mvnw incluido)
- Token de API de Bot de Telegram (crea uno a través de [@BotFather](https://t.me/botfather))
- Instancia de Pocketbase en ejecución
- Registro de servicios Eureka en ejecución (opcional, puede deshabilitarse)

## Configuración

Crea un archivo `.env` en el directorio raíz con las siguientes variables:

```
TOKEN_TELEGRAM_BOT=tu_token_de_bot_telegram
POCKETBASE_URL=http://tu-instancia-pocketbase:8090
POCKETBASE_API_KEY=tu_clave_api_pocketbase
```

Alternativamente, puedes proporcionar estas variables como variables de entorno o argumentos JVM.

## Integración con Pocketbase

### Estructura de la Base de Datos

El servicio espera una colección en Pocketbase llamada `usuarios` con los siguientes campos:
- `id` (string): Identificador único para el usuario
- `email` (string): Dirección de correo electrónico del usuario
- `telegramId` (número): ID de Telegram del usuario
- `rol` (string): Rol del usuario (por ejemplo, "docente", "estudiante")

### Detalles de Conexión

El servicio se conecta a Pocketbase utilizando:
- Un WebClient configurado en `MessagingConfig.java`
- Autenticación mediante API key a través del encabezado `Authorization`
- El servicio `PocketbaseClient.java` que proporciona métodos para interactuar con Pocketbase

Las siguientes operaciones se realizan en Pocketbase:
1. Buscar el ID de Telegram de un usuario por su identificador único
2. Actualizar el ID de Telegram de un usuario cuando se registra con el bot
3. Verificar los permisos del usuario basándose en su rol

## Ejecución del Servicio

### Usando Maven

```bash
# Usando Maven local
mvn spring-boot:run

# O usando el wrapper de Maven
./mvnw spring-boot:run
```

### Usando Docker (ejemplo de Dockerfile)

```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

Compilar y ejecutar:
```bash
mvn clean package
docker build -t messaging-service .
docker run -p 8018:8018 --env-file .env messaging-service
```

## Endpoints de API

### API de Telegram

- `POST /api/telegram/send`: Enviar un mensaje a un solo usuario
  ```json
  {
    "id": "id_usuario_de_pocketbase",
    "text": "Contenido del mensaje"
  }
  ```

- `POST /api/telegram/sendBatch`: Enviar un mensaje a múltiples usuarios
  ```json
  {
    "id": ["id_usuario_1", "id_usuario_2", "id_usuario_3"],
    "text": "Contenido del mensaje"
  }
  ```

## Comandos del Bot de Telegram

El microservicio registra un bot de Telegram con los siguientes comandos:

- `/inicio`: Da la bienvenida al usuario
- `/inicio {email}`: Vincula la cuenta de Telegram del usuario con su email en el sistema
- `/ayuda`: Muestra los comandos disponibles

## Descubrimiento de Servicios

El servicio se registra con el registro de servicios Eureka, haciéndolo descubrible por otros servicios en el sistema. La configuración de Eureka se encuentra en `application.properties`.

## Desarrollo y Pruebas

### Compilación del Proyecto

```bash
mvn clean install
```

### Ejecución de Pruebas

```bash
mvn test
```

### Generación de Documentación

```bash
mvn javadoc:javadoc
```

## Solución de Problemas

### Problemas Comunes

1. **No se puede conectar a Pocketbase**
   - Verifica que la URL de Pocketbase sea correcta y accesible
   - Verifica que la clave API tenga permisos suficientes

2. **El bot de Telegram no responde**
   - Asegúrate de que la variable de entorno TOKEN_TELEGRAM_BOT esté configurada correctamente
   - Verifica que el bot tenga los permisos necesarios (modo de privacidad desactivado)

3. **El servicio no se registra con Eureka**
   - Verifica que el servidor Eureka esté en funcionamiento
   - Comprueba la conectividad de red al servidor Eureka

## Licencia

PENDIENTE

## Contribuir

PENDIENTE

## Autores

Daniel Aldazosa