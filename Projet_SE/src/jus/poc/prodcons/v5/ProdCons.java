package jus.poc.prodcons.v5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v3.TestProdCons;

public class ProdCons implements Tampon {

	private List<MessageX> buffer;
	private int tailleMax;
	private int nbProd; // Nombre de producteurs qui accedent au buffer
	private int nbConsummed = 0; // Nombre de messages consommes pour le moment
	private int nbProduced = 0; // Nombre de messages produits pour le moment
	private Observateur observateur;

	private final Lock mutex = new ReentrantLock();
	private final Condition notFull = mutex.newCondition();
	private final Condition notEmpty = mutex.newCondition();

	public ProdCons(int nbBuffer, int nbProd, Observateur observateur) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		this.nbProd = nbProd;
		this.observateur = observateur;
	}

	public ProdCons(int nbBuffer, int nbProd) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		this.nbProd = nbProd;
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

		// On recupere la section critique conditionnelles
		mutex.lock();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("GET GOT THE SCC " + cons.identification());
		}
		try {
			// Tant que le buffer est vide, on attend
			while (enAttente() <= 0) {
				notEmpty.await();
			}
			synchronized (this) {
				// On enleve le permier message du buffer donc celui a l'indice
				// 0
				message = buffer.remove(0);
				// On initialise la date du retait du message
				message.setDate();
				// On augmente le nombre de message consommes
				nbConsummed++;
				// Control avec l'observateur
				observateur.retraitMessage(cons, message);
			}
			// On signalise que le buffer n'est plus remplis
			notFull.signal();
			if (TestProdCons.FLAG_DEBUG) {
				System.out.println("GET NOTFULL SIGNAL " + cons.identification());
			}
			return message;
		} finally {
			// On libere la section critique conditionnelles
			mutex.unlock();
			if (TestProdCons.FLAG_DEBUG) {
				System.out.println("GET FREE THE SCC " + cons.identification());
			}
		}
	}

	@Override
	/**
	 * Met le message a la fin buffer
	 * 
	 * @return le message retire
	 */
	public void put(_Producteur prod, Message message) throws Exception, InterruptedException {
		// On recupere la section critique conditionnelles
		mutex.lock();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("PUT GOT THE SCC " + prod.identification());
		}
		try {
			// Tant que le buffer est rempli, on attend
			while (taille() >= tailleMax) {
				notFull.await();
			}
			synchronized (this) {
				// On met message a la fin du buffer donc a l'indice taille()
				buffer.add(taille(), (MessageX) message);
				// Control avec l'observateur
				observateur.depotMessage(prod, message);
				// On met message a la fin du buffer donc a l'indice taille()
				nbProduced++;
			}
			// On avertit que le buffer n'est plus vide
			notEmpty.signal();
			if (TestProdCons.FLAG_DEBUG) {
				System.out.println("PUT NOTEMPTY SIGNAL " + prod.identification());
			}
		} finally {
			// On libere la section critique conditionnelles
			mutex.unlock();
			if (TestProdCons.FLAG_DEBUG) {
				System.out.println("PUT FREE THE SCC " + prod.identification());
			}
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
