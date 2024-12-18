CREATE DATABASE plateforme;

USE plateforme;

CREATE TABLE utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    motDePasse VARCHAR(100),
    type ENUM('formateur', 'etudiant')
);

CREATE TABLE formations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(100),
    description TEXT,
    formateur_id INT,
    prix DOUBLE,
    FOREIGN KEY (formateur_id) REFERENCES utilisateurs(id)
);

CREATE TABLE inscriptions (
    etudiant_id INT,
    formation_id INT,
    PRIMARY KEY (etudiant_id, formation_id),
    FOREIGN KEY (etudiant_id) REFERENCES utilisateurs(id),
    FOREIGN KEY (formation_id) REFERENCES formations(id)
);
