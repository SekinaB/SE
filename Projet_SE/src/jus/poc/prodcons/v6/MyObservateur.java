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
		// Verification des parametres
		if (p == null || m == null || tempsDeTraitement <= 0) {
			coherent = false;
			throw new ControlException(this.getClass(), "productionMessage");
		}
		// Verification de la presence de p dans la liste de producteurs
		if (!listProd.contains(p)) {
			coherent = false;
			throw new ControlException(this.getClass(), "productionMessage");
		}
		messageProd.put(p, m);
	}

	public final void depotMessage(_Producteur p, Message m) throws ControlException {
		// Verification des parametres
		if (p == null || m == null) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		// Verification de la presence de p dans la liste de producteurs
		if (!listProd.contains(p)) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		// Verification de la non repetition d'un meme message
		if (!messageProd.remove(p, m)) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		messageProd.put(p, m);

		// Verification de la taille du buffer
		if (buffer.size() >= nbBuffers) {
			coherent = false;
			throw new ControlException(this.getClass(), "depotMessage");
		}

		// Ajout du message
		buffer.add(m);
	}

	public final void retraitMessage(_Consommateur c, Message m) throws ControlException {
		// Verification des parametres
		if (c == null || m == null) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}

		// Verification de la presence de p dans la liste de producteurs et du
		// message m dans le liste des message produits
		if (!listCons.contains(c) || !messageProd.contains(m)) {
			coherent = false;
			throw new ControlException(this.getClass(), "retraitMessage");
		}
		
		Message message;
		// Retrait du premier message du buffer
		message = buffer.remove(0);
		
		// Verification de la valeur du message
		if (message != m) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}

		// Ajout du message dans la liste des messages consommes
		messageCons.put(c, m);

	}

	public void consommationMessage(_Consommateur c, Message m, int tempsDeTraitement) throws ControlException {
		// Verification des parametres
		if (c == null || m == null || tempsDeTraitement <= 0) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}

		// Verification de la presence de c dans la liste des consommateurs
		if (!listCons.contains(c)) {
			coherent = false;
			throw new ControlException(c.getClass(), "consommationMessage");
		}

		// Verification de la presence de m dans messageCons
		if (!messageCons.remove(c, m)) {
			coherent = false;
			throw new ControlException(this.getClass(), "consommationMessage");
		}

	}

	public final void newProducteur(_Producteur p) throws ControlException {
		// Verification des parametres
		if (listProd.size() == nbproducteurs || p == null || listProd.contains(p)) {
			coherent = false;
			throw new ControlException(this.getClass(), "newProducteur");
		}
		// Ajout de p dans la liste des producteurs
		listProd.add(p);
	}

	public final void newConsommateur(_Consommateur c) throws ControlException {
		// Verification des parametres
		if (listCons.size() == nbconsommateurs || c == null || listCons.contains(c)) {
			coherent = false;
			throw new ControlException(this.getClass(), "newConsommateur");
		}
		// Ajout de c dans la liste des  consommateurs
		listCons.add(c);
	}
}
