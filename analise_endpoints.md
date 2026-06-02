# Análise de Endpoints — EducCore LMS

## Arquitetura Geral

O **Next.js** funciona como um BFF (Backend for Frontend): as páginas chamam rotas `/api/*` do próprio Next, que por sua vez fazem proxy para o **Spring Boot** em `localhost:8080`. Há alguns casos de chamada direta ao Java (sem proxy).

---

## ✅ Endpoints Corretamente Integrados (Back ↔ Front)

| Recurso | Backend (Spring Boot) | Frontend (Next.js proxy) | Operações |
|---|---|---|---|
| **Login** | `POST /auth/login` | `POST /api/login` | Login |
| **Registro** | `POST /auth/register` | `POST /api/register` | Cadastro |
| **Usuários** | `GET/POST /api/users` | `GET/POST /api/users` | CRUD completo |
| **Usuários** | `GET/PUT/DELETE /api/users/{id}` | `GET/PUT/DELETE /api/users/{id}` | CRUD completo |
| **Cursos** | `GET/POST /api/courses` | `GET/POST /api/courses` | CRUD completo |
| **Cursos** | `PUT/DELETE /api/courses/{id}` | `PUT/DELETE /api/courses/{id}` | CRUD completo |
| **Banners** | `GET/POST /api/banners` | `GET/POST /api/banners` | CRUD completo |
| **Banners** | `PUT/DELETE /api/banners/{id}` | `PUT/DELETE /api/banners/{id}` | CRUD completo |
| **Banner-Register** | `GET /api/banner-register` | `GET /api/banner-register` | Leitura |
| **Ícones** | `GET /api/icons` | `GET /api/icons` | Leitura |
| **Pages** | `GET/POST /api/pages` | `GET/POST /api/pages` | CRUD completo |
| **Pages** | `GET/PUT/DELETE /api/pages/{id}` | `GET/PUT/DELETE /api/pages/{id}` | CRUD completo |
| **Grupos** | `GET/POST /api/groups` | `GET/POST /api/groups` | CRUD completo |
| **Grupos** | `GET/PUT/DELETE /api/groups/{id}` | `GET/PUT/DELETE /api/groups/{id}` | CRUD completo |
| **Professores** | `GET/POST /professor` | `GET/POST /api/teacher` | CRUD completo |
| **Professores** | `PUT/DELETE /professor/{id}` | `PUT/DELETE /api/teacher/{id}` | CRUD completo |
| **About** | `GET /api/about` | `GET /api/about` | Leitura |
| **About** | `PUT /api/about` | `PUT /api/about` | Atualização |

---

## 🔴 Endpoints do Back que NÃO são consumidos pelo Front

### 1. `GET/PUT/DELETE /api/faqs` — FaqController
- **O backend tem CRUD completo de FAQs** (`GET`, `POST`, `PUT /{id}`, `DELETE /{id}`)
- **O front não tem nenhuma rota `/api/faqs`** nem nenhum `fetch` para esse endpoint
- Provavelmente a seção de FAQ existe no produto mas ainda não foi integrada ao admin

### 2. `POST/PUT/GET /empresa` — EmpresaController
- O backend tem CRUD de `Empresa` (a entidade raiz que representa a escola/cliente)
- O front só chama `http://localhost:8080/empresa` diretamente em **uma única tela** (`website/page.tsx`) — apenas o `POST`, sem `GET` nem `PUT`
- Não existe proxy Next.js para esse recurso (chamada direta ao Java hardcoded)
- O `GET /empresa` nunca é chamado pelo front

### 3. `GET/POST/PUT/DELETE /api/modules` — ModuleController
- Backend possui CRUD completo de módulos
- O front **não consume esses endpoints em nenhuma tela integrada** — a tela `editContentCourse` usa dados mockados estáticos (hardcoded)

### 4. `GET/POST/PUT/DELETE /api/classes` — LessonController
- Backend possui CRUD completo de aulas
- O front **não consume** — mesma situação acima, a tela usa mock

### 5. `GET/POST/DELETE /api/module-class` — ModuleClassController
- Backend tem endpoints para vincular aulas a módulos
- O front **não consome** — sem nenhum fetch para esse recurso

---

## 🟡 Endpoints consumidos pelo Front que têm problemas

### 1. `/api/recoverPass` — Recuperação de senha
- O front chama `POST /api/recoverPass` (componente `RecoverPassModal`)
- A route do Next.js **existe mas não faz nada** — apenas loga o e-mail no console e retorna `{ message: "E-mail recebido" }`
- **Não existe endpoint correspondente no backend Spring Boot**

### 2. Chamada direta `POST http://localhost:8080/empresa` (sem proxy)
- A tela `website/page.tsx` chama o Java diretamente (sem passar pelo proxy Next.js)
- Isso pode causar **problemas de CORS em produção** e dificulta manutenção
- Além disso, o `EmpresaController` no back **não tem `@CrossOrigin`**, o que vai quebrar em produção

---

## 📋 Resumo Visual

```
BACKEND TEM, FRONT NÃO CONSOME:
  ❌ /api/faqs          (CRUD completo sem uso no front)
  ❌ /empresa GET/PUT   (só POST é chamado, e diretamente)
  ❌ /api/modules       (tela usa dados mockados)
  ❌ /api/classes       (tela usa dados mockados)
  ❌ /api/module-class  (nunca chamado)

FRONT CONSOME, BACK NÃO TEM:
  ❌ /api/recoverPass   (stub vazio no Next, sem backend real)

INCONSISTÊNCIAS:
  ⚠️  Chamada direta ao Java em website/page.tsx (sem proxy, sem CrossOrigin)
  ⚠️  Mistura de localhost vs 127.0.0.1 nas rotas proxy (inconsistência menor)
```

---

## 🗺️ Contexto LMS — O que falta desenvolver

Dado que é um LMS white-label por empresa, as lacunas mais críticas são:

1. **Gestão de conteúdo do curso** (Módulos + Aulas + Vínculo) — back está pronto, front é mock
2. **Recuperação de senha** — nem front nem back implementados de fato
3. **FAQ** — back pronto, front inexistente
4. **Empresa** — entidade central do produto, com integração incompleta

