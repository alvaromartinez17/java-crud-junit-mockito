# Task Manager API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-9.3+-blue.svg)](https://gradle.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-4.3.1-red.svg)](https://kafka.apache.org/)
[![JWT](https://img.shields.io/badge/JWT-0.11.5-purple.svg)](https://jwt.io/)
[![Tests](https://img.shields.io/badge/Tests-JUnit%205%20%2B%20Mockito-green.svg)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **API REST completa para gestión de tareas** con autenticación JWT, persistencia en MySQL, mensajería asíncrona con Apache Kafka y cobertura de tests unitarios.

---

## 🚀 Características principales

| Característica | Descripción |
|---------------|-------------|
| **CRUD de tareas** | Operaciones completas: crear, leer, actualizar, eliminar |
| **Autenticación JWT** | Login stateless con tokens firmados (HS256, expiración 1h) |
| **Seguridad por roles** | Endpoints públicos (`/auth/**`) y protegidos (resto) |
| **Persistencia JPA** | Spring Data JPA + Hibernate + MySQL |
| **Event-driven con Kafka** | Publicación de eventos JSON (`TAREA_CREADA`) al crear tareas |
| **Tests unitarios** | Capa de servicio cubierta con JUnit 5 + Mockito |
| **Configuración externalizada** | `application.properties` para topic, grupo, broker, BBDD |

---

## 🛠 Stack tecnológico

| Capa | Tecnología | Versión |
|------|------------|---------|
| **Lenguaje** | Java | 17 (LTS) |
| **Framework** | Spring Boot | 4.0.3 |
| **Web** | Spring Web MVC | — |
| **Datos** | Spring Data JPA / Hibernate | — |
| **Seguridad** | Spring Security + JWT (jjwt) | 0.11.5 |
| **Mensajería** | Spring Kafka (KRaft) | 4.0.3 |
| **BBDD** | MySQL | 8.0+ |
| **Build** | Gradle (Kotlin DSL compatible) | 9.3+ |
| **Tests** | JUnit 5, Mockito, Spring Boot Test | — |
| **Utilidades** | Lombok, SLF4J/Logback | — |

---

## 📁 Estructura del proyecto

```
src/
├── main/java/com/alvaro/taskmanager/
│   ├── config/          # KafkaConfig (topic, producer, consumer JSON)
│   ├── controller/      # TaskController, AuthController, KafkaController
│   ├── exception/       # Manejo global de errores (opcional)
│   ├── kafka/           # TaskEvent, UsuarioProducer, UsuarioConsumer
│   ├── model/           # Task (entidad JPA)
│   ├── repository/      # TaskRepository (Spring Data JPA)
│   ├── security/        # JwtFilter, JwtService, SecurityConfig, AuthController
│   └── service/         # TaskService (lógica de negocio + eventos Kafka)
└── test/java/com/alvaro/taskmanager/
    ├── security/        # JwtFilterTest
    └── service/         # TaskServiceTest
```

---

## 🔐 Autenticación y seguridad

### Flujo JWT

```mermaid
sequenceDiagram
    participant C as Cliente
    participant A as Auth Server
    participant R as Resource Server

    C->>A: POST /auth/login\n{username, password}
    A-->>C: 200 OK\n{token: "eyJhbGciOiJIUzI1NiJ9..."}
    
    C->>R: GET /tasks\nAuthorization: Bearer <token>
    R-->>C: 200 OK\n[tasks...]
```

### Configuración

```properties
# application.properties
spring.security.user.name=alvaro
spring.security.user.password=1234

# JWT (JwtService)
# Clave: 123456789123456789123456789123456789123456789
# Expiración: 1 hora
```

### Endpoints públicos vs protegidos

| Ruta | Acceso |
|------|--------|
| `POST /auth/login` | Público |
| `GET /auth/test` | Público |
| `GET /tasks` | **Protegido** (JWT requerido) |
| `POST /tasks` | **Protegido** |
| `PUT /tasks/{id}` | **Protegido** |
| `DELETE /tasks/{id}` | **Protegido** |
| `POST /kafka` | **Protegido** |

---

## 📨 Integración con Apache Kafka

### Arquitectura event-driven

```
┌─────────────┐     TaskEvent (JSON)      ┌─────────────┐
│ TaskService │ ────────────────────────▶ │  usuarios   │
│  (Producer) │                           │   -topic    │
└─────────────┘                           └──────┬──────┘
                                                  │
                                                  ▼
                                         ┌─────────────────┐
                                         │ UsuarioConsumer │
                                         │  (Listener)     │
                                         └─────────────────┘
```

### Evento publicado al crear tarea

```json
{
  "tipo": "TAREA_CREADA",
  "tareaId": 6,
  "titulo": "Segunda tarea JSON",
  "descripcion": "Verificando flujo completo",
  "completada": false,
  "fecha": "2026-10-13T02:56:28.5450162"
}
```

### Configuración centralizada (`application.properties`)

```properties
# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Topic y grupo (usados por producer y consumer)
app.kafka.topic.usuarios=usuarios-topic
app.kafka.consumer-group=crud-group
```

### Tecnologías Kafka destacadas

- **Spring Kafka 4.0** → serializadores `JacksonJsonSerializer` / `JacksonJsonDeserializer` (no deprecados)
- **KRaft mode** (sin ZooKeeper) para desarrollo local
- **Topic auto-creado** al arrancar (`NewTopic` bean)
- **Consumer group** `crud-group` con `auto.offset.reset=earliest`
- **Trusted packages** configurados con wildcard: `com.alvaro.taskmanager.*`

---

## 🧪 Tests unitarios

Cobertura de la capa de servicio (`TaskService`) con **JUnit 5 + Mockito**:

| Test | Qué verifica |
|------|--------------|
| `shouldReturnTask_whenIdExists` | `getTaskById` devuelve tarea existente |
| `shouldReturnNull_whenIdNotExists` | `getTaskById` devuelve `null` si no existe |
| `shouldReturnAllTasks` | `getAllTasks` lista completa / vacía |
| `shouldCreateTask` | `createTask` guarda y devuelve ID asignado |
| `shouldUpdateTask_whenExists` | `updateTask` actualiza campos y persiste |
| `shouldReturnNull_whenUpdateNotExists` | `updateTask` devuelve `null` si no existe |
| `shouldDeleteTask` | `deleteTask` llama a `repository.deleteById` |

### Ejecutar tests

```bash
# Linux/macOS
./gradlew test

# Windows
.\gradlew.bat test
```

### Test de filtro JWT (`JwtFilterTest`)

Verifica el comportamiento del filtro de seguridad:

- Token válido → autentica y setea `SecurityContextHolder`
- Header ausente → continúa sin autenticar
- Token inválido → limpia contexto y continúa

---

## ⚙️ Configuración y ejecución

### Prerrequisitos

- **Java 17+** (verificado con Temurin 17.0.18)
- **MySQL 8.0+** corriendo en `localhost:3306`
- **Apache Kafka 4.3.1** (KRaft) en `localhost:9092`

### Base de datos

```sql
CREATE DATABASE taskmanager_db;
CREATE USER 'taskmanager'@'localhost' IDENTIFIED BY 'taskmanager123';
GRANT ALL PRIVILEGES ON taskmanager_db.* TO 'taskmanager'@'localhost';
FLUSH PRIVILEGES;
```

### Kafka local (sin Docker)

```bash
# 1. Descargar Kafka 4.3.1
# https://downloads.apache.org/kafka/4.3.1/kafka_2.13-4.3.1.tgz

# 2. Formatear almacenamiento KRaft (una sola vez)
bin/windows/kafka-storage.bat format -t <UUID> --standalone -c config/server.properties

# 3. Arrancar broker
bin/windows/kafka-server-start.bat config/server.properties
```

### Compilar y ejecutar

```bash
# Compilar + tests
./gradlew build

# Ejecutar aplicación (puerto 8082)
./gradlew bootRun
# o directamente:
java -jar build/libs/taskmanager-0.0.1-SNAPSHOT.jar
```

---

## 📡 Endpoints API

### Autenticación

| Método | Endpoint | Body | Respuesta |
|--------|----------|------|-----------|
| `POST` | `/auth/login` | `{"username":"alvaro","password":"1234"}` | `{"token":"<JWT>"}` |
| `GET` | `/auth/test` | — | `"FUNCIONA"` |

### Tareas (requieren `Authorization: Bearer <token>`)

| Método | Endpoint | Body | Respuesta |
|--------|----------|------|-----------|
| `GET` | `/tasks` | — | `[{id, title, description, completed}, ...]` |
| `GET` | `/tasks/{id}` | — | `{id, title, description, completed}` |
| `POST` | `/tasks` | `{title, description, completed}` | `{id, title, description, completed}` |
| `PUT` | `/tasks/{id}` | `{title, description, completed}` | `{id, title, description, completed}` |
| `DELETE` | `/tasks/{id}` | — | `204 No Content` |
| `GET` | `/tasks/test` | — | `"TASK OK"` |

### Kafka (testing)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/kafka/test` | Health check |
| `POST` | `/kafka` | Publica evento `TaskEvent` de prueba |

---

## 📝 Ejemplos de uso (HTTP)

### Login y obtener token

```http
POST http://localhost:8082/auth/login
Content-Type: application/json

{
  "username": "alvaro",
  "password": "1234"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbHZhcm8iLCJpYXQiOjE2OTcx..."
}
```

### Crear tarea (con JWT)

```http
POST http://localhost:8082/tasks
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "title": "Estudiar Spring Boot 4",
  "description": "Nueva versión con KRaft",
  "completed": false
}
```

**Respuesta (201):**
```json
{
  "id": 7,
  "title": "Estudiar Spring Boot 4",
  "description": "Nueva versión con KRaft",
  "completed": false
}
```

> Al crear la tarea, se publica automáticamente un evento `TAREA_CREADA` en el topic `usuarios-topic`.

### Listar tareas

```http
GET http://localhost:8082/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 🏗 Decisiones de arquitectura y detalles técnicos

### 1. Spring Boot 4 + Spring Kafka 4
- Migración de `JsonSerializer/JsonDeserializer` (deprecados) a `JacksonJsonSerializer/JacksonJsonDeserializer`
- Configuración de `TRUSTED_PACKAGES` con wildcard `com.alvaro.taskmanager.*` para permitir deserialización de `TaskEvent` en subpaquete `.kafka`

### 2. Seguridad stateless
- Sin sesiones HTTP (`SessionCreationPolicy.STATELESS`)
- CSRF desactivado (API REST, no formularios web)
- Filtro JWT antes de `UsernamePasswordAuthenticationFilter`

### 3. Separación de responsabilidades
- `TaskService`: lógica de negocio + publicación de eventos
- `UsuarioProducer`: abstracción del envío a Kafka
- `UsuarioConsumer`: escucha y procesa eventos (logging actual, extensible a notificaciones, email, etc.)

### 4. Configuración externalizada
- Topic, grupo y broker en `application.properties` → cero valores hardcoded
- `NewTopic` bean crea el topic automáticamente al arrancar

---

## 📦 Build y artefactos

```bash
# JAR ejecutable (fat jar con dependencias)
./gradlew bootJar
# → build/libs/taskmanager-0.0.1-SNAPSHOT.jar

# JAR plano (solo clases del proyecto)
./gradlew jar
# → build/libs/taskmanager-0.0.1-SNAPSHOT-plain.jar
```

---

## 🗺 Roadmap / Próximos pasos

- [ ] Tests de integración con Testcontainers (MySQL + Kafka)
- [ ] Manejo global de excepciones (`@ControllerAdvice`)
- [ ] Documentación OpenAPI/Swagger (`springdoc-openapi`)
- [ ] Eventos adicionales: `TAREA_ACTUALIZADA`, `TAREA_ELIMINADA`
- [ ] Consumer con procesamiento real (email, push, audit log)
- [ ] Dockerfile multi-stage + docker-compose.yml completo
- [ ] CI/CD con GitHub Actions (build, test, publish)

---

## 👨‍💻 Autor

**Álvaro Martínez**  
[GitHub](https://github.com/alvaromartinez17) · [LinkedIn](https://linkedin.com/in/alvaromartinez17)

---

## 📄 Licencia

Este proyecto está bajo licencia **MIT**. Ver [`LICENSE`](LICENSE) para más detalles.

---

> **Nota:** Este proyecto forma parte de mi portfolio personal como desarrollador Java/Spring Boot.  
> Si te resulta útil, ¡dale una ⭐ en GitHub!