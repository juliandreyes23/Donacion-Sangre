# \# 🩸 Sistema de Donación de Sangre

# 

# Proyecto desarrollado para la gestión y administración de procesos relacionados con donaciones de sangre, control de donantes, inventario y autenticación de usuarios.

# 

# \---

# 

# \# 📌 Descripción del Proyecto

# 

# El sistema permite administrar información relacionada con:

# 

# \* Registro de donantes

# \* Gestión de donaciones

# \* Control de inventario de sangre

# \* Inicio de sesión y autenticación

# \* Administración de usuarios

# \* Gestión de reportes

# \* API REST para integración frontend/backend

# 

# El proyecto cuenta con:

# 

# \* Backend desarrollado con Spring Boot

# \* Frontend en HTML, CSS y JavaScript

# \* Base de datos MySQL

# \* Seguridad con JWT y Spring Security

# \* Documentación API con Swagger/OpenAPI

# \* Arquitectura organizada por capas

# 

# \---

# 

# \# 🚀 Tecnologías Utilizadas

# 

# \## Backend

# 

# \* Java 21

# \* Spring Boot 3

# \* Spring Security

# \* Spring Data JPA

# \* JWT Authentication

# \* Maven

# \* Lombok

# \* MapStruct

# \* Swagger/OpenAPI

# \* OpenPDF

# 

# \## Frontend

# 

# \* HTML5

# \* CSS3

# \* JavaScript

# 

# \## Base de Datos

# 

# \* MySQL

# 

# \## Herramientas

# 

# \* IntelliJ IDEA

# \* Visual Studio Code

# \* Git

# \* GitHub

# \* Docker

# 

# \---

# 

# \# 📂 Estructura del Proyecto

# 

# bash

# Sistema-Donacion-Sangre/

# │

# ├── src/

# │   ├── main/

# │   │   ├── java/

# │   │   ├── resources/

# │   │   └── templates/

# │   │

# │   └── test/

# │

# ├── pom.xml

# ├── Dockerfile

# ├── docker-compose.yml

# └── README.md

# 

# 

# \---

# 

# \# ⚙️ Configuración del Proyecto

# 

# \## 1. Clonar el repositorio

# 

# bash

# git clone https://github.com/juliandreyes23/Donacion-Sangre.git

# 

# 

# \---

# 

# \## 2. Abrir el proyecto

# 

# Puedes abrirlo en:

# 

# \* IntelliJ IDEA

# \* Visual Studio Code

# 

# \---

# 

# \## 3. Configurar MySQL

# 

# Crear una base de datos llamada:

# 

# sql

# CREATE DATABASE donacionsangre\_db;

# 

# 

# \---

# 

# \## 4. Configurar credenciales

# 

# Archivo:

# 

# bash

# src/main/resources/application.properties

# 

# 

# Configuración actual:

# 

# properties

# spring.datasource.url=jdbc:mysql://localhost:3306/donacionsangre\_db?useSSL=false\&serverTimezone=UTC

# spring.datasource.username=root

# spring.datasource.password=123456

# 

# 

# Modificar según tu configuración local.

# 

# \---

# 

# \# ▶️ Ejecutar el Proyecto

# 

# \## Desde IntelliJ IDEA

# 

# Ejecutar la clase principal:

# 

# bash

# SistemaDonacionSangreApplication

# 

# 

# \---

# 

# \## Desde terminal

# 

# bash

# ./mvnw spring-boot:run

# 

# 

# En Windows:

# 

# bash

# mvnw.cmd spring-boot:run

# 

# 

# \---

# 

# \# 🌐 Acceso al Sistema

# 

# \## Backend

# 

# bash

# http://localhost:8080

# 

# 

# \## Swagger API Docs

# 

# bash

# http://localhost:8080/swagger-ui/index.html

# 

# 

# \---

# 

# \# 🔐 Seguridad

# 

# El sistema implementa:

# 

# \* Spring Security

# \* Autenticación JWT

# \* Protección de rutas

# \* Manejo de usuarios y roles

# 

# \---

# 

# \# 📊 Funcionalidades Principales

# 

# \## 👤 Gestión de Donantes

# 

# \* Registro de donantes

# \* Consulta de información

# \* Actualización de datos

# \* Eliminación de registros

# 

# \## 🩸 Gestión de Donaciones

# 

# \* Registro de donaciones

# \* Historial de donaciones

# \* Relación entre donante y donación

# 

# \## 📦 Inventario de Sangre

# 

# \* Control de unidades disponibles

# \* Gestión por tipo sanguíneo

# \* Actualización de stock

# 

# \## 🔑 Autenticación

# 

# \* Inicio de sesión

# \* Generación de tokens JWT

# \* Protección de endpoints

# 

# \## 📄 Reportes

# 

# \* Generación de documentos PDF

# \* Reportes administrativos

# 

# \---

# 

# \# 🐳 Docker

# 

# El proyecto incluye:

# 

# \* Dockerfile

# \* docker-compose.yml

# 

# Para ejecutar con Docker:

# 

# bash

# docker-compose up --build

# 

# 

# \---

# 

# \# 📡 API REST

# 

# El backend expone endpoints REST para integración con el frontend.

# 

# Ejemplos:

# 

# http

# GET /api/donantes

# POST /api/donaciones

# PUT /api/inventario

# DELETE /api/donantes/{id}

# 

# 

# \---

# 

# \# 📷 Frontend

# 

# El frontend incluye páginas como:

# 

# \* Login

# \* Dashboard

# \* Donantes

# \* Donaciones

# \* Inventario

# 

# Archivos principales:

# 

# bash

# login.html

# dashboard.html

# Donantes.html

# Donaciones.html

# Inventario.html

# 

# 

# 

# 

# \# 🧪 Dependencias Importantes

# 

# \## Spring Boot

# 

# Framework principal para el desarrollo backend.

# 

# \## Spring Security

# 

# Implementación de autenticación y autorización.

# 

# \## JWT

# 

# Manejo de tokens seguros para autenticación.

# 

# \## Swagger/OpenAPI

# 

# Documentación automática de endpoints.

# 

# \## Lombok

# 

# Reducción de código repetitivo.

# 

# \## MapStruct

# 

# Conversión entre entidades y DTOs.

# 

# \---

# 

# \# 📈 Mejoras Futuras

# 

# \* Panel administrativo avanzado

# \* Notificaciones por correo

# \* Estadísticas y gráficos

# \* Integración con bancos de sangre

# \* Despliegue en la nube

# \* Responsive design mejorado

# 

# \---

# 

# \# 👨‍💻 Autor

# 

# \## Julian Reyes

# 

# Proyecto desarrollado como sistema de gestión de donación de sangre.

# 

# \---

# 

# 

# 

# 



