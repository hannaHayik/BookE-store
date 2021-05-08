package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailability;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TakeBook;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderResult;

/**
 * Selling service in charge of taking orders from customers. Holds a reference
 * to the {@link MoneyRegister} singleton of the store. Handles
 * {@link BookOrderEvent}. This class may not hold references for objects which
 * it is not responsible for: {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService {
	private MoneyRegister cashBox;
	private int currentTimeTick;

	public SellingService(String name) {
		super(name);
		this.cashBox = MoneyRegister.getInstance();
		this.currentTimeTick = 1;
	}

	@Override
	// It's a beautiful looking method :D, ofcourse every fail in an IF statement
	// means we make a false orderReceipt, IF's statement checks eveything from
	// money and the amount of the book etc..., otherwise it gets all the needed
	// info and it charges the credit card and make a new OrderReceipt and put it in
	// the MoneyRegister and updates the ticks on the receipt. Any failure will
	// result in a "failed" receipt which indicates that the order didn't go as
	// planned and was dumped.
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class, ev -> {
			Future<Integer> futureObject = sendEvent(new CheckAvailability(ev.getBookName()));
			if (futureObject != null) {
				if (futureObject.get() != null) {
					if (futureObject.get() > -1) {
						if (ev.getCustomer().getAvailableCreditAmount() >= futureObject.get()) {
							Future<OrderResult> futureObject2 = sendEvent(new TakeBook(ev.getBookName()));
							if (futureObject2 != null) {
								if (futureObject2.get() != null) {
									if (futureObject2.get() == OrderResult.SUCCESSFULLY_TAKEN) {
										this.cashBox.chargeCreditCard(ev.getCustomer(), futureObject.get());
										sendEvent(new DeliveryEvent(ev.getBookName(), ev.getCustomer()));
										OrderReceipt kabala = new OrderReceipt(666, this.getName(),
												ev.getCustomer().getId(), ev.getBookName(), futureObject.get(),
												this.currentTimeTick);
										kabala.setProcessTick(currentTimeTick);
										kabala.setOrderTick(ev.getIssuedTick());
										this.cashBox.file(kabala);
										complete(ev, kabala);
									} else {
										complete(ev, new OrderReceipt(false));
									}
								} else {
									complete(ev, new OrderReceipt(false));
								}
							} else {
								complete(ev, new OrderReceipt(false));
							}
						} else {
							complete(ev, new OrderReceipt(false));
						}
					} else {
						complete(ev, new OrderReceipt(false));
					}
				} else {
					complete(ev, new OrderReceipt(false));
				}
			} else {
				complete(ev, new OrderReceipt(false));
			}
		});
		subscribeBroadcast(Terminate.class, br -> {
			this.terminate();
		});

		subscribeBroadcast(TickBroadcast.class, br -> {
			this.currentTimeTick = br.getTimeTick();
		});

	}

}
