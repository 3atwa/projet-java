package main;

import java.util.Scanner;

import Gestionnaire.GestionnaireConnexion;
import Gestionnaire.GestionnaireInscription;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        try {
        	while(true) {
                System.out.println("Veuillez choisir votre choix :");
                System.out.println("1: Connecter au plateforme");
                System.out.println("2: Inscrire au plateforme");
                System.out.println("3: Quitter");

                int choix = 0;
                try {
                    choix = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide !");
                }

                switch (choix) {
                    case 1:
                        GestionnaireConnexion.gestionnaireConnexion(sc);
                        break;
                    case 2:
                        GestionnaireInscription.gestionnaireInscription(sc);
                        break;
                    case 3:
                        System.out.println("Au revoir !");
                        return;
                    default:
                        System.out.println("Veuillez entrer un nombre entre 1 et 3 !");
                }
        	}
            
        } finally {
            sc.close();
        }
    }
}

