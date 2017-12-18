package jus.poc.prodcons.v5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {

	public static boolean FLAG_DEBUG = false; // Flag a utilise pour avoir une trace
	public static boolean FLAG_TIME = true; // Flag a utilise pour avoir le temps de chaque action
	public static Date START_TIME = new Date(); // heure de debut de l'execution

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

	public TestProdCons(Observateur observateur) {
		super(observateur);
		listProd = new ArrayList<Producteur>();
		listCons = new ArrayList<Consommateur>();
	}

	protected void run() throws Exception {
		String fileName = "options.xml";

		// Initialisation des variables
		init("jus/poc/prodcons/options/" + fileName);

		// Initialisation de l'observateur
		observateur.init(nbProd, nbCons, nbBuffer);

		// Creation du buffer
		ProdCons buffer = new ProdCons(nbBuffer, nbProd, observateur);

		// Creation des Producteurs
		for (int i = 0; i < nbProd; i++) {
			int nombreDeProduction = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
			Producteur currentProd = new Producteur(buffer, i, nombreDeProduction, observateur, tempsMoyenProduction,
					deviationTempsMoyenProduction);
			// Ajout le Producteur dans l'observateur
			observateur.newProducteur(currentProd);
			if (FLAG_DEBUG) {
				System.out.println("Prod " + i + " alive");
			}
			currentProd.start();
			listProd.add(i, currentProd);
		}

		// Creation des Consommateurs
		for (int i = 0; i < nbCons; i++) {
			Consommateur currentCons = new Consommateur(buffer, i, observateur, tempsMoyenConsommation,
					deviationTempsMoyenConsommation);
			// Ajout le Consommateur dans l'observateur
			observateur.newConsommateur(currentCons);
			currentCons.setDaemon(true);
			if (FLAG_DEBUG) {
				System.out.println("Cons " + i + " alive");
			}
			currentCons.start();
			listCons.add(i, currentCons);
		}

		// Terminaison des Producteur :
		// On les tue a la fin de leur execution.
		for (int i = 0; i < nbProd; i++) {
			listProd.get(i).join();
			if (FLAG_DEBUG) {
				System.out.println("Prod " + i + " dead");
			}
		}

		// Condition de Terminaison:
		// On laisse les Consommateurs s'executer tant que le buffer n'est pas
		// vide ou que le nombre de message consomme et produit ne sont pas egals
		do {
			Thread.yield();
		} while (buffer.enAttente() > 0 || buffer.getConsummed() != buffer.getProduced());

		if (FLAG_DEBUG) {
			System.out.println("Contenu du buffer: " + buffer.taille());
			System.out.println("consommé :" + buffer.getConsummed());
			System.out.println("produit : " + buffer.getProduced());
		}
		if (FLAG_TIME) {
			Date d = new Date();
			System.out.println("Fin du Programme: " + (d.getTime() - START_TIME.getTime()));
		}
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
