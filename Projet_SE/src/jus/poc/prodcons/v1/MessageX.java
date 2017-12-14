package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	boolean isConsumed;
	Producteur creator;
	Consommateur consumer;
	
	public MessageX(Producteur p){
		this.isConsumed = false;
		this.creator = p;
	}
}
