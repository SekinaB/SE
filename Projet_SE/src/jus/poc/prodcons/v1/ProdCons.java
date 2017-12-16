package jus.poc.prodcons.v1;

import java.util.ArrayList;
import java.util.List;

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

	public ProdCons(int nbBuffer, int nbProd) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		this.nbProd = nbProd;
	}

	@Override
	public int enAttente() {
		return buffer.size();
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception, InterruptedException {
		while (enAttente() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		MessageX message;
		message = buffer.remove(0);
		nbConsummed++;
		notifyAll();
		return message;
	}

	@Override
	public synchronized void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		while (taille() >= tailleMax) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.add(taille(), (MessageX) arg1);

		nbProduced++;
		notifyAll();
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
