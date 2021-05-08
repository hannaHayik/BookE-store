package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.OrderResult;

public class TakeBook implements Event<OrderResult> {
	private String bookName;
	
	public TakeBook(String name) {
		this.bookName=name;
	}
	
	public String getBookName() {
		return this.bookName;
	}

}
