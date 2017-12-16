package jus.poc.prodcons.v4;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int identifiantProducteur;
	private int identifiantMessage;
	private int nbLectures;
	
	public MessageX(int identifiantMessage, int identifiantProducteur){
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
	}
	
	public MessageX(int identifiantMessage, int identifiantProducteur, int nombreMoyenNbExemplaire, int deviationNombreMoyenNbExemplaire){
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
		this.nbLectures=Aleatoire.valeur(nombreMoyenNbExemplaire,deviationNombreMoyenNbExemplaire);
	}
	
	public String toString(){
		return "le message " + identifiantMessage + " du Producteur " + identifiantProducteur + " copies " + this.getnbLectures();  
	}
	public int getnbLectures(){
		return this.nbLectures;
	}
	public void reading() {
		this.nbLectures--;
	}
	
	
}
