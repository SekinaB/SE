package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private int identifiantProducteur;
	private int identifiantMessage;
	
	public MessageX(int identifiantMessage, int identifiantProducteur) {
		this.identifiantProducteur = identifiantProducteur;
		this.identifiantMessage = identifiantMessage;
	}
	
	public String toString(){
		return "le message " + identifiantMessage + " du Producteur " + identifiantProducteur; 
	}
	
	
}
