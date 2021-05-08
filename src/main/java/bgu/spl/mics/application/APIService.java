package bgu.spl.mics.application.services;

import java.util.List;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderSchedule;

/**
 * APIService is in charge of the connection between a client and the store. It
 * informs the store about desired purchases using {@link BookOrderEvent}. This
 * class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService {
	private List<OrderSchedule> listOfOrders;
	private Customer customer;

	public APIService(String name, List<OrderSchedule> listOfOrders, Customer customer) {
		super(name);
		this.listOfOrders = listOfOrders;
		this.customer = customer;
	}

	@Override
	// Assignment said to send every order and wait for the answer, so we checked
	// the list and every order on currentTick we send an event for it, and we check
	// if we got the appropriate results to file the OrderReceipt.
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
			for (int i = 0; i < this.listOfOrders.size(); i++) {
				if (this.listOfOrders.get(i).getTimeTick() == br.getTimeTick()) {
					Future<OrderReceipt> futureObject = ((Future<OrderReceipt>) sendEvent(new BookOrderEvent(
							listOfOrders.get(i).getBookTitle(), this.customer, listOfOrders.get(i).getTimeTick())));
					if (futureObject != null) {
						if (futureObject.get() != null)
							if (futureObject.get().getOrderId() != -11) { 
								// If you look in the OrderReceipt there is a failure constructor which indicates the OrderId with -11
								OrderReceipt toAdd = futureObject.get();
								this.customer.addToReceiptList(toAdd);
							}
					}
				}
			}
		});
		subscribeBroadcast(Terminate.class, br -> {
			this.terminate();
		});
	}

}
