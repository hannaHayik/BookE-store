package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.TakeBook;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;

/**
 * InventoryService is in charge of the book inventory and stock. Holds a
 * reference to the {@link Inventory} singleton of the store. This class may not
 * hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService {
	private Inventory wareHouse;

	public InventoryService(String name) {
		super(name);
		this.wareHouse = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(Terminate.class, br -> {
			this.terminate();
		});

		subscribeEvent(CheckAvailability.class, ev -> {
			int bookPrice = wareHouse.checkAvailabiltyAndGetPrice(ev.getBookName());
			complete(ev, bookPrice);
		});

		subscribeEvent(TakeBook.class, ev -> {
			OrderResult takingResult = null;
			takingResult = wareHouse.take(ev.getBookName());
			complete(ev,takingResult);
		});

	}

}
