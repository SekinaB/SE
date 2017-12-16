package jus.poc.prodcons.v4;

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
	private int nombreMoyenNbExemplaire;
	private int deviationNombreMoyenNbExemplaire;
	
	private ProdCons buffer;

	protected Producteur(ProdCons buffer, int identification, int nombreDeMessages, Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement, int nombreMoyenNbExemplaire, int deviationNombreMoyenNbExemplaire) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;
		this.identification = identification;
		this.nombreDeMessages = nombreDeMessages;
		this.nombreMoyenNbExemplaire=nombreMoyenNbExemplaire;
		this.deviationNombreMoyenNbExemplaire=deviationNombreMoyenNbExemplaire;
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
				sleep(Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement));
				MessageX messageProd = new MessageX(i, identification,nombreMoyenNbExemplaire,deviationNombreMoyenNbExemplaire);
				buffer.put(this, messageProd);
				// System.out.println("Le producteur " + this.identification + "
				// produit le message " + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}