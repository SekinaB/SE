package jus.poc.prodcons.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {
	private int nbProd;
	private int nbCons;
	private int nbBuffer;
	private int tempsMoyenProduction;
	private int nombreMoyenDeProduction;
	private int deviationTempsMoyenProduction;
	private int deviationNombreMoyenDeProduction;
	private int tempsMoyenConsommation;
	private int deviationTempsMoyenConsommation;

	private int nombreMoyenNbExemplaire;
	private int deviationNombreMoyenNbExemplaire;

	private List<Producteur> listProd;
	private List<Consommateur> listCons;
	private int nbProd_alive;

	public TestProdCons(Observateur observateur) {
		super(observateur);
		listProd = new ArrayList<Producteur>();
		listCons = new ArrayList<Consommateur>();
	}

	protected void run() throws Exception {

		// Chargement des paramètres à partir du XML
		String fileName = "options.xml";
		init("jus/poc/prodcons/options/" + fileName);

		// Indication de la configuration
		this.observateur.init(nbProd, nbCons, nbBuffer);

		// Création du buffer
		ProdCons buffer = new ProdCons(nbBuffer,this.observateur);
		this.nbProd_alive = nbProd;

		// Création des producteurs + démarrage
		for (int i = 0; i < nbProd; i++) {
			int nombreDeProduction = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
			Producteur currentProd = new Producteur(buffer, i, nombreDeProduction, observateur, tempsMoyenProduction,
					deviationTempsMoyenProduction);

			// Indique à l'observateur la création d'un producteur
			this.observateur.newProducteur(currentProd);

			// On lance le producteur + ajout dans la liste des producteurs
			currentProd.start();
			listProd.add(i, currentProd);

		}

		// Création des consommateurs + démarrage
		for (int i = 0; i < nbCons; i++) {
			Consommateur currentCons = new Consommateur(buffer, i, observateur, tempsMoyenConsommation,
					deviationTempsMoyenConsommation);

			// Indique à l'observateur la création d'un consommateur
			this.observateur.newConsommateur(currentCons);

			// Les consommateurs sont des démons.
			currentCons.setDaemon(true);
			currentCons.start();
			listCons.add(currentCons);

		}

		// GESTION DE LA TERMINAISON
		
		//Attente de la fin des producteurs
		for (int i = 0; i < nbProd; i++) {
			listProd.get(i).join();
			this.nbProd_alive--;
			System.out.println(i + " Producteur dead");
		}
		
		// Attente de consommation des messages
		while (buffer.enAttente() != 0) {
		}
		
		System.out.println("Contenu du buffer: " + buffer.taille());
		System.out.println("consommé :" + buffer.getcons());
		System.out.println("produit : " + buffer.getprod());
	}

	/**
	 * Retreave the parameters of the application.
	 * 
	 * @param file
	 *            the final name of the file containing the options.
	 * @throws IOException
	 * @throws InvalidPropertiesFormatException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	protected void init(String file) throws InvalidPropertiesFormatException, IOException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		Properties properties = new Properties();
		properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		String key;
		int value;
		Class<?> thisOne = getClass();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			key = (String) entry.getKey();
			value = Integer.parseInt((String) entry.getValue());
			thisOne.getDeclaredField(key).set(this, value);
		}
	}

	public static void main(String[] args) {
		new TestProdCons(new Observateur()).start();

	}

}