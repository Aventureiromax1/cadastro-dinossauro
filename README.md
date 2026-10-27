# Cadastro de Dinossauros - Spring Boot & PostgreSQL

Projeto desenvolvido para a disciplina de LP VI (Java), demonstrando a integração do **Spring Boot 3** com **Spring Data JPA** e banco de dados **PostgreSQL**.

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
  - Spring Web
  - Spring Data JPA (Hibernate)
  - Spring Boot Starter Test
- **PostgreSQL Driver**
- **Maven Wrapper (`mvnw`)**

---

## ⚙️ Configuração do Banco de Dados

Arquivo: `src/main/resources/application.properties`

```properties
spring.application.name=cadastro-dinossauro

# Configuração PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/cadastro_dinossauro
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> **Nota:** As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`) ao inicializar a aplicação ou persistir entidades.

---

## 🚀 Como Executar

### 1. Criar o banco no PostgreSQL (caso ainda não exista)
```bash
createdb -U postgres -h localhost -p 5432 cadastro_dinossauro
```
*(ou via `psql -U postgres -c "CREATE DATABASE cadastro_dinossauro;"`)*

### 2. Rodar a aplicação
Na raiz do projeto:
```bash
./mvnw spring-boot:run
```

A aplicação iniciará na porta `8080`.

---

## 📮 Testes no Postman / cURL

### 1. Cadastrar Dinossauro (POST)
- **URL:** `http://localhost:8080/dinossauro`
- **Método:** `POST`
- **Header:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "nome": "Tiranossauro Rex",
  "especie": "Tyrannosaurus rex",
  "peso": 8000,
  "altura": 4.0,
  "comprimento": 12.3,
  "comportamento": "Carnívoro predador"
}
```
- **Resposta Esperada:** `201 Created`

### 2. Listar Todos os Dinossauros (GET)
- **URL:** `http://localhost:8080/dinossauro`
- **Método:** `GET`
- **Resposta Esperada:** `200 OK` (retorna lista com os dinossauros cadastrados)

### 3. Buscar Dinossauro por ID (GET)
- **URL:** `http://localhost:8080/dinossauro/1`
- **Método:** `GET`

### 4. Atualizar Dinossauro (PUT)
- **URL:** `http://localhost:8080/dinossauro/1`
- **Método:** `PUT`
- **Header:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "nome": "T-Rex Alfa",
  "especie": "Tyrannosaurus rex",
  "peso": 8500,
  "altura": 4.2,
  "comprimento": 12.5,
  "comportamento": "Superpredador territorial"
}
```

### 5. Excluir Dinossauro (DELETE)
- **URL:** `http://localhost:8080/dinossauro/1`
- **Método:** `DELETE`
- **Resposta Esperada:** `204 No Content`
