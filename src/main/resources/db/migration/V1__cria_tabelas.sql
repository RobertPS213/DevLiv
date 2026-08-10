CREATE TABLE tb_estante (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    localizacao VARCHAR(100),
    capacidade INTEGER
);
CREATE TABLE tb_editora (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    email VARCHAR(100)
);
CREATE TABLE tb_autor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nacionalidade VARCHAR(50)
);
CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);
CREATE TABLE tb_livro (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    isbn VARCHAR(30) UNIQUE,
    ano_publicacao INTEGER,
    numero_paginas INTEGER,
    estante_id BIGINT NOT NULL,
    editora_id BIGINT NOT NULL,
    
    CONSTRAINT fk_livro_estante FOREIGN KEY (estante_id) REFERENCES tb_estante(id),
    CONSTRAINT fk_livro_editora FOREIGN KEY (editora_id) REFERENCES tb_editora(id)
);
CREATE TABLE tb_livro_autor (
    livro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    
    PRIMARY KEY (livro_id, autor_id),
    CONSTRAINT fk_livro_autor_livro FOREIGN KEY (livro_id) REFERENCES tb_livro(id) ON DELETE CASCADE,
    CONSTRAINT fk_livro_autor_autor FOREIGN KEY (autor_id) REFERENCES tb_autor(id) ON DELETE CASCADE
);
CREATE TABLE tb_livro_categoria (
    livro_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    
    PRIMARY KEY (livro_id, categoria_id),
    CONSTRAINT fk_livro_categoria_livro FOREIGN KEY (livro_id) REFERENCES tb_livro(id) ON DELETE CASCADE,
    CONSTRAINT fk_livro_categoria_categoria FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id) ON DELETE CASCADE
);