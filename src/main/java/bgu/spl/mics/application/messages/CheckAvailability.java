package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class CheckAvailability implements Event<Integer> {
	
	private String bookName;

	public CheckAvailability(String name) {
		this.bookName=name;
	}
	
	public String getBookName() {
		return this.bookName;
	}

}
