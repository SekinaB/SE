package jus.poc.prodcons.v3;

import java.util.Date;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.v1.TestProdCons;

public class MessageX implements Message {

	private int identifiantProducteur; // identifiant du producteur
	private int identifiantMessage; // identifiant du message dans ceux du prod
	public Date date; // temps de retrait de message

	public MessageX(int identifiantMessage, int identifiantProducteur) {
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
	}

	/**
	 * Retourne une chaine caracterisant le message contenant sont identifiant
	 * de l'identifiant du son producteur; Si FLAG_TIME == true, on aura aussi
	 * le temps du retrait du message.
	 * 
	 * @return chaine de caractere
	 */
	public String toString() {
		if (TestProdCons.FLAG_TIME) {
			return "Message " + identifiantMessage + " of Producteur " + identifiantProducteur + " at "
					+ (date.getTime() - TestProdCons.START_TIME.getTime());
		} else {
			return "Message " + identifiantMessage + " of Producteur " + identifiantProducteur;
		}
	}

	/**
	 * Initalise le variable date à l'heure de l'appel de la fonction
	 */
	public void setDate() {
		this.date = new Date();
	}

}