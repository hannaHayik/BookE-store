package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicle;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.RealeseVehicle;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

/**
 * Logistic service in charge of delivering books that have been purchased to
 * customers. Handles {@link DeliveryEvent}. This class may not hold references
 * for objects which it is not responsible for: {@link ResourcesHolder},
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

	public LogisticsService(String name) {
		super(name);
	}

	@Override
	// We try to aquire vehicle through an event that is handled by someone with
	// access to ResourcesHolder, if succeeded it delivers and sends back a
	// RealeaseVehicle event to indicate that the vehicle has finished working.
	protected void initialize() {
		subscribeEvent(DeliveryEvent.class, ev -> {
			Future<DeliveryVehicle> futureObject = sendEvent(new AcquireVehicle());
			if (futureObject != null) {
				if (futureObject.get() != null) {
					futureObject.get().deliver(ev.getCustomer().getAddress(), ev.getCustomer().getDistance());
					sendEvent(new RealeseVehicle(futureObject.get()));
					complete(ev, true);
				}
			}
		});
		subscribeBroadcast(Terminate.class, br -> {
			this.terminate();
		});

	}

}
