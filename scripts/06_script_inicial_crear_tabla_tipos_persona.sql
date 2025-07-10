CREATE TABLE IF NOT EXISTS tipos_personas (
    tipo_persona_id INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

INSERT INTO
    tipos_personas (tipo_persona_id, nombre)
VALUES
    (1, 'Natural'),
    (2, 'Jurídica'),
    (3, 'Extranjera'),
    (4, 'Gobierno'),
    (5, 'ONG'),
    (6, 'Otro');