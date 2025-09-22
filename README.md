# Bihan 🔗 – Minimal URL Shortener for BreizhCamp

Tiny, deterministic, cache-friendly URL shortener with expirable links.

TOC: [🚀 Quick Start](#-quick-start) • [🔧 Usage](#-usage) • [🧱 Architecture & Design](#-architecture--design) • [⚙️ Configuration](#-configuration) • [🗄️ Database & Migrations](#-database--migrations) • [🐳 Container Image](#-container-image) • [🧪 Testing](#-testing) • [🔐 Security Notes](#-security-notes) • [📜 License](#-license)

## 🚀 Quick Start

Prereqs: Java 21, Maven (wrapper provided), Docker (for Postgres).

Run Postgres:
```bash
docker-compose up -d
```

Run the app (dev profile):
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
Dev defaults:
- URL: http://localhost:4010/
- API Key: 1234567890
- DB: localhost:29332 (user/pass: bihan / bihan)

Jar build:
```bash
./mvnw clean package
java -jar target/bihan-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## 🔧 Usage

Create a short link (only admin operation exposed):

```bash
API_KEY=1234567890
BASE=http://localhost:4010

curl -s -X POST "$BASE/api/links" \
  -H "Authorization: ApiKey $API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
        "url": "https://breizhcamp.org/schedule",
        "expirationDate": "2025-12-31T23:59:59Z",
        "id": "optionalCustomId"
      }'
# => {"id":"AbCdEf"}
```

Follow redirect:
```
open $BASE/AbCdEf        # or curl -I $BASE/AbCdEf
```

Behavior:
- Unknown / expired / missing ID → redirects to fallback (`bihan.base-redirect`)
- Same (url, expirationDate) submitted again returns same short code
- Optional `id` used if free; else random 6-letter (a–zA–Z)

## 🧱 Architecture & Design

Hexagonal / layered slices:
```
application/  (REST controllers, cron, DTO)
config/       (typed properties)
domain/       (entity + use cases + ports)
infrastructure/ (adapters, JPA repo, persistence model)
```

Flow (create):
`AdminCtrl` → `AddLink` use case → `LinkPort` → `LinkAdapter` → `LinkRepo` (JPA) → Postgres  
Flow (redirect): `RedirectCtrl` → `GetLink` → cache / DB → URL or fallback

Key points ✨:
- Deterministic persistence id: SHA-256(expiration|url) prevents duplicates
- Public short code (`linkId`) either user-provided or generated (collision-checked)
- Expiration enforced at read + periodic purge
- Caffeine cache (`@Cacheable("links")`) for read path
- Hourly purge (`PurgeLinksCron`) evicts expired rows + clears cache
- Liquibase migrations for schema
- Small surface area (only one write endpoint) → simple hardening

ID Strategy:
- DB primary key = hash (stable)
- Short code = human friendly (6 chars) or explicit
- Up to 1000 attempts to generate a collision-free random code

Caching:
- GET lookups cached by short code
- Mutations (add / purge) evict as needed

## ⚙️ Configuration

Prefix `bihan.` (see `BihanConfig`):

| Property        | Description                                         |
|-----------------|-----------------------------------------------------|
| base-redirect   | Fallback target when ID invalid / expired / absent  |
| public-url      | Public base URL of this instance                    |
| api-key         | Shared secret for `POST /api/links`                 |

Dev profile (`application-dev.yml`):
- `server.port=4010`
- Local Postgres config
- `bihan.api-key=1234567890`

Activate a profile:
```
-Dspring.profiles.active=dev
# or
SPRING_PROFILES_ACTIVE=dev
```

Env-based prod example:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${POSTGRESQL_ADDON_HOST}:${POSTGRESQL_ADDON_PORT}/${POSTGRESQL_ADDON_DB}
    username: ${POSTGRESQL_ADDON_USER}
    password: ${POSTGRESQL_ADDON_PASSWORD}
bihan:
  base-redirect: https://www.breizhcamp.org/
  public-url: https://bzh.camp/
  api-key: ${ADMIN_API_KEY}
```

## 🗄️ Database & Migrations

- Postgres (see `docker-compose.yml`)
- Liquibase changelogs under `src/main/resources/db/changelog`
- Auto-applied on startup
- Purge job physically deletes expired links hourly

## 🐳 Container Image

Build local Docker image via Jib (no Dockerfile needed):
```bash
./mvnw jib:dockerBuild -Dimage=bihan:local
```

Push to registry:
```bash
./mvnw jib:build -Dimage=ghcr.io/your-org/bihan:latest
```

## 🧪 Testing

```bash
./mvnw test
```

## 🔐 Security Notes

- Simple header auth: `Authorization: ApiKey <key>`
- 404 on auth failure (avoids differentiating auth vs not-found)
- Recommend TLS termination + rate limiting at reverse proxy
- Consider rotation of API key through secret management

## 📜 License

[Bihan][repo_url] is free and open-source software licensed under
the [GPL-3.0 License][repo_license_url]

---

Enjoy shortening! 🚀
