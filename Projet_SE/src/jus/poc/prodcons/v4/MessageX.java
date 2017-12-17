package jus.poc.prodcons.v4;

import java.util.Date;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int identifiantProducteur;
	private int identifiantMessage;
	public Date date;
	private int nbExemplaire;

	public MessageX(int identifiantMessage, int identifiantProducteur, int nbExemplaire) {
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
		this.nbExemplaire = nbExemplaire;
	}

	public String toString() {
		if (TestProdCons.FLAG_TIME) {
			return "Message " + identifiantMessage + " of Producteur " + identifiantProducteur + " at "
					+ (date.getTime() - TestProdCons.START_TIME.getTime());
		} else {
			return "Message " + identifiantMessage + " of Producteur " + identifiantProducteur;
		}
	}

	public void setDate() {
		this.date = new Date();
	}
	
	public void lireExemplaire(){
		this.nbExemplaire--;
	}
	
	public int idProd(){
		return this.identifiantProducteur;
	}
	
	public boolean enUnExemplaire(){
		return (this.nbExemplaire == 1);
	}
	
	public int getExemplaire(){
		return this.nbExemplaire;
	}

}
