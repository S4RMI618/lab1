# 📌 Laboratorio 1 – Programación Orientada a Objetos II

Creación de un **API REST** con **Spring Boot**, **MySQL**, **JPA** y **Maven**.

## 🚀 Tecnologías utilizadas
- Java 21
- Spring Boot 3.5.x
- Spring Data JPA
- Spring Web
- Spring Rest Repositories
- MySQL
- Maven

---

## 📂 Estructura del proyecto
```
src/main/java/com/laboratorio1
 ├── controller
 │    └── PersonaController.java
 ├── model
 │    └── Persona.java
 ├── repository
 │    └── PersonaRepository.java
 └── services
      ├── Interfaces.java
      |     └── IPersonaService.java
      └── PersonaServiceImpl.java
```
`src/main/resources/application.properties` debe existir.

---

## 🛠️ Pasos para ejecutar el proyecto

### 1) Crear la base de datos en MySQL
```sql
CREATE DATABASE ppooii;

USE ppooii;

CREATE TABLE persona (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  PNOMBRE VARCHAR(100) NOT NULL,
  EDAD INT DEFAULT NULL,
  PRIMARY KEY (ID)
);

-- Datos de prueba
INSERT INTO persona (PNOMBRE, EDAD) VALUES ('Carlos', 25), ('Ana', 30), ('Luis', NULL);
```

### 2) Dependencias (Spring Initializr / Eclipse)
Incluye:
- Spring Data JPA
- Spring Web
- MySQL Driver
- Rest Repositories

### 3) Configuración en `application.properties`
> Ajusta usuario y contraseña reales de tu MySQL.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ppooii?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect # opcional (Hibernate lo detecta)

server.port=8080
```

### 4) Código base

**Entidad `Persona`**
```java
package com.laboratorio1.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PNOMBRE", nullable = false)
    private String pnombre;

    @Column(name = "EDAD")
    private Integer edad;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPnombre() { return pnombre; }
    public void setPnombre(String pnombre) { this.pnombre = pnombre; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
}
```

**Repositorio**
```java
package com.laboratorio1.repository;

import com.laboratorio1.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {}
```

**Servicio**
```java
package com.laboratorio1.services;

import com.laboratorio1.entities.Persona;
import java.util.List;

public interface IPersonaService {
    List<Persona> findAll();
    Persona findById(Long id);
    Persona save(Persona persona);
    void delete(Long id);
}
```

**Implementación Servicio**
```java
package com.laboratorio1.services;

import com.laboratorio1.entities.Persona;
import com.laboratorio1.repository.PersonaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonaServiceImpl implements IPersonaService {

    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public List<Persona> findAll() { return personaRepository.findAll(); }

    @Override
    public Persona findById(Long id) { return personaRepository.findById(id).orElse(null); }

    @Override
    public Persona save(Persona persona) { return personaRepository.save(persona); }

    @Override
    public void delete(Long id) { personaRepository.deleteById(id); }
}
```

**Controlador**
```java
package com.laboratorio1.controller;

import com.laboratorio1.entities.Persona;
import com.laboratorio1.services.IPersonaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    private final IPersonaService personaService;

    public PersonaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> listar() { return personaService.findAll(); }

    @GetMapping("/{id}")
    public Persona buscar(@PathVariable Long id) { return personaService.findById(id); }

    @PostMapping
    public Persona crear(@RequestBody Persona persona) { return personaService.save(persona); }

    @PutMapping("/{id}")
    public Persona actualizar(@PathVariable Long id, @RequestBody Persona persona) {
        Persona existente = personaService.findById(id);
        if (existente != null) {
            existente.setPnombre(persona.getPnombre());
            existente.setEdad(persona.getEdad());
            return personaService.save(existente);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { personaService.delete(id); }
}
```

### 5) Ejecutar
```bash
mvn spring-boot:run
```
Abrir: http://localhost:8080

---

## 📡 Endpoints

| Método | Endpoint         | Descripción                    |
|-------:|------------------|--------------------------------|
| GET    | `/personas`      | Lista todas las personas       |
| GET    | `/personas/{id}` | Busca persona por ID           |
| POST   | `/personas`      | Crea nueva persona             |
| PUT    | `/personas/{id}` | Actualiza persona              |
| DELETE | `/personas/{id}` | Elimina persona                |

**Ejemplo JSON**
```json
{ "pnombre": "Juan", "edad": 40 }
```

---

## 🧯 Troubleshooting (errores comunes)

### ❗ `Access denied for user 'root'@'localhost' (using password: NO)`
Causa: Spring intenta conectarse sin contraseña. Soluciones:
1. Asegúrate de **poner tu contraseña** en `application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=TU_PASSWORD_REAL
   ```
2. Si `root` **no tiene contraseña**, pon la propiedad vacía explícitamente:
   ```properties
   spring.datasource.password=
   ```
   (y verifica que MySQL realmente permita root sin password).
3. **Mejor práctica:** Crea un usuario dedicado:
   ```sql
   CREATE USER 'lab'@'localhost' IDENTIFIED BY 'lab123';
   GRANT ALL PRIVILEGES ON ppooii.* TO 'lab'@'localhost';
   FLUSH PRIVILEGES;
   ```
   y en `application.properties`:
   ```properties
   spring.datasource.username=lab
   spring.datasource.password=lab123
   ```
4. Agrega parámetros útiles al URL:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ppooii?useSSL=false&allowPublicKeyRetrieval=true
   ```
5. Verifica la ruta del archivo: debe estar en `src/main/resources/application.properties`.
6. Si usas perfiles, asegúrate que no haya `application-<profile>.properties` pisando valores. En los logs verás “No active profile set” → se usa `application.properties` por defecto.

### ❗ Dialectos / Warnings de Hibernate
Puedes omitir `hibernate.dialect`; Hibernate elige automáticamente `MySQLDialect`.

### ❗ DB inexistente
Ejecuta:
```sql
CREATE DATABASE ppooii;
```

---

## ✅ Checklist final
- [ ] DB `ppooii` creada y tabla `persona` con datos de prueba.
- [ ] `application.properties` en `src/main/resources` con usuario/contraseña correctos.
- [ ] Proyecto compila sin errores y levanta en `:8080`.
- [ ] Endpoints CRUD responden.

---

> _Este README está listo para copiar/pegar en tu repo del laboratorio._
