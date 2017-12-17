package jus.poc.prodcons.v5;

import java.util.ArrayList;
import java.util.List;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import java.util.concurrent.locks.*;
import java.util.Date;

public class ProdCons implements Tampon {

	List<MessageX> buffer;
	private int tailleMax;
	private int consummed = 0;
	private int produced = 0;
	Observateur obs;
	
	final Lock lock;
	final Condition notFull;
	final Condition notEmpty;
	
	public ProdCons(int nbBuffer) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		
		lock = new ReentrantLock();
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
	}

	public ProdCons(int nbBuffer, Observateur obs) {
		this.buffer = new ArrayList<MessageX>();
		this.tailleMax = nbBuffer;
		lock = new ReentrantLock();
		notFull = lock.newCondition();
		notEmpty = lock.newCondition();
		this.obs=obs;
	}
	@Override
	public int enAttente() {
		return buffer.size();
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		
		Date date = new Date();
		double seconds;
		MessageX message;
		
		lock.lock();
		try {
			while (enAttente() == 0) {
				notEmpty.await();
			}	
			message = buffer.remove(0);
			seconds = date.getTime();
			consummed++;
			obs.retraitMessage(arg0, message);
			notFull.signal();
			
			System.out.println("RETRAIT :  " + message + " PAR " + arg0.identification() + " AT " + seconds);
			return message;
		} finally {
			lock.unlock();
		}	
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		
		Date date = new Date();
		double seconds;
		lock.lock();
		
		try {
			while(taille() >= tailleMax) {
				notFull.await();
			}
			buffer.add(taille(), (MessageX) arg1);
			seconds = date.getTime();
			produced++;
			obs.depotMessage(arg0, arg1);
			System.out.println("DEPOT :  " + arg1.toString() + " PAR " + arg0.identification() + " AT " + seconds);
		
			notEmpty.signal();
		} finally {
			lock.unlock();
		}	
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
