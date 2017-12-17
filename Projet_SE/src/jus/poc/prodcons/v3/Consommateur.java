package jus.poc.prodcons.v3;

import java.util.Date;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private int deviationTempsDeTraitement;
	private int moyenneTempsDeTraitement;
	private int identification;
	private int nombreDeMessages;
	private ProdCons buffer;
	private Observateur observateur;

	protected Consommateur(ProdCons buffer, int identification, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.deviationTempsDeTraitement = deviationTempsDeTraitement;
		this.moyenneTempsDeTraitement = moyenneTempsDeTraitement;
		this.identification = identification;
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
		MessageX messageCons;
		while ((buffer.enAttente() > 0) || buffer.producteurAlive()) {
			try {
				int tempsDeTraitement = Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
				messageCons = (MessageX) buffer.get(this);
				observateur.consommationMessage(this, messageCons, tempsDeTraitement);
				Date d = new Date();
				System.out.println("Le consommateur " + this.identification + " consomme " + messageCons.toString()
						+ " " + d.getTime());
				sleep(tempsDeTraitement);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (TestProdCons.DEBUG) {
			System.out.println("Le consommateur " + this.identification + " is dead ");
		}
	}

}
