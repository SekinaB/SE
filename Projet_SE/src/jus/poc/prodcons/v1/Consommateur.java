package jus.poc.prodcons.v1;

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

	protected Consommateur(ProdCons buffer, int identification, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;
		this.identification = identification;
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
		MessageX val;
		while ((buffer.enAttente() > 0) || buffer.producteurAlive()) {
			try {
				sleep(Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement));
				val = (MessageX) buffer.get(this);
				if (val != null) {
					System.out.println("Le consommateur " + this.identification + " consomme " + val.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Le consommateur " + this.identification + " is dead ");
	}

}
