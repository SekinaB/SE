package jus.poc.prodcons.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private List<MessageX> buffer;
	private int tailleMax;
	private int nbProd;
	private int nbConsummed = 0;
	private int nbProduced = 0;
	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore mutex;

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
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		MessageX message;

		notEmpty.acquire();
		mutex.acquire();
		synchronized (this){
		message = buffer.remove(0);
		nbConsummed++;
		}
		mutex.release();
		notFull.release();
		return message;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		notFull.acquire();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT NOTFULL " + arg0.identification());
		}
		mutex.acquire();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT MUTEX  " + arg0.identification());
		}
		synchronized (this){
		buffer.add(taille(), (MessageX) arg1);
		nbProduced++;
		}
		mutex.release();
		if (TestProdCons.DEBUG) {
			System.out.println("PUT MUTEX Released " + arg0.identification());
		}
		notEmpty.release();
		if (TestProdCons.DEBUG) {
			System.out.println("Put FIFO Released by" + arg0.identification());
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
