# Pedidos360 - Frontend

Aplicación Angular standalone protegida con Microsoft Authentication Library (`@azure/msal-angular`). Implementa inicio y cierre de sesión, `MsalGuard`, guard por rol, adquisición silenciosa de tokens, lectura de roles/scopes y `MsalInterceptor` para adjuntar el JWT al BFF/API Gateway.

## Configuración de Microsoft Entra ID

### 1. Registro de la API

1. Crear un App Registration llamado `Pedidos360 API`.
2. En **Expose an API**, usar `api://<API_CLIENT_ID>` como Application ID URI.
3. Crear el scope delegado `Pedidos.ReadWrite`.
4. Crear los App Roles `Pedidos.Admin` y `Pedidos.Operador`, permitidos para usuarios/grupos.

### 2. Registro del frontend

1. Crear un App Registration llamado `Pedidos360 Frontend`.
2. Agregar plataforma **Single-page application**.
3. Registrar `http://localhost:4200` y la URL productiva como redirect URI y logout URI.
4. En **API permissions**, agregar el permiso delegado `Pedidos.ReadWrite` de `Pedidos360 API` y conceder consentimiento.
5. Asignar al usuario uno de los roles de la API.

### 3. Variables de la aplicación

Editar `src/environments/environment.ts`:

```ts
tenantId: '<TENANT_ID>',
clientId: '<FRONTEND_CLIENT_ID>',
apiClientId: '<API_CLIENT_ID>',
apiUrl: 'https://<API_ID>.execute-api.<REGION>.amazonaws.com/api'
```

## Ejecución y verificación

```bash
npm ci
npm start
```

Abrir `http://localhost:4200`, iniciar sesión y comprobar:

1. La ruta `/inicio` solicita autenticación.
2. La ruta `/pedidos` exige autenticación y un rol permitido.
3. En DevTools > Network, `GET /api/orders` contiene `Authorization: Bearer ...`.
4. La pantalla muestra roles y scopes recuperados desde los claims.
5. Cerrar sesión devuelve al origen configurado.

Compilación de producción:

```bash
npm run build
```

No se deben subir `node_modules`, `dist`, `.env`, tokens ni secretos; están contemplados en `.gitignore`.
