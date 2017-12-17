package jus.poc.prodcons.v6;

import java.util.Date;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int identifiantProducteur;
	private int identifiantMessage;
	public Date date;

	public MessageX(int identifiantMessage, int identifiantProducteur) {
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
	}

	public String toString() {
		if (TestProdCons.FLAG_TIME) {
			return "Message " + identifiantMessage + " of Producteur " + identifiantProducteur + " at "
					+ (date.getTime() - TestProdCons.START_TIME.getTime());
		} else {
			return "Message " + identifiantMessage + " of Producteur " + identifiantProducteur;
		}
	}

	public void setDate() {
		this.date = new Date();
	}

}
