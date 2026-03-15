# 🔐 Auth API — Spring Boot 3.4 + Java 21 + JWT

API REST completa com sistema de autenticação via JWT Token.

---

## 🚀 Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 (LTS) |
| Spring Boot | 3.4.3 |
| Spring Security | 6.x |
| JJWT | 0.12.6 |
| Spring Data JPA | 3.4.x |
| H2 Database | (dev/test) |
| SpringDoc OpenAPI | 2.8.5 |
| Lombok | latest |

---

## 📁 Estrutura do Projeto

```
src/main/java/com/authapi/
├── config/
│   ├── DataInitializer.java     # Seed de dados iniciais
│   ├── GlobalExceptionHandler.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java      # /api/auth/**
│   └── AdminController.java     # /api/admin/** (ADMIN only)
├── dto/
│   └── AuthDtos.java            # Records: Request/Response DTOs
├── entity/
│   ├── RefreshToken.java
│   ├── Role.java
│   └── User.java
├── repository/
│   ├── RefreshTokenRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthenticationFilter.java
│   └── JwtService.java
└── service/
    ├── AuthService.java
    └── UserDetailsServiceImpl.java
```

---

## ▶️ Como executar

```bash
# Clonar o repositório
git clone <url>
cd auth-api

# Executar com Maven Wrapper
./mvnw spring-boot:run
```

A aplicação inicia em: **http://localhost:8080**

---

## 📖 Swagger UI

Acesse a documentação interativa em:

```
http://localhost:8080/swagger-ui.html
```

---

## 🔑 Endpoints

### Autenticação (público)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/auth/register` | Cadastro de novo usuário |
| `POST` | `/api/auth/login` | Login (retorna JWT) |
| `POST` | `/api/auth/refresh` | Renovar access token |
| `POST` | `/api/auth/logout` | Logout (revoga tokens) 🔒 |
| `GET`  | `/api/auth/me` | Perfil do usuário autenticado 🔒 |

### Admin (requer `ROLE_ADMIN`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET`    | `/api/admin/users` | Listar todos os usuários |
| `PUT`    | `/api/admin/users/{id}/role` | Alterar role do usuário |
| `DELETE` | `/api/admin/users/{id}` | Desativar usuário |

---

## 🧪 Exemplos de uso

### 1. Cadastrar usuário
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "password": "senha123"
  }'
```

**Resposta:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "uuid-refresh-token",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@email.com",
    "role": "USER",
    "createdAt": "2025-01-01T10:00:00"
  }
}
```

### 3. Usar token em rota protegida
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 4. Renovar token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "uuid-refresh-token"}'
```

---

## 👤 Usuários pré-criados (dev)

| E-mail | Senha | Role |
|--------|-------|------|
| `admin@authapi.com` | `admin123` | ADMIN |
| `user@authapi.com` | `user123` | USER |

---

## 🗄️ H2 Console (dev)

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:authdb
Usuário: sa
Senha: (vazio)
```

---

## 🔒 Segurança

- Tokens JWT assinados com **HS256**
- **Access Token**: expira em 24h
- **Refresh Token**: expira em 7 dias, armazenado no banco
- Senhas encriptadas com **BCrypt**
- Rotas protegidas por **Spring Security 6**
- Suporte a roles: `USER`, `ADMIN`, `MODERATOR`
- `@PreAuthorize` para controle granular por método

---

## 🔧 Configuração (application.yml)

```yaml
jwt:
  secret: <chave-base64-256bits>
  expiration: 86400000       # 24h
  refresh-expiration: 604800000  # 7 dias
```

> ⚠️ **Produção**: troque o `jwt.secret` por uma chave segura e use PostgreSQL/MySQL.
