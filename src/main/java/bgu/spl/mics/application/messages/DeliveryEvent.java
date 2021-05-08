package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class DeliveryEvent implements Event<Boolean> {

	private Customer customer;
	private String bookName;
	
	public DeliveryEvent(String bookName, Customer customer) {
		this.customer=customer;
		this.bookName=bookName;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	
	public String getBookName() {
		return this.bookName;
	}
}
