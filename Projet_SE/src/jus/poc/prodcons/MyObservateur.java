package jus.poc.prodcons.v6;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jus.poc.prodcons.*;

import jus.poc.prodcons.ControlException;

public class MyObservateur {

	private boolean coherent = true;
	private int nbproducteurs;
	private int nbconsommateurs;
	private int nbBuffers;
	
	List<_Consommateur> listCons;
	List<_Producteur> listProd;
	List <Message> buffer;
	private Hashtable<_Producteur, Message> messageProd;
	private Hashtable<_Consommateur, Message> messageCons;
	
	
	public boolean coherent() {
		return this.coherent;
	}
	
	public void consommationMessage(_Consommateur c, Message m, int tempsDeTraitement) throws ControlException{
		
		
		// verifier que les arguments ne sont pas vides
		if((c==null) || (m==null) || (tempsDeTraitement<=0)) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}
		
		// verifier que le consommateur existe dans listCons
		if (listCons.contains(c)== false) {
			coherent = false;
			throw new ControlException(c.getClass(), "consommationMessage");
		}
		
		//verifier que le message m a été retiré de la Hashtable messageCons
		if(messageCons.remove(c, m)==false) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}
			
			
	}
	
	public void depotMessage(_Producteur p, Message m) throws ControlException{
		
		// verifier que les arguments sont valides
		if((p==null) || (m==null)) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}
		
		// verifier que le producteur existe dans listProd
		if (listProd.contains(p)== false) {
			coherent = false;
			throw new ControlException(p.getClass(), "depotMessage");
		}
		
		// verifier si le producteur n'a pas deja depose le message m
		if (messageProd.remove(p, m) == false) {
			throw new ControlException(this.getClass(), "depotMessage");
		}
		
		buffer.add(m);
		
		if(buffer.size()>nbBuffers){
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}
	}
	
	public void init(int nbproducteurs, int nbconsommateurs, int nbBuffers) throws ControlException{
		
		if (nbproducteurs <=0 || nbconsommateurs <=0 || nbBuffers<=0) {
			coherent = false;
			throw new ControlException(null, "init");
		}
		
		this.nbproducteurs = nbproducteurs;
		this.nbconsommateurs = nbconsommateurs;
		this.nbBuffers = nbBuffers;
		
		listProd = new ArrayList<_Producteur>();
		listCons = new ArrayList<_Consommateur>();
		buffer = new ArrayList<Message>();
		
		this.messageProd = new Hashtable<_Producteur, Message>();
		this.messageCons = new Hashtable<_Consommateur, Message>();
	}
	
	public void newConsommateur (_Consommateur c) throws ControlException {
		
		if (c == null) {
			coherent = false;
			throw new ControlException(this.getClass(), "newConsommateur");
		}
		listCons.add(c);
		
		if(listCons.size()>nbconsommateurs) {
			coherent = false;
			throw new ControlException(this.getClass(), "newConsommateur");
		}
	}
	
	public void newProducteur (_Producteur p) throws ControlException{
		
		if (p == null) {
			coherent = false;
			throw new ControlException(this.getClass(), "newProducteur");
		}
		
		listProd.add(p);
		
		if(listProd.size()>nbproducteurs) {
			coherent = false;
			throw new ControlException(this.getClass(), "newProducteurs");
		}
	}
	
	public void productionMessage (_Producteur p, Message m, int tempsDeTraitement) throws ControlException{
		
		if((p==null) || (m==null) || (tempsDeTraitement<=0)) {
			coherent = false;
			throw new ControlException(this.getClass(), "productionMessage");	
		}
		messageProd.put(p, m);
	}
	
	public void retraitMessage (_Consommateur c, Message m) throws ControlException {
		Message message;
		
		if((c==null) || (m==null)) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}
		
		message = buffer.get(0);
		if (message!= m) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}
		
		messageCons.put(c, m);
		
		
	}
	
}

