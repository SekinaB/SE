package jus.poc.prodcons.v3;

import java.util.Date;

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
	private Observateur observateur;

	protected Producteur(ProdCons buffer, int identification, int nombreDeMessages, Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.deviationTempsDeTraitement = deviationTempsDeTraitement;
		this.moyenneTempsDeTraitement = moyenneTempsDeTraitement;
		this.identification = identification;
		this.nombreDeMessages = nombreDeMessages;
		this.buffer = buffer;
		this.observateur = observateur;
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
				MessageX messageProd = new MessageX(i, identification);
				int tempsDeTraitement = Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
				observateur.productionMessage(this, messageProd, tempsDeTraitement);
				buffer.put(this, messageProd);
				Date d = new Date();
				System.out.println("Le producteur " + this.identification + " produit le message " + i + " " + d.getTime());
				sleep(tempsDeTraitement);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buffer.finProducteur();
		if (TestProdCons.DEBUG) {
			System.out.println("Le producteur " + this.identification + " is dead ");

		}
	}

}
