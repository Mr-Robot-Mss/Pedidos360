# Pedidos360 - Backend

Backend cloud native construido como proyecto Maven multimódulo con tres componentes independientes:

- `bff-service` (8080): único punto público. Valida JWT de Microsoft Entra ID y aplica autorización.
- `orders-service` (8081): microservicio de pedidos con entidad y repositorio JPA.
- `catalog-service` (8082): microservicio de catálogo con entidad y repositorio JPA.

## Arquitectura

```mermaid
flowchart LR
  A[Angular + MSAL] -->|Bearer JWT| G[AWS API Gateway]
  G -->|JWT validado| B[BFF Spring Boot]
  B --> O[Orders Service]
  B --> C[Catalog Service]
  O --> DB[(Base de datos)]
  C --> DB
```

El token se valida dos veces: API Gateway funciona como perímetro y el BFF aplica defensa en profundidad. Spring Security verifica firma criptográfica, `iss`, `aud`, `exp` y `nbf`. Los claims `scp` se convierten en autoridades `SCOPE_...` y `roles` en autoridades `ROLE_...`.

## Requisitos

- Java 17
- Maven 3.9+
- Docker y Docker Compose (alternativa recomendada)

## Configuración

Copiar `.env.example` como `.env` y reemplazar:

| Variable | Ejemplo | Propósito |
|---|---|---|
| `AZURE_ISSUER_URI` | `https://login.microsoftonline.com/<tenant>/v2.0` | Emisor exacto permitido |
| `AZURE_API_AUDIENCE` | `<api-client-id>` | Audiencia exacta esperada en tokens v2 |
| `FRONTEND_ORIGIN` | `http://localhost:4200` | Origen CORS permitido |

Para conectar una base cloud (por ejemplo, Amazon RDS PostgreSQL), definir en cada microservicio `DB_URL`, `DB_USER`, `DB_PASSWORD` y `DDL_AUTO=validate`. Sin estas variables se utiliza H2 en memoria para desarrollo.

## Ejecución

```bash
mvn clean test
mvn -pl orders-service spring-boot:run
mvn -pl catalog-service spring-boot:run
mvn -pl bff-service spring-boot:run
```

O ejecutar los tres componentes:

```bash
docker compose up --build
```

## Endpoints y autorización

| Endpoint público del BFF | Regla |
|---|---|
| `GET /actuator/health` | Público |
| `GET /api/orders` | Scope `Pedidos.ReadWrite` o rol `Pedidos.Admin`/`Pedidos.Operador` |
| `GET /api/catalog` | Scope `Pedidos.ReadWrite` |

Un token ausente, mal firmado, vencido, de otro issuer o con audiencia incorrecta genera `401`. Un token válido sin scope/rol suficiente genera `403`.

## AWS

1. Desplegar los JAR o contenedores en EC2 dentro de una VPC.
2. Exponer públicamente solo el BFF; los microservicios deben permanecer en red privada.
3. Importar `deploy/api-gateway-openapi.yml` como definición de una HTTP API.
4. Reemplazar tenant, client ID, región, API ID y DNS de EC2.
5. Restringir el Security Group del BFF para aceptar tráfico desde la infraestructura definida.

Nunca se deben versionar secretos ni tokens. `.env` está excluido mediante `.gitignore`.

## Evidencia de pruebas

`AudienceValidatorTest` demuestra aceptación de la audiencia correcta y rechazo de una incorrecta. Antes de entregar, ejecutar `mvn clean test` y adjuntar una captura del resultado exitoso.
