# MedPharm Express - Laboratorio 13

Laboratorio 13 - Desarrollo de Software IV 
## Cómo correrlo

```
cd medpharm-backend
mvn spring-boot:run

cd medpharm-frontend
npm install
ng serve

```

Usuarios de prueba (clave `password123`): `medico1` y `farma1`.

## Parte 3: error 401

Para esta parte quité el `authInterceptor` de `app.config.ts` y probé cargar `GET /api/v1/recetas`. El servidor devolvió 401, como se ve en la captura adjunta en mv.

**Por qué falló:** el backend usa JWT y no guarda sesión, entonces en cada petición necesita el header Authorization: Bearer token. Sin el interceptor, Angular no lo mandó, aunque yo ya había hecho login y el token estaba en localStorage. El filtro AuthTokenFilter no encontró el token, no se autenticó a nadie y Spring Security respondió 401. No fue por CORS, porque el origen http://localhost:4200 ya estaba permitido.

**Cómo lo arregla el interceptor:** el authInterceptor agarra cada petición que sale hacia la API, saca el token con AuthService.getToken(), clona la petición (porque no se pueden modificar directamente) y le agrega el header Authorization: Bearer token. Con eso el backend valida el token y responde 200 con las recetas.