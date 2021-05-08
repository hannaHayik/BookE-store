package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class RealeseVehicle implements Event<Boolean> {
	
	private DeliveryVehicle carToRealese;
	
	public RealeseVehicle(DeliveryVehicle car) {
		this.carToRealese=car;
	}
	
	public DeliveryVehicle getCarToRealese() {
		return this.carToRealese;
	}

}
