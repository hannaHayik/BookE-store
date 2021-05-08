package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class BookOrderEvent implements Event<OrderReceipt> {
	private int issuedTick;
	private String bookName;
	private Customer customer;

	public BookOrderEvent(String bookName, Customer customer, int issuedTick) {
		this.bookName = bookName;
		this.customer=customer;
		this.issuedTick=issuedTick;
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
	
	public String getBookName() {
		return this.bookName;
	}
	
	public int getIssuedTick() {
		return this.issuedTick;
	}
	

}
