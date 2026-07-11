# Sistema de Gestión de Biblioteca

Proyecto personal desarrollado para practicar y demostrar los fundamentos de la Programación Orientada a Objetos en Java, junto con persistencia de datos en una base NoSQL (MongoDB).

## 🎯 Objetivo del proyecto

Simular el funcionamiento básico de una biblioteca: registrar socios, cargar libros, gestionar préstamos y devoluciones, y controlar reglas de negocio (deudas, disponibilidad, fechas), aplicando buenas prácticas de diseño orientado a objetos.

## 🧠 Conceptos de POO aplicados

| Concepto | Dónde se aplica |
|---|---|
| **Herencia** | `Persona` (clase abstracta) → `Socio` y `Bibliotecario` |
| **Interfaces** | `Prestable`, implementada por `Libro` |
| **Composición / Asociación** | `Prestamo` conecta `Socio` y `Libro` |
| **Excepciones custom** | `LibroNoDisponibleException`, `SocioConDeudaException` |
| **Encapsulamiento** | Atributos privados, validaciones centralizadas en `ValidadorUtils` |
| **Colecciones y Comparator** | Ordenamiento de libros por título y socios por deuda |
| **Manejo de fechas** | `LocalDate` para fechas de préstamo, límite y control de vencimiento |

## 🗂️ Estructura de clases

```
Persona (abstract)
 ├── Socio        (dni, dirección, teléfono, deuda)
 └── Bibliotecario (credencial)

Prestable (interface)
 └── Libro implementa Prestable

Prestamo
 └── referencia a Socio + Libro (clase asociativa)

Biblioteca
 └── orquesta listas de Socios, Libros y Préstamos
```

## 📦 Organización del código

El proyecto está separado en paquetes, siguiendo una organización típica de proyectos Java:

```
com.enzo.biblioteca
├── Main.java          → punto de entrada
├── modelo/            → Persona, Socio, Bibliotecario, Libro, Prestable, Prestamo
├── excepciones/        → LibroNoDisponibleException, SocioConDeudaException
├── util/              → ValidadorUtils
├── db/                → ConexionMongo
└── servicio/          → Biblioteca (orquesta el modelo)
```

## 🛢️ Persistencia

Los datos de los socios se guardan en **MongoDB Atlas**, usando el driver oficial de Java (`mongodb-driver-sync`). El proyecto implementa el CRUD completo:

- **Create / Update**: vía `upsert` por `dni` — si el socio ya existe, se actualiza; si no, se crea. Esto evita duplicados aunque el programa se ejecute varias veces.
- **Read**: búsqueda de todos los socios, o de uno puntual por `dni`
- **Delete**: eliminación de un documento

La cadena de conexión (`MONGO_URI`) se maneja como variable de entorno del sistema, nunca hardcodeada en el código ni subida al repositorio.

## 🛠️ Tecnologías

- Java 21
- Maven
- MongoDB Atlas + MongoDB Java Driver

## 🚧 Estado del proyecto

Funcional: todo el modelo de POO, las validaciones y el CRUD contra MongoDB (con upsert) están probados y funcionando. Próximos pasos posibles: migrar también `Libro` y `Prestamo` a MongoDB, y sumar una interfaz (consola o web) para interactuar sin tener que editar el código.

## ✍️ Autor

Enzo Gomez — Estudiante de la Tecnicatura en Programación Informática (UTN)
