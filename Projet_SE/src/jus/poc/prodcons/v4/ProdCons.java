package jus.poc.prodcons.v4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private List<MessageX> buffer;
	private int nbProd; // Nombre de producteurs qui accedent au buffer
	private int nbCons; // Nombre de consommateurs qui accedent au buffer
	private int nbConsummed = 0; // Nombre de messages consommes pour le moment
	private int nbProduced = 0; // Nombre de messages produits pour le moment
	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore mutex;
	Semaphore[] producteurs; // Semaphore controlant les producteurs
	Semaphore[] consommateurs; // Semaphore controlant les consommateurs

	public ProdCons(int nbBuffer, int nbProd, int nbCons) {
		this.buffer = new ArrayList<MessageX>();
		this.nbProd = nbProd;
		this.nbCons = nbCons;
		notFull = new Semaphore(nbBuffer);
		notEmpty = new Semaphore(0);
		mutex = new Semaphore(1);
		producteurs = new Semaphore[nbProd];
		for (int i = 0; i < nbProd; i++) {
			producteurs[i] = new Semaphore(1);
		}
		consommateurs = new Semaphore[nbCons];
		for (int i = 0; i < nbCons; i++) {
			consommateurs[i] = new Semaphore(1);
		}
	}

	@Override
	/**
	 * @return le nombre de message actuellement dans le buffer
	 */
	public int enAttente() {
		return buffer.size();
	}

	@Override
	/**
	 * Retire le premier message du buffer
	 * 
	 * @return le message retire
	 */
	public Message get(_Consommateur cons) throws Exception, InterruptedException {
		MessageX message;

		// On bloque le consommateur qui ne peut plus acceder au buffer
		consommateurs[cons.identification()].acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET CONSO BLOCKED " + cons.identification());
		}
		// Si le buffer est vide, on attend
		notEmpty.acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET NOTEMPTY ACQUIRED " + cons.identification());
		}
		// On bloque les autres processus
		mutex.acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET MUTEX ACQUIRED " + cons.identification());
		}
		synchronized (this) {
			// On prend le permier message du buffer donc celui a l'indice 0
			message = buffer.get(0);
			if (message.enUnExemplaire()) {
				// On enleve le permier message du buffer
				message = buffer.remove(0);
				// On libere le producteur qui peut continuer sa production
				producteurs[message.idProd()].release();
				if (TestProdCons.FLAG_DEBUG) {
					System.out.println("GET PROD RELEASED " + message.idProd());
				}
				// On libere les consommateurs
				for (int i = 0; i < nbCons; i++) {
					consommateurs[i].release();
				}
				if (TestProdCons.FLAG_DEBUG) {
					System.out.println("GET ALL CONSO RELEASED");
				}
			} else {
				// On prend un exemplaire un message
				message.lireExemplaire();
			}
			// On initialise la date du retait du message
			message.setDate();
			// On augmente le nombre de message consommes
			nbConsummed++;

		}
		// On libere le semaphore
		mutex.release();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET MUTEX RELEASED " + cons.identification());
		}
		// On libere la case du buffer nbExemplaire fois
		synchronized (this) {
			for (int i = 0; i < message.getExemplaire(); i++) {
				notFull.release();
			}
		}
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET NOTFULL RELEASED " + cons.identification());
		}
		return message;
	}

	@Override
	/**
	 * Met le message a la fin buffer
	 * 
	 * @return le message retire
	 */
	public void put(_Producteur prod, Message message) throws Exception, InterruptedException {

		// On bloque le producteur qui ne peut plus acceder au buffer
		producteurs[prod.identification()].acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT PROD BLOCKED " + prod.identification());
		}
		// Si le buffer est plein, on attend
		MessageX messageX = (MessageX) message;
		notFull.acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT NOTFULL ACQUIRED " + prod.identification());
		}
		// On bloque les autres processus
		mutex.acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT MUTEX ACQUIRED " + prod.identification());
		}
		synchronized (this) {
			// On met message a la fin du buffer donc a l'indice taille()
			buffer.add(taille(), messageX);
			// On met message a la fin du buffer donc a l'indice taille()
			nbProduced += messageX.getExemplaire();
		}
		// On libere le semaphore
		mutex.release();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT MUTEX RELEASED " + prod.identification());
		}
		// On avertit que le buffer n'est plus vide nbExemplaire fois
		synchronized (this) {
			for (int i = 0; i < messageX.getExemplaire(); i++) {
				notEmpty.release();
			}
		}
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT NOTEMPTY RELEASED " + prod.identification());
		}
	}

	@Override
	/**
	 * Retourne la taille actuelle du buffer
	 * 
	 * @return taille du buffer
	 */
	public int taille() {
		return buffer.size();
	}

	/**
	 * Decremente le nombre de producteurs qui accedent au buffer
	 * 
	 */
	public void finProducteur() {
		this.nbProd--;
	}

	/**
	 * Retourne Vrai si il reste des producteurs, dont l'execution n'a pas ete
	 * terminee, accedant au buffer
	 * 
	 * @return vrai si il reste des producteurs accedant au buffer
	 */
	public boolean producteurAlive() {
		return !(nbProd == 0);
	}

	/**
	 * @return le nombre de messages produits au total
	 */
	public int getProduced() {
		return this.nbProduced;
	}

	/**
	 * @return le nombre de messages consommes au total
	 */
	public int getConsummed() {
		return this.nbConsummed;
	}

}
