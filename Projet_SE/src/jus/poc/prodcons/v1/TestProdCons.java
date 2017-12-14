package jus.poc.prodcons.v1;

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

	public TestProdCons(Observateur observateur) {
		super(observateur);
		listProd = new ArrayList<Producteur>();
		listCons = new ArrayList<Consommateur>();
	}

	protected void run() throws Exception {
		System.out.println("Run");
		// le corps de votre programme principal
		String fileName = "options.xml";
		init("./jus/poc/prodcons/v1/" + fileName);
		// creer buffer
		System.out.println("Prod created");
		ProdCons buffer = new ProdCons(nbBuffer);

		// creer prod et consomateurs et les demarrer
		for (int i = 0; i < nbProd; i++) {
			int nombreDeProduction = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
			Producteur currentProd = new Producteur(buffer, i, nombreDeProduction, observateur, tempsMoyenProduction,
					deviationTempsMoyenProduction);
			listProd.add(i, currentProd);
			currentProd.start();

		}
		for (int i = 0; i < nbCons; i++) {
			Consommateur currentCons = new Consommateur(buffer, i, observateur, tempsMoyenConsommation,
					deviationTempsMoyenConsommation);
			listCons.add(currentCons);
			currentCons.start();

		}
		// gerer la condition de terminaison
		for (int i = 0; i < nbProd; i++) {
			listProd.get(i).join();
			System.out.println(i + " Producteur dead");
		}
		if(buffer.enAttente()==0){
			for (int i = 0; i < nbCons; i++) {
				//System.out.println("trying to die ");
				listCons.get(i).join();
				System.out.println(i + " Consommateur dead");
			}
		}
		
			System.out.println(buffer.getcons());
			System.out.println(buffer.getprod());
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
