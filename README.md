# P5
Aplicación web que usa Spring JPA para persistir los datos de un API REST de gestión de usuarios.
El API permite el registro de nuevos usuarios y su identificación mediante email y password.
Una vez identificados, se emplea una cookie de sesión para autenticar las peticiones que permiten 
a los usuarios leer, modificar y borrar sus datos. También existe un endpoint para cerrar la sesión.  

## Endpoints

// TODO#1: rellena la tabla siguiente analizando el código del proyecto

## Endpoints

| Método | Ruta                   | Descripción                                               | Respuestas                                                   |
|--------|------------------------|-----------------------------------------------------------|--------------------------------------------------------------|
| POST   | `/api/users`           | Registro de nuevo usuario                                 | `201 Created` si el registro es correcto<br>`409 Conflict` si el email ya existe |
| POST   | `/api/session`         | Login de usuario                                          | `200 OK` con cookie `session` si login correcto<br>`401 Unauthorized` si credenciales incorrectas |
| GET    | `/api/users`           | Obtener perfil del usuario autenticado                    | `200 OK` con `ProfileResponse`<br>`401 Unauthorized` si no autenticado |
| PUT    | `/api/users`           | Modificar perfil del usuario autenticado                  | `200 OK` con nuevo `ProfileResponse`<br>`401 Unauthorized` si no autenticado |
| DELETE | `/api/users`           | Eliminar usuario autenticado                              | `204 No Content` si éxito<br>`401 Unauthorized` si no autenticado |
| DELETE | `/api/session`         | Cerrar sesión (logout)                                    | `204 No Content`                                             |


## Comandos 

- Construcción: 
  ```sh
  ./mvnw clean package
  ```

- Ejecución: 
  ```sh
  ./mvnw spring-boot:run
  ```

- Tests:
  ```sh
  ./mvnw test
  ```
