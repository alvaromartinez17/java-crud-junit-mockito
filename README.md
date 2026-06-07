#  Task Manager API

API REST para gestión de tareas desarrollada con Spring Boot.

##  Tecnologías usadas en el proyecto

- Java 17
- Spring Boot
- Spring Data JPA
- JUnit 5 + Mockito (tests unitarios)
- Gradle

## 📁 Estructura del proyecto

```
src/
├── main/java/com/alvaro/taskmanager/
│   ├── controller/    # Endpoints REST
│   ├── service/       # Lógica de negocio
│   ├── repository/    # Acceso a datos
│   ├── model/         # Entidades JPA
│   ├── dto/           # Objetos de transferencia
│   ├── exception/     # Manejo de errores
│   └── config/        # Configuración
└── test/java/com/alvaro/taskmanager/
    └── service/
        └── TaskServiceTest.java
```

##  Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/tasks` | Obtener todas las tareas |
| GET | `/tasks/{id}` | Obtener tarea por ID |
| POST | `/tasks` | Crear nueva tarea |
| PUT | `/tasks/{id}` | Actualizar tarea |
| DELETE | `/tasks/{id}` | Eliminar tarea |

## ✅ Tests unitarios

Los tests cubren la capa de servicio usando **Mockito** para simular
el repositorio y **JUnit 5** para las aserciones:

- `getAllTasks` — lista completa y lista vacía
- `getTaskById` — tarea existente y no existente
- `createTask` — creación correcta
- `updateTask` — actualización y tarea no encontrada
- `deleteTask` — llamada correcta al repositorio

Para ejecutarlos:

```bash
./gradlew test
```

##  Cómo ejecutar

```bash
./gradlew bootRun
```
