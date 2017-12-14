package jus.poc.prodcons.v1;

import java.io.IOException;
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
	}

	protected void run() throws Exception {
		// le corps de votre programme principal
		String fileName = "options.xml";
		init("jus/poc/prodcons/options/" + fileName);
		// creer buffer
		ProdCons buffer = new ProdCons(nbBuffer);
		
		// creer prod et consomateurs et les demarrer
		for(int i = 0; i<nbProd; i++){
			Producteur currentProd = new Producteur(); 
			listProd.add(currentProd);
			currentProd.run();
		}
		for(int i = 0; i<nbCons; i++){
			Consommateur currentCons = new Consommateur(); 
			listCons.add(currentCons);
			currentCons.run();
		}
		// gerer la condition de terminaison
		while(true){}

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
