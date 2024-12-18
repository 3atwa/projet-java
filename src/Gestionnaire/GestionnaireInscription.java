package Gestionnaire;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class GestionnaireInscription {

    public static void gestionnaireInscription(Scanner sc) {
        System.out.println("Name : ");
        String name = sc.nextLine();
        System.out.println("Email : ");
        String email = sc.nextLine();
        System.out.println("Password : ");
        String password = sc.nextLine();
        System.out.println("User Type (etudiant/formateur): ");
        String userType = sc.nextLine();

        if (!userType.equals("etudiant") && !userType.equals("formateur")) {
            System.out.println("Invalid user type! Please enter either 'student' or 'formateur'.");
            return;
        }
        
        
        try (Connection connection = DatabaseConnection.getConnection()) {
        	
        	
        	
        	
            String query = "INSERT INTO utilisateurs (nom, email, motDePasse, type) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, userType);
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Utilisateur enregistré avec succès !");
            } else {
                System.out.println("Échec de l'enregistrement.");
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }
}
