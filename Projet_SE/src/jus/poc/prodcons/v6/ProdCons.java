package jus.poc.prodcons.v6;

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
	private int nbConsummed = 0; // Nombre de messages consommes pour le moment
	private int nbProduced = 0; // Nombre de messages produits pour le moment
	private Semaphore notFull;
	private Semaphore notEmpty;
	private Semaphore mutex;
	private MyObservateur myobs;

	public ProdCons(int nbBuffer, int nbProd, MyObservateur myobs) {
		this.buffer = new ArrayList<MessageX>();
		this.nbProd = nbProd;
		notFull = new Semaphore(nbBuffer);
		notEmpty = new Semaphore(0);
		mutex = new Semaphore(1);
		this.myobs = myobs;
	}

	public ProdCons(int nbBuffer, int nbProd) {
		this.buffer = new ArrayList<MessageX>();
		this.nbProd = nbProd;
		notFull = new Semaphore(nbBuffer);
		notEmpty = new Semaphore(0);
		mutex = new Semaphore(1);
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

		// Si le buffer est vide, on attend
		notEmpty.acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET NOTEMPTY ACQUIRED" + cons.identification());
		}
		// On bloque les autres processus
		mutex.acquire();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET MUTEX ACQUIRED " + cons.identification());
		}
		synchronized (this) {
			// On enleve le permier message du buffer donc celui a l'indice 0
			message = buffer.remove(0);
			// On initialise la date du retait du message
			message.setDate();
			// On augmente le nombre de message consommes
			nbConsummed++;
			// Control avec notre observateur
			myobs.retraitMessage(cons, message);
		}
		// On libere le semaphore
		mutex.release();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET MUTEX RELEASED " + cons.identification());
		}
		// On libere une case du buffer
		notFull.release();
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
		// Si le buffer est plein, on attend
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
			buffer.add(taille(), (MessageX) message);
			// Control avec notre observateur
			myobs.depotMessage(prod, message);
			// On met message a la fin du buffer donc a l'indice taille()
			nbProduced++;
		}
		// On libere le semaphore
		mutex.release();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT MUTEX RELEASED " + prod.identification());
		}
		// On avertit que le buffer n'est plus vide
		notEmpty.release();
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
