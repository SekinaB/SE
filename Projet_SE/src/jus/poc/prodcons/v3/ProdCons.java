package jus.poc.prodcons.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private List<MessageX> buffer;
	private int tailleMax;
	private int nbProd;
	private int nbConsummed = 0;
	private int nbProduced = 0;
	private Semaphore notFull;
	private Semaphore notEmpty;
	private Semaphore mutex;
	private Observateur observateur;

	public ProdCons(int nbBuffer, int nbProd, Observateur observateur) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		this.nbProd = nbProd;
		notFull = new Semaphore(nbBuffer);
		notEmpty = new Semaphore(0);
		mutex = new Semaphore(1);
		this.observateur = observateur;
	}

	public ProdCons(int nbBuffer, int nbProd) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		this.nbProd = nbProd;
		notFull = new Semaphore(nbBuffer);
		notEmpty = new Semaphore(0);
		mutex = new Semaphore(1);
	}

	@Override
	public int enAttente() {
		return buffer.size();
	}

	@Override
	public Message get(_Consommateur cons) throws Exception, InterruptedException {
		MessageX message;

		notEmpty.acquire();
		mutex.acquire();
		synchronized (this) {
			message = buffer.remove(0);
			nbConsummed++;
			observateur.retraitMessage(cons, message);
		}
		mutex.release();
		notFull.release();
		return message;
	}

	@Override
	public void put(_Producteur prod, Message message) throws Exception, InterruptedException {
		notFull.acquire();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT NOTFULL " + prod.identification());
		}
		mutex.acquire();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT MUTEX  " + prod.identification());
		}
		synchronized (this) {
			buffer.add(taille(), (MessageX) message);
			nbProduced++;
			observateur.depotMessage(prod, message);
		}
		mutex.release();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT MUTEX Released by " + prod.identification());
		}
		notEmpty.release();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT FIFO Released by " + prod.identification());
		}
	}

	@Override
	public int taille() {
		return buffer.size();
	}

	public void finProducteur() {
		this.nbProd--;
	}

	public boolean producteurAlive() {
		return !(nbProd == 0);
	}

	public int getProduced() {
		return this.nbProduced;
	}

	public int getConsummed() {
		return this.nbConsummed;
	}

}
