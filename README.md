# LinkeTinder (Versão com Gradle)

> Este é um repositório aprimorado do projeto original LinkeTinder. A principal diferença é que esta versão foi totalmente migrada para usar **Gradle** como sistema de build e gerenciamento de dependências, tornando o projeto mais robusto, moderno e fácil de executar.
>
> O repositório original (sem Gradle) pode ser encontrado em: [https://github.com/gabrielh0landa/LinkeTinder](https://github.com/gabrielh0landa/LinkeTinder)

## Sobre o Projeto

O projeto "LinkeTinder" é um aplicativo de console desenvolvido em Groovy como parte da trilha do AceleraZG. Ele simula um sistema de gerenciamento de perfis para candidatos e empresas, permitindo:

-   Criar perfis de candidatos com informações pessoais e competências.
-   Criar perfis de empresas com dados corporativos e competências exigidas.
-   Atualizar perfis existentes.
-   Listar todos os candidatos e empresas cadastradas.
-   Conectar a um banco de dados PostgreSQL para persistência de dados.

A aplicação segue o padrão de Orientação a Objetos, utilizando herança (`Pessoa` é a classe-pai de `Candidato` e `Empresa`) e separação de responsabilidades (classes `Controller`, `Service`, `Model` e `Repository`).

## Principais Melhorias (Diferenças da Versão Original)

A migração para Gradle trouxe várias vantagens:

-   **Gerenciamento de Dependências Automatizado:** Não é mais necessário ter uma pasta `/lib` com arquivos `.jar`. O Gradle baixa e gerencia todas as bibliotecas (como Groovy e o driver do PostgreSQL) automaticamente.
-   **Build Padronizado:** Qualquer pessoa pode compilar e executar o projeto com um único comando, sem depender da configuração da IDE.
-   **Estrutura de Projeto Moderna:** O código-fonte agora segue a convenção padrão de projetos Gradle (`src/main/groovy`), facilitando a manutenção.
-   **Facilidade de Execução:** O projeto pode ser executado diretamente pela linha de comando, sem a necessidade de configurar a IDE manualmente.

## Pré-requisitos

-   Java Development Kit (JDK) - Versão 17 ou superior.
-   Git para clonar o repositório.

**Você não precisa ter o Groovy instalado**, pois o Gradle Wrapper cuidará disso.

## Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/gabrielh0landa/Linketinder_Gradle.git](https://github.com/gabrielh0landa/Linketinder_Gradle.git)
    ```
    *(Lembre-se de substituir pela URL correta do seu novo repositório, se for diferente)*

2.  **Navegue até a pasta do projeto:**
    ```bash
    cd Linketinder_Gradle
    ```

3.  **Execute a aplicação:**
    - No Linux ou macOS:
      ```bash
      ./gradlew run
      ```
    - No Windows:
      ```bash
      gradlew.bat run
      ```

O Gradle cuidará de baixar as dependências, compilar o código e iniciar o programa no seu terminal.

## Estrutura do Projeto (Padrão Gradle)

```
├── .gradle/      # Cache e arquivos do Gradle
├── gradle/       # Arquivos do Gradle Wrapper
├── src/
│   ├── main/
│   │   └── groovy/ # Todo o código-fonte da aplicação
│   │       ├── controller/
│   │       ├── main/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── test/
│       └── groovy/ # Todos os testes unitários
├── build.gradle  # O coração do projeto: define dependências e tarefas
├── gradlew       # Script do Wrapper para Linux/macOS
├── gradlew.bat   # Script do Wrapper para Windows
└── settings.gradle # Configurações do projeto
```


## Autor

**Gabriel Holanda De Freitas**
