package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicle;
import bgu.spl.mics.application.messages.RealeseVehicle;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store. This
 * class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService {
	private ResourcesHolder resources;
	private List<Event<DeliveryVehicle>> listOfUncompletedEvents;

	public ResourceService(String name) {
		super(name);
		this.resources = ResourcesHolder.getInstance();
		this.listOfUncompletedEvents = new ArrayList<Event<DeliveryVehicle>>();
	}

	@Override
	protected void initialize() {
		// Because ResourcesService can't find a vehicle sometimes it saves those events
		// in a DataStructure as a field that if the aquireVehicle method returns a null
		// meaning no vehicle is free in the current moment, and at every Tick we check
		// again if there is a free car to give it to someone who didn't succeed before,
		// if he gets a vehicle he is removed from the list, otherwise it gets a resolve
		// to null before termination because there could be a LogisticsService waiting
		// to get a vehicle and resolving to Null in our program means that no need to
		// wait nobody is handling this.

		subscribeEvent(AcquireVehicle.class, ev -> {
			Future<DeliveryVehicle> futureObject = this.resources.acquireVehicle();
			if (futureObject != null) {
				this.complete(ev, futureObject.get());
			} else
				this.listOfUncompletedEvents.add(ev);

		});

		subscribeBroadcast(TickBroadcast.class, br -> {
			List<Event<DeliveryVehicle>> tmpList = new ArrayList<Event<DeliveryVehicle>>();
			for (int i = 0; i < this.listOfUncompletedEvents.size(); i++) {
				Future<DeliveryVehicle> futureObject = this.resources.acquireVehicle();
				if (futureObject != null) {
					this.complete(this.listOfUncompletedEvents.get(i), futureObject.get());
				} else
					tmpList.add(this.listOfUncompletedEvents.get(i));
			}
			this.listOfUncompletedEvents = tmpList;
		});
		subscribeBroadcast(Terminate.class, br -> {
			for (int i = 0; i < this.listOfUncompletedEvents.size(); i++)
				this.complete(this.listOfUncompletedEvents.get(i), null);
			this.terminate();
		});
		subscribeEvent(RealeseVehicle.class, ev -> {
			this.resources.releaseVehicle(ev.getCarToRealese());
		});
	}

}
