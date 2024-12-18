package courses;

import users.Formateur;

public class Formation {
	private String titre;
	private String description;
	private Formateur formateur;
	private double prix;
		public Formation(String titre,String description,Formateur formateur,double prix) 
		
	{
			this.titre=titre;
			this.description=description;
			this.formateur=formateur;
			this.prix=prix;
	}
	
	
	/////getters
		
	public	String getTitre() {
		return titre;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	
	public Formateur getFormateur() {
		return formateur;
	}
	
	public double getPrix() {
		return prix;
	}
	
	///setters
	
	public void setTitre(String titre) {
		this.titre=titre;
	}

	public void setDescription(String description) {
		this.description=description;
	}

	public void setFormateur(Formateur formateur) {
		this.formateur=formateur;
	}
	
	public void setPrix(double prix) {
		this.prix=prix;
		
	}
		
}
