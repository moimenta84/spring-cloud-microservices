# Apuntes Microservicios

---

## 1. Maven

### ¿Qué es Maven?
Herramienta de gestión y construcción de proyectos Java. Gestiona dependencias y el ciclo de vida del proyecto.

### Estructura pom.xml
- **parent** → proyecto del que hereda configuración
- **modules** → módulos hijos del proyecto (multi-módulo)
- **dependencies** → librerías que usa el proyecto
- **build** → configuración de cómo se compila y empaqueta

### Comandos
| Comando | Descripción |
|---------|-------------|
| `mvn clean install` | Limpia, compila, testea y empaqueta |
| `mvn compile` | Solo compila el código fuente |
| `mvn package` | Genera el JAR/WAR |
| `mvn validate` | Valida que el proyecto es correcto |
| `mvn test` | Ejecuta los tests |

---

## 2. Spring Boot

### ¿Qué es Spring Boot?
Framework Java que simplifica la creación de microservicios y aplicaciones REST. Elimina gran parte de la configuración manual.

### application.properties
```properties
spring.application.name=nombre-del-servicio
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/bbdd
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

### Anotaciones importantes
| Anotación | Descripción |
|-----------|-------------|
| `@SpringBootApplication` | Clase principal, arranca la aplicación |
| `@RestController` | Clase que recibe peticiones HTTP |
| `@Service` | Clase con la lógica de negocio |
| `@Repository` | Clase que accede a la base de datos |
| `@Entity` | Clase que mapea una tabla de la BBDD |
| `@Autowired` | Inyección de dependencias |
| `@GetMapping` | Endpoint HTTP GET |
| `@PostMapping` | Endpoint HTTP POST |
| `@PutMapping` | Endpoint HTTP PUT |
| `@DeleteMapping` | Endpoint HTTP DELETE |

---

## 3. Spring Cloud

### ¿Qué es Spring Cloud?
Conjunto de herramientas para construir sistemas distribuidos y microservicios.

### Componentes principales
- **Feign** → comunicación entre microservicios de forma sencilla
- **Resilience4j** → circuit breaker, evita fallos en cascada entre servicios
- **Eureka** → registro y descubrimiento de servicios (cada micro se registra aquí)
- **Config Server** → configuración centralizada para todos los microservicios

---

## 4. Kubernetes

### ¿Qué es Kubernetes?
Plataforma para desplegar, escalar y gestionar contenedores Docker de forma automática.

### Comandos kubectl
| Comando | Descripción |
|---------|-------------|
| `kubectl get pods` | Lista los pods en ejecución |
| `kubectl get services` | Lista los servicios |
| `kubectl apply -f archivo.yaml` | Despliega un recurso desde un fichero |
| `kubectl delete -f archivo.yaml` | Elimina un recurso |
| `kubectl logs nombre-pod` | Ver logs de un pod |
| `kubectl describe pod nombre-pod` | Detalle de un pod |

### Estructura ficheros YAML
- **Deployment** → define cómo desplegar la aplicación y cuántas réplicas
- **Service** → expone la aplicación dentro o fuera del cluster
- **ConfigMap** → configuración externa de la aplicación

---

## 5. Docker

### ¿Qué es Docker?
Herramienta para empaquetar aplicaciones en contenedores, garantizando que funcionan igual en cualquier entorno.

### Comandos básicos
| Comando | Descripción |
|---------|-------------|
| `docker build -t nombre .` | Construye una imagen |
| `docker run -p 8080:8080 nombre` | Arranca un contenedor |
| `docker ps` | Lista contenedores en ejecución |
| `docker stop nombre` | Para un contenedor |
| `docker images` | Lista imágenes descargadas |
| `docker pull nombre` | Descarga una imagen |

### Estructura Dockerfile
```dockerfile
FROM openjdk:17
WORKDIR /app
COPY target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose
Permite levantar varios contenedores a la vez (ej: app + base de datos).

---

## 6. Git

### Flujo de trabajo
1. Crear rama desde main/develop
2. Desarrollar el cambio
3. Commit con mensaje descriptivo
4. Push a remoto
5. Crear Pull Request (PR)
6. Code review por un compañero
7. Merge a la rama principal

### Comandos
| Comando | Descripción |
|---------|-------------|
| `git clone url` | Clona el repositorio |
| `git pull` | Descarga los últimos cambios |
| `git checkout -b nombre-rama` | Crea y cambia a una nueva rama |
| `git add .` | Añade todos los cambios al stage |
| `git commit -m "mensaje"` | Guarda los cambios con un mensaje |
| `git push origin nombre-rama` | Sube los cambios al remoto |
| `git status` | Ver estado de los cambios |
| `git log` | Ver historial de commits |

---

## 7. Swagger

### ¿Qué es Swagger?
Herramienta que genera documentación visual e interactiva de la API REST automáticamente.

### URLs una vez arrancado el servicio
- Interfaz visual: `http://localhost:8080/swagger-ui.html`
- JSON de la API: `http://localhost:8080/v3/api-docs`

### Dependencia en pom.xml
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.6</version>
</dependency>
```

---

## 8. Errores y Soluciones

| Fecha | Error | Solución |
|-------|-------|----------|
|       |       |          |

---

## 9. Dailies y Forma de Trabajar

| Fecha | Compañero | Qué hace | Términos nuevos |
|-------|-----------|----------|-----------------|
|       |           |          |                 |

---

## 10. Pasos al Empezar en un Proyecto de Microservicios

1. **Entender el dominio** → qué hace cada microservicio, cómo se comunican, pedir diagrama de arquitectura
2. **Montar el entorno local** → clonar repo, instalar dependencias, levantar BBDD con Docker
3. **Arrancar los servicios** → orden: config server → Eureka → microservicios
4. **Entender la estructura del código** → controller → service → repository → entity
5. **Primer cambio pequeño** → crear rama, desarrollar, hacer PR
6. **Herramientas habituales** → Postman, Swagger, Docker, Git, Jira/Azure DevOps
