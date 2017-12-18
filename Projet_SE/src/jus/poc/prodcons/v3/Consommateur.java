package jus.poc.prodcons.v3;

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
	/**
	 * Getter of deviationTempsDeTraitement
	 * 
	 * @return deviationTempsDeTraitement of the Consommateur
	 */
	public int deviationTempsDeTraitement() {
		return deviationTempsDeTraitement;
	}

	@Override
	/**
	 * Getter of identification
	 * 
	 * @return identification of the Consommateur
	 */
	public int identification() {
		return identification;
	}

	@Override
	/**
	 * Getter of moyenneTempsDeTraitement
	 * 
	 * @return moyenneTempsDeTraitement of the Consommateur
	 */
	public int moyenneTempsDeTraitement() {
		return moyenneTempsDeTraitement;
	}

	@Override
	/**
	 * Getter of nombreDeMessages
	 * 
	 * @return nombreDeMessages of the Consommateur
	 */
	public int nombreDeMessages() {
		return nombreDeMessages;
	}

	@Override
	public void run() {
		MessageX messageCons;
		while (true) {
			try {
				// Simulation du temps de traitement avec un sleep
				int tempsDeTraitement = Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement);
				sleep(tempsDeTraitement);

				// Recuperation du message
				messageCons = (MessageX) buffer.get(this);
				
				// Control avec l'observateur
				observateur.consommationMessage(this, messageCons, tempsDeTraitement);

				// Traitement du message
				System.out.println("RETRAIT : " + messageCons.toString() + " by Consommateur " + this.identification);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
