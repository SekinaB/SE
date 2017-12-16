package jus.poc.prodcons.v2;

import java.util.ArrayList;
import java.util.List;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import java.util.concurrent.Semaphore;

public class ProdCons implements Tampon {

	List<MessageX> buffer;
	private int tailleMax;
	private int consummed = 0;
	private int produced = 0;
	Semaphore notFull;
	Semaphore notEmpty;
	Semaphore mutex;

	public ProdCons(int nbBuffer) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
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

		message = buffer.remove(0);
		consummed++;
		
		System.out.println("RETRAIT :  " + message + " PAR " + arg0.identification());
		mutex.release();
		notFull.release();
		
		return message;

	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		notFull.acquire();
		//System.out.println("PUT NOTFULL " + arg0.identification());
		mutex.acquire();
		//System.out.println("PUT MUTEX  " + arg0.identification());
		buffer.add(taille(), (MessageX) arg1);
		produced++;
		System.out.println("DEPOT :  " + arg1.toString() + " PAR " + arg0.identification());
		mutex.release();
		//System.out.println("PUT MUTEX Released " + arg0.identification());
		notEmpty.release();
		//System.out.println("Put FIFO Released by" + arg0.identification());
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