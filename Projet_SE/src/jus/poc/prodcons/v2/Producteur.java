package jus.poc.prodcons.v2;

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

	protected Producteur(ProdCons buffer, int identification, int nombreDeMessages, Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.deviationTempsDeTraitement = deviationTempsDeTraitement;
		this.moyenneTempsDeTraitement = moyenneTempsDeTraitement;
		this.identification = identification;
		this.nombreDeMessages = nombreDeMessages;
		this.buffer = buffer;
	}

	@Override
	/**
	 * Getter of deviationTempsDeTraitement
	 * 
	 * @return deviationTempsDeTraitement of the Producteur
	 */
	public int deviationTempsDeTraitement() {
		return deviationTempsDeTraitement;
	}

	@Override
	/**
	 * Getter of identification
	 * 
	 * @return identification of the Producteur
	 */
	public int identification() {
		return identification;
	}

	@Override
	/**
	 * Getter of moyenneTempsDeTraitement
	 * 
	 * @return moyenneTempsDeTraitement of the Producteur
	 */
	public int moyenneTempsDeTraitement() {
		return moyenneTempsDeTraitement;
	}

	@Override
	/**
	 * Getter of nombreDeMessages
	 * 
	 * @return nombreDeMessages of the Producteur
	 */
	public int nombreDeMessages() {
		return nombreDeMessages;
	}

	@Override
	public void run() {
		for (int i = 0; i < nombreDeMessages; i++) {
			try {
				// Creation du message a deposer
				MessageX messageProd = new MessageX(i, identification);

				// Simulation du temps de traitement avec un sleep avant le
				// depot
				sleep(Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement));

				// Depot du message
				buffer.put(this, messageProd);

				if (TestProdCons.FLAG_TIME) {
					Date d = new Date();
					System.out.println("DEPOT : Message " + i + " by Producteur " + this.identification + " at "
							+ (d.getTime() - TestProdCons.START_TIME.getTime()));
				} else {
					System.out.println("DEPOT : Message " + i + " by Producteur " + this.identification);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Le producteur a fini son execution : on met a jour la variable du
		// buffer
		buffer.finProducteur();
		if (TestProdCons.FLAG_DEBUG) {
			System.out.println("Producteur " + this.identification + " finished");

		}
	}

}
