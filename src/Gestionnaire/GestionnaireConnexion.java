package Gestionnaire;

import database.DatabaseConnection;
import exceptions.UtilisateurNonTrouveException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class GestionnaireConnexion {

    public static void gestionnaireConnexion(Scanner sc) {
        System.out.println("Email : ");
        String email = sc.nextLine();
        System.out.println("Password : ");
        String password = sc.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String emailCheckQuery = "SELECT * FROM utilisateurs WHERE email = ?";
            try (PreparedStatement emailCheckStmt = connection.prepareStatement(emailCheckQuery)) {
                emailCheckStmt.setString(1, email);
                try (ResultSet emailCheckResult = emailCheckStmt.executeQuery()) {
                    if (!emailCheckResult.next()) {
                        throw new UtilisateurNonTrouveException("Adresse email non trouvée. Veuillez vérifier ou créer un compte.");
                    }
                }
            }

            // CHantinuer LogIM
            String loginQuery = "SELECT * FROM utilisateurs WHERE email = ? AND motDePasse = ?";
            try (PreparedStatement loginStmt = connection.prepareStatement(loginQuery)) {
                loginStmt.setString(1, email);
                loginStmt.setString(2, password);

                try (ResultSet loginResult = loginStmt.executeQuery()) {
                    if (loginResult.next()) {
                        String userType = loginResult.getString("type");
                        int userId = loginResult.getInt("id");
                        System.out.println("Connexion réussie !");
                        if ("formateur".equals(userType)) {
                            System.out.println("Bienvenue formateur !");
                            GestionnaireFormateur.gestionnaireFormateur(sc, userId);
                        } else if ("etudiant".equals(userType)) {
                            GestionnaireEtudiant.gestionnaireEtudiant(sc, userId);
                        }
                    } else {
                        System.out.println("Mot de passe invalide.");
                    }
                }
            }
        } catch (UtilisateurNonTrouveException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Une erreur est survenue : " + e.getMessage());
        }
    }
}
