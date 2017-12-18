package jus.poc.prodcons.v6;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class MyObservateur {
	private boolean coherent;

	private int nbproducteurs;
	private int nbconsommateurs;
	private int nbBuffers;

	private List<_Producteur> listProd;
	private List<_Consommateur> listCons;
	private List<Message> buffer;

	private Hashtable<_Producteur, Message> messageProd;
	private Hashtable<_Consommateur, Message> messageCons;

	public boolean coherent() {
		return this.coherent;
	}

	public void init(int nbproducteurs, int nbconsommateurs, int nbBuffers) throws ControlException {
		// Verification des parametres
		if (nbproducteurs <= 0 || nbconsommateurs <= 0 || nbBuffers <= 0) {
			coherent = false;
			throw new ControlException(this.getClass(), "init");
		}

		this.nbproducteurs = nbproducteurs;
		this.nbconsommateurs = nbconsommateurs;
		this.nbBuffers = nbBuffers;

		this.listProd = new ArrayList<_Producteur>();
		this.listCons = new ArrayList<_Consommateur>();
		this.buffer = new ArrayList<Message>();

		this.messageProd = new Hashtable<_Producteur, Message>();
		this.messageCons = new Hashtable<_Consommateur, Message>();

		coherent = true;
	}

	public final void productionMessage(_Producteur p, Message m, int tempsDeTraitement) throws ControlException {
		if (p == null || m == null || tempsDeTraitement <= 0) {
			coherent = false;
			throw new ControlException(this.getClass(), "productionMessage");
		}
		if (!listProd.contains(p)) {
			coherent = false;
			throw new ControlException(this.getClass(), "productionMessage");
		}
		messageProd.put(p, m);
	}

	public final void depotMessage(_Producteur p, Message m) throws ControlException {

		// verifier que les arguments sont valides
		if (p == null || m == null) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		// verifier que le producteur existe dans listProd
		if (!listProd.contains(p)) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		// verifier si le producteur n'a pas deja depose le message m
		if (!messageProd.remove(p, m)) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		messageProd.put(p, m);

		if (buffer.size() > nbBuffers) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		buffer.add(m);
	}

	public final void retraitMessage(_Consommateur c, Message m) throws ControlException {
		// verifier que les arguments sont valides
		if (c == null || m == null) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}
		if (!listCons.contains(c) || !messageProd.contains(m)) {
			coherent = false;
			throw new ControlException(this.getClass(), "retraitMessage");
		}
		Message message;

		message = buffer.remove(0);
		if (message != m) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}
		
		messageCons.put(c, m);

	}

	public void consommationMessage(_Consommateur c, Message m, int tempsDeTraitement) throws ControlException {

		// verifier que les arguments ne sont pas vides
		if (c == null || m == null || tempsDeTraitement <= 0) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}

		// verifier que le consommateur existe dans listCons
		if (!listCons.contains(c)) {
			coherent = false;
			throw new ControlException(c.getClass(), "consommationMessage");
		}

		// verifier que le message m a été retiré de la Hashtable messageCons
		if (!messageCons.remove(c, m)) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}

	}

	public final void newProducteur(_Producteur p) throws ControlException {
		if (listProd.size() == nbproducteurs || p == null || listProd.contains(p)) {
			coherent = false;
			throw new ControlException(this.getClass(), "newProducteur");
		}
		listProd.add(p);
	}

	public final void newConsommateur(_Consommateur c) throws ControlException {
		if (listCons.size() == nbconsommateurs || c == null || listCons.contains(c)) {
			coherent = false;
			throw new ControlException(this.getClass(), "newConsommateur");
		}
		listCons.add(c);
	}
}
