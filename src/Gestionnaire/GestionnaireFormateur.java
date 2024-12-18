package Gestionnaire;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


public class GestionnaireFormateur {

public static void gestionnaireFormateur(Scanner sc, int idFormateur) {

        while (true) {
            System.out.println("Veuillez choisir une option :");
            System.out.println("1: Ajouter une formation");
            System.out.println("2: Voir les formations disponibles");
            System.out.println("3: Voir mes formations");
            System.out.println("4: Modifier Formation");
            System.out.println("5: Supprimer Formation");
            System.out.println("6: Voir les statistiques des formations");
            System.out.println("7: Déconnexion");

            int choix = 0;
            try {
                choix = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide !");
                continue;
            }
            ////Hal
            switch (choix) {
                case 1:
                    ajoutFormation(sc, idFormateur);
                    break;
                case 2:
                    voirFormationDisponible();
                    break;
                case 3:
                    voirFormation(idFormateur);
                    break;

                case 4:
                    modifierFormation(idFormateur);
                    break;
                case 5:
                    supprimerFormation(idFormateur);
                    break;
                case 6:
                	afficherStatistiquesFormateur(idFormateur);
                	break;
                case 7:
                    System.out.println("Déconnexion réussie !");
                    return; // Exit the method
                default:
                    System.out.println("Veuillez entrer un choix valide !");
            }
        }
    }

    
    
private static void modifierFormation(int idFormateur) {
    Scanner sc = new Scanner(System.in);
    ///Li
    int formationId = -1;

    while (true) {
        System.out.println("Entrez l'ID de la formation que vous souhaitez modifier (ou 0 pour annuler) :");
        try {
            formationId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un ID valide !");
            continue;
        }
        

        if (formationId == 0) {
            System.out.println("Modification annulée. Retour au menu principal.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM formations WHERE id = ? AND formateur_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, formationId);
            preparedStatement.setInt(2, idFormateur);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Aucune formation trouvée avec cet ID pour ce formateur. Veuillez réessayer.");
                continue;
            }
            break; 
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
            return;
        }
    }

 
    System.out.println("Entrez le nouveau titre de la formation (ou appuyez sur Entrée pour ne pas modifier) :");
    String newTitle = sc.nextLine();

    System.out.println("Entrez la nouvelle description (ou appuyez sur Entrée pour ne pas modifier) :");
    String newDescription = sc.nextLine();

    Double newPrice = null;
    while (true) {
        System.out.println("Entrez le nouveau prix (ou appuyez sur Entrée pour ne pas modifier) :");
        String newPriceInput = sc.nextLine();

        if (newPriceInput.isEmpty()) {
            break; 
        }

        try {
            newPrice = Double.parseDouble(newPriceInput);
            if (newPrice < 0) {
                System.out.println("Le prix doit être positif !");
                continue;
            }
            break;
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un prix valide !");
        }
    }

    try (Connection connection = DatabaseConnection.getConnection()) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE formations SET ");
        boolean hasUpdates = false;

        if (!newTitle.isEmpty()) {
            queryBuilder.append("titre = ?, ");
            hasUpdates = true;
        }
        if (!newDescription.isEmpty()) {
            queryBuilder.append("description = ?, ");
            hasUpdates = true;
        }
        if (newPrice != null) {
            queryBuilder.append("prix = ?, ");
            hasUpdates = true;
        }

        if (!hasUpdates) {
            System.out.println("Aucune modification détectée.");
            return;
        }

        String query = queryBuilder.substring(0, queryBuilder.length() - 2) + " WHERE id = ? AND formateur_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        int index = 1;
        if (!newTitle.isEmpty()) preparedStatement.setString(index++, newTitle);
        if (!newDescription.isEmpty()) preparedStatement.setString(index++, newDescription);
        if (newPrice != null) preparedStatement.setDouble(index++, newPrice);
        preparedStatement.setInt(index++, formationId);
        preparedStatement.setInt(index, idFormateur);

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Formation mise à jour avec succès !");
        } else {
            System.out.println("Formation introuvable ou vous n'avez pas les droits pour la modifier.");
        }
    } catch (Exception e) {
        System.out.println("Une erreur est survenue : " + e.getMessage());
    }
    
}


private static void ajoutFormation(Scanner sc, int idFormateur) {
        System.out.println("Entrez le nom de la formation : ");
        String name = sc.nextLine();
        System.out.println("Entrez la description : ");
        String description = sc.nextLine();
        double price = -1;
        while(true) {
        System.out.println("Entrez le prix : ");
        try {
            price = Double.parseDouble(sc.nextLine());
            break;
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un prix valide !");
        }
        }
        

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO formations (titre, description,formateur_id, prix) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, idFormateur);
            preparedStatement.setDouble(4, price);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Formation ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout de la formation.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }

private static void voirFormation(int idFormateur) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM formations WHERE formateur_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idFormateur); 
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Vos formations :");
            while (resultSet.next()) {
            	
                System.out.println("ID: " + resultSet.getInt("id") +
                                   " | Nom: " + resultSet.getString("titre") +
                                   " | Description: " + resultSet.getString("description") +
                                   " | Prix: " + resultSet.getDouble("prix"));
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }
    
private static void supprimerFormation(int idFormateur) {
    Scanner sc = new Scanner(System.in);
    
    while (true) {
        System.out.println("Entrez l'ID de la formation à supprimer (ou entrez 0 pour quitter) :");
        int formationId = -1;
        try {
            formationId = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un ID valide !");
            continue;
        }

        if (formationId == 0) {
            System.out.println("Retour au menu principal.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM formations WHERE id = ? AND formateur_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, formationId);
            preparedStatement.setInt(2, idFormateur);
            //M
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Formation supprimée avec succès !");
                return;
            } else {
                System.out.println("Aucune formation trouvée avec cet ID pour ce formateur. Veuillez réessayer.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
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
            System.out.println(
                               "Nom: " + resultSet.getString("titre") +
                               " | Description: " + resultSet.getString("description") +
                               " | Prix: " + resultSet.getDouble("prix"));
        }
    } catch (Exception e) {
        System.out.println("Une erreur est survenue : " + e.getMessage());
    }
}


private static void afficherStatistiquesFormateur(int idFormateur) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = """
            SELECT f.titre, 
                   COUNT(i.etudiant_id) AS nombre_inscriptions, 
                   SUM(f.prix) AS revenus
            FROM formations f
            LEFT JOIN inscriptions i ON f.id = i.formation_id
            WHERE f.formateur_id = ?
            GROUP BY f.id
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, idFormateur);
        ResultSet resultSet = preparedStatement.executeQuery();

        System.out.println("Statistiques pour vos formations :");
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            System.out.println("Formation : " + resultSet.getString("titre") +
                               " | Inscriptions : " + resultSet.getInt("nombre_inscriptions") +
                               " | Revenus générés : " + resultSet.getDouble("revenus") + " €");
        }

        if (!found) {
            System.out.println("Aucune donnée trouvée pour vos formations.");
        }
    } catch (Exception e) {
        System.out.println("Une erreur est survenue : " + e.getMessage());
    }
}

}
