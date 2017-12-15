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
		// TODO Auto-generated constructor stub
		this.buffer = buffer;
		this.identification = identification;
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
		MessageX val;
		while(true){
			try {
				sleep(Aleatoire.valeur(moyenneTempsDeTraitement, deviationTempsDeTraitement));
				val = (MessageX) buffer.get(this);
				System.out.println("Le consommateur " + this.identification + " consomme " + val.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
