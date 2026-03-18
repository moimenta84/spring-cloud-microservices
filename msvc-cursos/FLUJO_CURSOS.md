# msvc-cursos — Flujo de CourserServiceImpl

## Arquitectura general

```
HTTP Request
     │
     ▼
CourseController          (REST layer — expone los endpoints)
     │
     ▼
CourserServiceImpl        (Business logic)
     │
     ├──► CourseRepository ──────► Base de datos MySQL
     │         (JPA)                  tabla: courses
     │                                tabla: courses_users
     │
     └──► UserClientRest ─────────► msvc-users :8001
               (Feign)                GET  /{id}   → buscar usuario
                                      POST /       → crear usuario
```

---

## Operaciones CRUD locales

Estas operaciones solo tocan la base de datos de msvc-cursos.

| Método       | HTTP interno     | Qué hace                          |
|--------------|------------------|-----------------------------------|
| `index()`    | GET  /           | Devuelve todos los cursos         |
| `byId(id)`   | GET  /{id}       | Devuelve un curso por ID          |
| `save()`     | POST / PUT       | Inserta o actualiza un curso      |
| `delete(id)` | DELETE /{id}     | Elimina un curso por ID           |

Todas usan `@Transactional`. Las de lectura llevan `readOnly = true`
para optimizar el rendimiento (no genera snapshot de escritura).

---

## Operaciones entre microservicios (Feign)

Estas operaciones coordinan msvc-cursos y msvc-users.
msvc-cursos NUNCA guarda datos del usuario, solo su ID en `courses_users`.

---

### assignUser(user, courseId) — Asignar usuario existente

```
1. repository.findById(courseId)
        │
        ├── NOT FOUND ──► return Optional.empty()
        │
        └── FOUND
              │
              ▼
        client.show(user.getId())     ← GET localhost:8001/{id}
              │
              ▼
        new CoursesUsers()
        coursesUsers.setUserId(usuarioMsvc.getId())
              │
              ▼
        course.addCourseUser(coursesUsers)
        repository.save(course)       ← INSERT en courses_users
              │
              ▼
        return Optional.of(usuarioMsvc)
```

---

### createUser(user, courseId) — Crear usuario nuevo y asignarlo

```
1. repository.findById(courseId)
        │
        ├── NOT FOUND ──► return Optional.empty()
        │
        └── FOUND
              │
              ▼
        client.store(user)            ← POST localhost:8001/
              │                          (crea el usuario en msvc-users)
              ▼
        new CoursesUsers()
        coursesUsers.setUserId(usuarioNewMsvc.getId())
              │
              ▼
        course.addCourseUser(coursesUsers)
        repository.save(course)       ← INSERT en courses_users
              │
              ▼
        return Optional.of(usuarioNewMsvc)
```

---

### desAssignUser(user, courseId) — Desasignar usuario

```
1. repository.findById(courseId)
        │
        ├── NOT FOUND ──► return Optional.empty()
        │
        └── FOUND
              │
              ▼
        client.show(user.getId())     ← GET localhost:8001/{id}
              │                          (verifica que el usuario existe)
              ▼
        new CoursesUsers()
        coursesUsers.setUserId(usuarioMsvc.getId())
              │
              ▼
        course.removeCourseUser(coursesUsers)   ← usa equals() por userId
        repository.save(course)                 ← DELETE en courses_users
              │
              ▼
        return Optional.of(usuarioMsvc)
```

---

## Tabla courses_users — Por qué existe

msvc-cursos y msvc-users son microservicios independientes con bases de datos separadas.
No se puede hacer un JOIN entre ellas.

La solución es:
- `courses_users` guarda solo el `user_id` (referencia al usuario de msvc-users)
- Cuando se necesitan los datos del usuario, se consulta msvc-users en tiempo real via Feign
- El campo `users` en `Course` es `@Transient`: se rellena en runtime, no se persiste

---

## CoursesUsers.equals() — Por qué importa

`removeCourseUser()` usa `List.remove()` internamente, que llama a `equals()`.
Por eso `equals()` compara por `userId` y no por referencia de objeto:

```java
// Si no hubiera equals() personalizado, esto no encontraría el registro
// y el remove() no funcionaría correctamente.
course.removeCourseUser(coursesUsers);
```
