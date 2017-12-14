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

	public ProdCons(int nbBuffer) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
	}

	@Override
	public int enAttente() {
		// TODO Auto-generated method stub
		return buffer.size();
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
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
		return message;
	}

	@Override
	public synchronized void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
		while (taille() < tailleMax) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.set(taille(), (MessageX) arg1);
		notifyAll();
	}

	@Override
	public int taille() {
		// TODO Auto-generated method stub
		return buffer.size();
	}

}
