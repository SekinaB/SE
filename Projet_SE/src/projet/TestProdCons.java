package projet;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {
	protected int nbProd, nbCons, nbBuffer, tempsMoyenProduction;
	protected int deviationTempsMoyenProduction, tempsMoyenConsommation;
	protected int deviationTempsMoyenConsommation, nombreMoyenDeProduction;
	protected int deviationNombreMoyenDeProduction, nombreMoyenNbExemplaire;
	protected int deviationNombreMoyenNbExemplaire;

	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	protected void run() throws Exception{
	// le corps de votre programme principal
		String fileName = "options.xml";
		init("jus/poc/prodcons/options/" + fileName);
		// creer buffer 
		ProdCons buffer = new ProdCons();
		// creer prod et consomateurs et les demarrer
		
		
		// gerer la condition de terminaison
		
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
	protected void init(String file) throws InvalidPropertiesFormatException, IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
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
