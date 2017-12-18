package jus.poc.prodcons.v4;

import java.util.Date;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int identifiantProducteur; // identifiant du producteur
	private int identifiantMessage; // identifiant du message dans ceux du prod
	public Date date; // temps de retrait de message
	private int nbExemplaire; // nombre d'exemplaire du message dans le buffer

	public MessageX(int identifiantMessage, int identifiantProducteur, int nbExemplaire) {
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
		this.nbExemplaire = nbExemplaire;
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

	/**
	 * Decrement le variable nbExemplaire a l'appel de la fonction
	 */
	public void lireExemplaire() {
		this.nbExemplaire--;
	}

	/**
	 * Retourne l'identifiant du producteur, createur du message
	 * 
	 * @return int identifiant du producteur
	 */
	public int idProd() {
		return this.identifiantProducteur;
	}

	/**
	 * Indique si il ne reste qu'un seul exemplaire du message dans le buffer
	 * 
	 * @return true si nbExemplaire == 1 et false sinon
	 */
	public boolean enUnExemplaire() {
		return (this.nbExemplaire == 1);
	}

	/**
	 * Retourne le nombre d'exemplaire du message restant dans le buffer
	 * 
	 * @return nombre d'exemplaire restant
	 */
	public int getExemplaire() {
		return this.nbExemplaire;
	}

}
