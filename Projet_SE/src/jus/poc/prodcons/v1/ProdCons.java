package jus.poc.prodcons.v1;

import java.util.ArrayList;
import java.util.List;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	List<MessageX> buffer;
	private int tailleMax;
	private int consummed = 0;
	private int produced = 0;

	public ProdCons(int nbBuffer) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
	}

	@Override
	public int enAttente() {
		return buffer.size();
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception, InterruptedException {
		MessageX message;
		while (enAttente() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		message = buffer.remove(0);
		notifyAll();
		consummed++;
		System.out.println("RETRAIT :  "+message+ " PAR " + arg0.identification());
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
		notifyAll();
		produced++;
		System.out.println("DEPOT :  "+arg1.toString()+ " PAR " + arg0.identification());
	}

	@Override
	public int taille() {
		return buffer.size();
	}

	public int getprod() {
		return this.produced;
	}

	public int getcons() {
		return this.consummed;
	}
}