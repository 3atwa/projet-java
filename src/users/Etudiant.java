package users;

import java.util.ArrayList;
import java.util.List;

import courses.Formation;
import exceptions.FormationDejaInscriteException;

public class Etudiant extends Utilisateur {
    private List<Formation> inscriptions;

    public Etudiant(String nom, String email, String motDePasse) {
        super(nom, email, motDePasse);
        this.inscriptions = new ArrayList<>();
    }

    public List<Formation> getInscriptions() {
        return inscriptions;
    }

    public void sinscrireFormation(Formation formation) throws FormationDejaInscriteException {
        if (inscriptions.contains(formation)) {
            throw new FormationDejaInscriteException("Vous êtes déjà inscrit à cette formation.");
        }
        inscriptions.add(formation);
    }
}