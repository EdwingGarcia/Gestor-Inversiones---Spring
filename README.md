# Spring Boot App: Inversiones

Este proyecto es una aplicación web desarrollada con Spring Boot que incluye un sistema de autenticación de usuarios (login) y un CRUD para gestionar inversiones y un registro de operativa en desarrollo (orientado a traders).

## Características

- **Login de usuarios**: Autenticación de usuarios con credenciales (correo electrónico y contraseña).
- **Registro de usuarios**: Creación de usuarios con su nombre,apellido email y contraseña encriptada.
- **CRUD de Inversiones**: Los usuarios autenticados pueden gestionar sus inversiones. Esto incluye:
  - Crear nuevas inversiones.
  - Consultar la lista de inversiones.
  - Actualizar inversiones existentes.
  - Eliminar inversiones.
  Además incluye una nueva funcionalidad  inicial para traders que busquen almacenar sus registros de operativa.

## Tecnologías

- **Backend**: Spring Boot
- **Persistencia**: JPA/Hibernate con MySQL
- **Seguridad**: Spring Security para el sistema de login
- **Frontend**: Thymeleaf como motor de plantillas HTML
- **Estilos**: Bootstrap para una UI simple y responsiva
- **Maven**: Gestión de dependencias


**Rutas del Controlador de Inversiones**
/inversiones/listar (GET): Muestra todas las inversiones del usuario autenticado en la vista index.
/inversiones/agregar (GET): Muestra el formulario para agregar una nueva inversión.
/inversiones/guardar (POST): Guarda una nueva inversión y redirige a la lista de inversiones.
/inversiones/editar/{id} (GET): Carga la inversión a editar y muestra el formulario de edición.
/inversiones/eliminar/{id} (GET): Elimina la inversión por su ID y redirige a la lista actualizada.

**Rutas del Controlador de Operativa**
/operativa/crear (GET): Muestra el formulario para crear una nueva operativa.
/operativa/guardar (POST): Guarda la operativa en la base de datos. Si hay errores en el formulario, vuelve a mostrar la vista para corregirlos. Después de guardar, redirige a la lista de inversiones.

**Rutas del Controlador de Registro**
/login (GET): Muestra la página de inicio de sesión para que los usuarios puedan autenticarse.

**Rutas del Controlador de Registro de Usuario**
/registro (GET): Muestra el formulario de registro para crear una nueva cuenta de usuario.
/registro (POST): Procesa el formulario de registro y guarda la nueva cuenta de usuario. Redirige al formulario con un mensaje de éxito si el registro es exitoso.

## Contribuciones
Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un "issue" o un "pull request".

## Contacto
Para consultas, puedes contactarme en [edwingnico@gmail.com](mailto:edwingnico@gmail.com).