package Gestionnaire;

import database.DatabaseConnection;
import exceptions.FormationDejaInscriteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class GestionnaireEtudiant {

    public static void gestionnaireEtudiant(Scanner sc, int idEtudiant) {
        System.out.println("Bienvenue étudiant !");
        while (true) {
            System.out.println("Veuillez choisir une option :");
            System.out.println("1: Voir les formations disponibles");
            System.out.println("2: S'inscrire à une formation");
            System.out.println("3: Voir formation inscrits");
            System.out.println("4: Rechercher des formations");
            System.out.println("5: Déconnexion");

            int choix = 0;
            try {
                choix = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide !");
                continue;
            }

            switch (choix) {
                case 1:
                	voirFormationDisponible();
                    break;
                case 2:
                    inscritFormation(sc, idEtudiant);
                    break;
                case 3:
                	voirFormation(idEtudiant);
                	break;
                case 4:
                	rechercherFormation(sc);
                	break;
                case 5:
                    System.out.println("Déconnexion réussie !");
                    return;
                default:
                    System.out.println("Veuillez entrer un choix valide !");
            }
        }
    }

    private static void voirFormationDisponible() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM formations";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Formations disponibles :");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id")+
                                   " | Nom: " + resultSet.getString("titre") +
                                   " | Description: " + resultSet.getString("description") +
                                   " | Prix: " + resultSet.getDouble("prix"));
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }

private static void voirFormation(int idEtudiant) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "SELECT formation_id FROM inscriptions WHERE etudiant_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idEtudiant);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Vos formations :");
                while (resultSet.next()) {
                    String queryFormation = "SELECT * FROM formations WHERE id = ?";
                    try (PreparedStatement preparedStatementFormation = connection.prepareStatement(queryFormation)) {
                        preparedStatementFormation.setInt(1, resultSet.getInt("formation_id"));
                        try (ResultSet resultSetFormation = preparedStatementFormation.executeQuery()) {
                            while (resultSetFormation.next()) {
                                System.out.println("ID: " + resultSetFormation.getInt("id") +
                                                   " | Nom: " + resultSetFormation.getString("titre") +
                                                   " | Description: " + resultSetFormation.getString("description") +
                                                   " | Prix: " + resultSetFormation.getDouble("prix"));
                            }
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Une erreur est survenue : " + e.getMessage());
    }
}

   
private static void inscritFormation(Scanner sc, int idEtudiant) {
    System.out.println("Entrez l'ID de la formation que vous souhaitez rejoindre :");
    int courseId = 0;
    try {
        courseId = Integer.parseInt(sc.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Veuillez entrer un ID valide !");
        return;
    }

    try (Connection connection = DatabaseConnection.getConnection()) {
        
        String checkQuery = "SELECT * FROM inscriptions WHERE etudiant_id = ? AND formation_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, idEtudiant);
            checkStmt.setInt(2, courseId);
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next()) {
                    throw new FormationDejaInscriteException("Vous êtes déjà inscrit à cette formation.");
                }
            }
        }

        
        String query = "INSERT INTO inscriptions (etudiant_id, formation_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idEtudiant);
            preparedStatement.setInt(2, courseId);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Inscription réussie !");
            }
        }
    } catch (FormationDejaInscriteException e) {
        System.out.println(e.getMessage());
    } catch (Exception e) {
        System.out.println("Formation inexistante ou une erreur est survenue.");
    }
}

 

private static void rechercherFormation(Scanner sc) {
    System.out.println("Entrez un mot-clé pour rechercher des formations :");
    String keyword = sc.nextLine();

    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "SELECT * FROM formations WHERE titre LIKE ? OR description LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, "%" + keyword + "%");
        preparedStatement.setString(2, "%" + keyword + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("Résultats de la recherche :");
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            System.out.println("ID: " + resultSet.getInt("id") +
                               " | Nom: " + resultSet.getString("titre") +
                               " | Description: " + resultSet.getString("description") +
                               " | Prix: " + resultSet.getDouble("prix"));
        }

        if (!found) {
            System.out.println("Aucune formation ne correspond à votre recherche.");
        }
    } catch (Exception e) {
        System.out.println("Une erreur est survenue : " + e.getMessage());
    }
}


}
