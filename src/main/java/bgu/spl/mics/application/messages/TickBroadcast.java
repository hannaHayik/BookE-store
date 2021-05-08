package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
	
	private int timeTick;
	private String broadcastName;
	
	public TickBroadcast(String name,int timeTick) {
		this.timeTick=timeTick;
		this.broadcastName=name;
	}
	
	public int getTimeTick() {
		return this.timeTick;
	}
	
	public String getBroadcastName() {
		return this.broadcastName;
	}

}
