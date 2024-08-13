CREATE TABLE animals
(
    id              BIGSERIAL   NOT NULL,
    animal_type     varchar(30) NOT NULL,
    animal_status   varchar(30) NOT NULL,
    breed           varchar(40) NULL,
    date_of_birth   timestamp NULL,
    breeder_id      INT NOT NULL,
    CONSTRAINT PK_animals PRIMARY KEY (id)
);
CREATE TABLE breeders(
    id              BIGSERIAL   NOT NULL,
    first_name      varchar(30) NULL,
    last_name       varchar(50) NULL,
    password        varchar(255) NULL,
    email           varchar(255) NULL,
    CONSTRAINT PK_animals PRIMARY KEY (id)
);