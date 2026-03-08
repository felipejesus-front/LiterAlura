# Literalura

Um aplicativo Java baseado em linha de comando que permite pesquisar, armazenar e gerenciar informações sobre livros e autores. O projeto utiliza a API Gutendex para obter dados de livros públicos e persiste as informações em um banco de dados PostgreSQL.

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação principal
- **Spring Boot 4.0.3** - Framework web e de aplicação
- **Spring Data JPA** - Abstração para acesso a dados com Hibernate
- **Hibernate** - ORM (Object-Relational Mapping) para mapeamento de entidades

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional para persistência de dados
- **JDBC Driver PostgreSQL** - Driver JDBC para conexão com PostgreSQL

### APIs Externas
- **Gutendex API** - API pública para buscar informações de livros (https://gutendex.com/)

### Bibliotecas Utilitárias
- **Jackson Databind 2.15.2** - Serialização e desserialização de JSON

### Build e Testes
- **Maven** - Gerenciador de dependências e build
- **JUnit** - Framework para testes unitários (via Spring Boot Starter Test)

## 📋 Pré-requisitos

- Java JDK 17 ou superior instalado
- PostgreSQL 12+ instalado e em execução
- Maven 3.6+ instalado
- Conexão com a internet (para consumir a API Gutendex)

## 🚀 Configuração e Instalação

### 1. Clone o repositório
```bash
git clone <seu-repositorio>
cd Literalura
```

### 2. Configure as variáveis de ambiente
Crie um arquivo `.env` ou configure as variáveis de ambiente do sistema com:

```bash
export DB_HOST=localhost:5432
export DB_NAME=literalura
export DB_USER=seu_usuario
export DB_PASSWORD=sua_senha
```

### 3. Crie o banco de dados PostgreSQL
```sql
CREATE DATABASE literalura;
```

### 4. Instale as dependências
```bash
./mvnw clean install
```

### 5. Execute a aplicação
```bash
./mvnw spring-boot:run
```

## 📚 Estrutura do Projeto

```
Literalura/
├── src/
│   ├── main/
│   │   ├── java/br/com/alura/Literalura/
│   │   │   ├── LiteraluraApplication.java      # Classe principal da aplicação
│   │   │   ├── model/
│   │   │   │   ├── Livro.java                  # Entidade de Livro (JPA)
│   │   │   │   ├── Autor.java                  # Entidade de Autor (JPA)
│   │   │   │   ├── DadosLivro.java             # Record para deserializar JSON da API
│   │   │   │   ├── DadoAutor.java              # Record para dados de Autor da API
│   │   │   │   └── Resposta.java               # Record para resposta da API
│   │   │   ├── principal/
│   │   │   │   └── Principal.java              # Menu interativo da aplicação
│   │   │   ├── repository/
│   │   │   │   ├── LivroRepository.java        # Interface JPA para Livro
│   │   │   │   └── AutorRepository.java        # Interface JPA para Autor
│   │   │   └── service/
│   │   │       ├── ConsumoApi.java             # Consumidor HTTP da API Gutendex
│   │   │       ├── ConverteDados.java          # Conversor JSON para objetos Java
│   │   │       └── IConverteDados.java         # Interface do conversor
│   │   └── resources/
│   │       └── application.properties          # Configurações da aplicação
│   └── test/
│       └── java/br/com/alura/Literalura/
│           └── LiteraluraApplicationTests.java # Testes da aplicação
└── pom.xml                                      # Configuração Maven
```

## 🗄️ Modelo de Dados

### Tabelas do Banco de Dados

#### Tabela: `livros`
| Coluna  | Tipo    | Descrição                      |
|---------|---------|--------------------------------|
| id      | INTEGER | Identificador único (PK)       |
| titulo  | VARCHAR | Título do livro (UNIQUE)       |
| linguas | VARCHAR | Idiomas separados por vírgula  |

#### Tabela: `autores`
| Coluna      | Tipo    | Descrição                      |
|-------------|---------|--------------------------------|
| id          | BIGINT  | Identificador único (PK)       |
| nome        | VARCHAR | Nome do autor (UNIQUE)         |
| nascimento  | INTEGER | Ano de nascimento              |
| morte       | INTEGER | Ano de morte (NULL se vivo)    |

#### Tabela: `livros_autores` (Many-to-Many)
| Coluna    | Tipo    | Descrição                |
|-----------|---------|--------------------------|
| livro_id  | INTEGER | Referência para livro    |
| autor_id  | BIGINT  | Referência para autor    |

## 🎯 Funcionalidades

### Menu Principal

O aplicativo oferece um menu interativo com as seguintes opções:

#### 1. Buscar Livro na Web
- Pesquisa por título de livro na API Gutendex
- Exibe os resultados encontrados
- Salva automaticamente os livros e autores no banco de dados
- Evita duplicatas de livros e autores

#### 2. Listar Livros Registrados
- Exibe todos os livros salvos no banco de dados
- Mostra título, autores associados e idiomas

#### 3. Listar Autores Registrados
- Exibe todos os autores salvos
- Mostra nome, ano de nascimento e ano de morte (ou "Vivo(a)" se ainda vivo)

#### 4. Listar Autores Vivos em um Ano
- Permite pesquisar autores que estavam vivos em um ano específico
- Baseia-se nos anos de nascimento e morte registrados

#### 5. Listar Livros por Idioma
- Filtra livros pelo código de idioma (ex: 'pt' para português, 'en' para inglês)
- Exibe todos os livros disponíveis naquele idioma

## 🔗 Relacionamentos de Dados

- **Livro ↔ Autor**: Relação Many-to-Many
  - Um livro pode ter vários autores
  - Um autor pode ter vários livros
  - A relação é gerenciada através da tabela `livros_autores`

## 🔍 Queries SQL Importantes

### Listar todos os livros com seus autores
```sql
SELECT l.*, a.nome 
FROM livros l
LEFT JOIN livros_autores la ON l.id = la.livro_id
LEFT JOIN autores a ON la.autor_id = a.id;
```

### Buscar autores vivos em um ano específico
```sql
SELECT * FROM autores 
WHERE nascimento <= :ano 
AND (morte IS NULL OR morte >= :ano);
```

### Listar livros por idioma
```sql
SELECT * FROM livros 
WHERE linguas LIKE CONCAT('%', :idioma, '%');
```

## 🛡️ Configurações de Logging

A aplicação utiliza Spring Boot Logging para exibir as queries SQL:

```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

Isso permite visualizar todas as queries SQL executadas e seus parâmetros no console.

## 📝 Exemplo de Uso

```
--- MENU LITERALURA ---
1 - Buscar livro na web
2 - Listar livros registrados
3 - Listar autores registrados
4 - Listar autores vivos em um ano
5 - Listar livros por idioma
0 - Sair

Escolha uma opção:
1
Digite o nome do livro para busca:
Dom Casmurro

Livro salvo: Dom Casmurro

--- Resumo ---
Livros salvos: 1
Livros já existentes: 0
```

## 🐛 Tratamento de Erros

- **Livro já existe**: Se o título já está registrado, o livro não é salvo novamente
- **Autor já existe**: Se o autor já está no banco, é reutilizado ao invés de criar uma duplicata
- **Entidades detached**: Autores recuperados do banco são gerenciados corretamente sem erro de cascata

## 📖 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [Gutendex API Documentation](https://gutendex.com/books)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

## 👨‍💻 Desenvolvimento

### Compilar o projeto
```bash
./mvnw clean compile
```

### Executar testes
```bash
./mvnw test
```

### Criar pacote JAR
```bash
./mvnw clean package
```

### Executar o JAR
```bash
java -jar target/Literalura-0.0.1-SNAPSHOT.jar
```

## 📄 Licença

Este projeto é um projeto de aprendizado.

## 🎓 Notas de Aprendizado

Este projeto demonstra:
- Uso de Spring Boot para criar aplicações Java
- Mapeamento objeto-relacional (ORM) com Hibernate/JPA
- Relacionamentos Many-to-Many em bancos de dados
- Consumo de APIs REST externas
- Persistência de dados com PostgreSQL
- Queries JPQL para busca de dados
- Design de aplicações em linha de comando com menu interativo

