package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

@SuppressWarnings("serial")
//As said in the assignment docs, we created an OrderSchedule so the APIService constructor can have a list of OrderSchedule objects.
public class OrderSchedule implements Serializable{
	
	private int customerId;
	private String bookTitle;
	private int timeTick;
	
	public OrderSchedule(int customerId, String bookTitle, int timeTick) {
		this.customerId=customerId;
		this.bookTitle=bookTitle;
		this.timeTick=timeTick;
	}
	
	public int getCustomerId() {
		return this.customerId;
	}
	
	public String getBookTitle() {
		return this.bookTitle;
	}
	
	public int getTimeTick() {
		return this.timeTick;
	}
}
