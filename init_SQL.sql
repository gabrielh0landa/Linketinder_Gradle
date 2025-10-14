CREATE TABLE "candidato" (
  "id" SERIAL PRIMARY KEY,
  "nome" varchar NOT NULL,
  "sobrenome" varchar NOT NULL,
  "data_nascimento" date NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "cpf" varchar UNIQUE NOT NULL,
  "pais" varchar NOT NULL,
  "cep" varchar NOT NULL,
  "descricao" varchar NOT NULL,
  "senha" varchar NOT NULL
);

CREATE TABLE "empresa" (
  "id" SERIAL PRIMARY KEY,
  "nome" varchar NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "cnpj" varchar UNIQUE NOT NULL,
  "pais" varchar NOT NULL,
  "cep" varchar NOT NULL,
  "descricao" varchar NOT NULL,
  "senha" varchar NOT NULL
);

CREATE TABLE "competencia" (
  "id" SERIAL PRIMARY KEY,
  "nome" varchar UNIQUE NOT NULL
);

CREATE TABLE "vaga" (
  "id" SERIAL PRIMARY KEY,
  "nome" varchar NOT NULL,
  "descricao" text NOT NULL,
  "local" varchar NOT NULL,
  "id_empresa" int NOT NULL
);

CREATE TABLE "vaga_competencia" (
  "id_vaga" int,
  "id_competencia" int,
  PRIMARY KEY ("id_vaga", "id_competencia")
);

CREATE TABLE "candidato_competencia" (
  "id_candidato" int,
  "id_competencia" int,
  PRIMARY KEY ("id_candidato", "id_competencia")
);

CREATE TABLE "candidatura" (
  "id_candidato" int,
  "id_vaga" int,
  PRIMARY KEY ("id_candidato", "id_vaga")
);

CREATE TABLE "interesses_empresa" (
  "id_empresa" int,
  "id_candidato" int,
  PRIMARY KEY ("id_empresa", "id_candidato")
);

ALTER TABLE "vaga" ADD FOREIGN KEY ("id_empresa") REFERENCES "empresa" ("id");

ALTER TABLE "vaga_competencia" ADD FOREIGN KEY ("id_vaga") REFERENCES "vaga" ("id");

ALTER TABLE "vaga_competencia" ADD FOREIGN KEY ("id_competencia") REFERENCES "competencia" ("id");

ALTER TABLE "candidato_competencia" ADD FOREIGN KEY ("id_candidato") REFERENCES "candidato" ("id");

ALTER TABLE "candidato_competencia" ADD FOREIGN KEY ("id_competencia") REFERENCES "competencia" ("id");

ALTER TABLE "candidatura" ADD FOREIGN KEY ("id_candidato") REFERENCES "candidato" ("id");

ALTER TABLE "candidatura" ADD FOREIGN KEY ("id_vaga") REFERENCES "vaga" ("id");

ALTER TABLE "interesses_empresa" ADD FOREIGN KEY ("id_empresa") REFERENCES "empresa" ("id");

ALTER TABLE "interesses_empresa" ADD FOREIGN KEY ("id_candidato") REFERENCES "candidato" ("id");

INSERT INTO empresa (nome, email, cnpj, pais, cep, descricao, senha) VALUES
(
  'Tech Inova Soluções',
  'contato@techinova.com',
  '11.222.333/0001-44',
  'Brasil',
  '01001-000',
  'Empresa líder em desenvolvimento de software e soluções de nuvem, focada em inovação e agilidade.',
  'senhaForte123'
),
(
  'Nexus Dados',
  'comercial@nexusdados.com',
  '22.333.444/0001-55',
  'Brasil',
  '20040-004',
  'Plataforma de Big Data e Analytics. Ajudamos empresas a transformar dados em decisões estratégicas.',
  'nexus_data_!'
),
(
  'Conecta Consultoria',
  'rh@conecta.com',
  '33.444.555/0001-66',
  'Brasil',
  '70150-900',
  'Consultoria especializada em processos ágeis e transformação digital para o mercado corporativo.',
  'conecta#2025'
),
(
  'Studio Criativo Digital',
  'orcamento@studiocriativo.com',
  '44.555.666/0001-77',
  'Brasil',
  '40020-000',
  'Agência de design e marketing digital, criando experiências de marca memoráveis para nossos clientes.',
  'criatividade10'
),
(
  'Pastelsoft S.A.',
  'recrutamento@pastelsoft.com',
  '55.666.777/0001-88',
  'Brasil',
  '80420-210',
  'Desenvolvimento de softwares ERPs para redes de restaurantes e distribuidoras de bebida de todo o país.',
  'pastel#soft'
);



INSERT INTO candidato (nome, sobrenome, data_nascimento, email, cpf, pais, cep, descricao, senha) VALUES
(
  'Lucas',
  'Almeida',
  '1995-03-15',
  'lucas.almeida@email.com',
  '111.222.333-44',
  'Brasil',
  '05424-000',
  'Desenvolvedor Full Stack com 5 anos de experiência em JavaScript/TypeScript (React, Node.js) e Java (Spring).',
  'lucas@1995'
),
(
  'Juliana',
  'Ribeiro',
  '1998-07-22',
  'juliana.r@email.com',
  '222.333.444-55',
  'Brasil',
  '22290-240',
  'Cientista de Dados apaixonada por machine learning e visualização de dados. Proficiente em Python, SQL e R.',
  'senhaJuliana!'
),
(
  'Fernando',
  'Costa',
  '1992-11-01',
  'fernando.c.designer@email.com',
  '333.444.555-66',
  'Brasil',
  '30112-010',
  'Designer UX/UI focado em criar experiências intuitivas e acessíveis. Especialista em Figma e Adobe XD.',
  'design#123'
),
(
  'Beatriz',
  'Oliveira',
  '1999-01-30',
  'beatriz.oliveira@email.com',
  '444.555.666-77',
  'Brasil',
  '90035-070',
  'Engenheira DevOps com experiência em automação de CI/CD, monitoramento e infraestrutura como código (Terraform, Ansible).',
  'b34tr1z_dev'
),
(
  'Sandra',
  'Nunes', 
  '2001-05-10',
  'sandra.nunes@email.com',
  '555.666.777-88',
  'Brasil',
  '60175-045',
  'Estudante de Análise e Desenvolvimento de Sistemas em busca da primeira oportunidade profissional. Foco em back-end com Java.',
  'sandubinha25'
);


SELECT * FROM candidato;
select * from empresa;
