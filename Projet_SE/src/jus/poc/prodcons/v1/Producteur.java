package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	private int deviationTempsDeTraitement;
	private int moyenneTempsDeTraitement;
	private int identification;
	private int nombreDeMessages;
	private ProdCons buffer;

	protected Producteur(ProdCons buffer, int identification, int nombreDeMessages, Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
		this.buffer = buffer;
		this.identification = identification;
		this.nombreDeMessages = nombreDeMessages;
	}

	@Override
	public int deviationTempsDeTraitement() {
		// TODO Auto-generated method stub
		return deviationTempsDeTraitement;
	}

	@Override
	public int identification() {
		// TODO Auto-generated method stub
		return identification;
	}

	@Override
	public int moyenneTempsDeTraitement() {
		// TODO Auto-generated method stub
		return moyenneTempsDeTraitement;
	}

	@Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return nombreDeMessages;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < nombreDeMessages; i++) {
			try {
				sleep(Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement));
				MessageX messageProd = new MessageX(i, identification);
				buffer.put(this, messageProd);
				System.out.println("Le producteur " + this.identification + " produit le message " + i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
