package jus.poc.prodcons.v3;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
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
		this.buffer = buffer;
		this.identification = identification;
		this.nombreDeMessages = nombreDeMessages;
		this.moyenneTempsDeTraitement=moyenneTempsDeTraitement;
		this.deviationTempsDeTraitement=deviationTempsDeTraitement;
	}

	@Override
	public int deviationTempsDeTraitement() {
		return deviationTempsDeTraitement;
	}

	@Override
	public int identification() {
		return identification;
	}

	@Override
	public int moyenneTempsDeTraitement() {
		return moyenneTempsDeTraitement;
	}

	@Override
	public int nombreDeMessages() {
		return nombreDeMessages;
	}

	@Override
	public void run() {
		for (int i = 0; i < nombreDeMessages; i++) {
			try {
				int productionDelai = Aleatoire.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
				MessageX messageProd = new MessageX(i, identification);
				sleep(productionDelai);
				buffer.put(this, messageProd);
				this.observateur.productionMessage(this, messageProd, productionDelai);
				// System.out.println("Le producteur " + this.identification + "
				// produit le message " + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}