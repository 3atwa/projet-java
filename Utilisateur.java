package users;

public class Utilisateur {
private String nom;
private String email;
private String motDePasse;


public Utilisateur(String nom, String email, String motDePasse) {
	this.nom=nom;
	this.email=email;
	this.motDePasse=motDePasse;
}

public String getNom() {
	return nom;
}

public String getEmail() {
	return email;
}

public String getMotDePasse() {
	return motDePasse;
}

public void setNom(String nom) {
	this.nom=nom;
}

public void setEmail(String email) {
	this.email=email;
}

public void setMotDePasse(String motDePasse) {
	this.motDePasse=motDePasse;
}




}
