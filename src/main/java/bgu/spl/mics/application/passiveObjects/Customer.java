package bgu.spl.mics.application.passiveObjects;

import java.util.List;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Passive data-object representing a customer of the store. You must not alter
 * any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public
 * methods).
 */
@SuppressWarnings("serial")
public class Customer implements Serializable {

	private int id;
	private String name;
	private String address;
	private int distance;
	private List<OrderReceipt> receiptsissued;
	private int creditCard;
	private int availableAmountInCreditCard;
	private List<OrderSchedule> ordersSchedule;

	public Customer() {
	}

	public Customer(int id, String name, String address, int distance, int creditCard, int left) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.distance = distance;
		this.creditCard = creditCard;
		this.availableAmountInCreditCard = left;
		this.ordersSchedule = new LinkedList<OrderSchedule>();
		this.receiptsissued = new LinkedList<OrderReceipt>();
	}

	public void addToOrders(OrderSchedule e) {
		this.ordersSchedule.add(e);
	}

	public List<OrderSchedule> getOrdersList() {
		return this.ordersSchedule;
	}

	/**
	 * Retrieves the name of the customer.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves the ID of the customer .
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Retrieves the address of the customer.
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Retrieves the distance of the customer from the store.
	 */
	public int getDistance() {
		return this.distance;
	}

	/**
	 * Retrieves a list of receipts for the purchases this customer has made.
	 * <p>
	 * 
	 * @return A list of receipts.
	 */
	public List<OrderReceipt> getCustomerReceiptList() {
		return this.receiptsissued;
	}

	public void addToReceiptList(OrderReceipt or) {
		this.receiptsissued.add(or);
	}

	/**
	 * Retrieves the amount of money left on this customers credit card.
	 * <p>
	 * 
	 * @return Amount of money left.
	 */
	public int getAvailableCreditAmount() {
		synchronized (this) {
			return this.availableAmountInCreditCard;
		}
	}

	/**
	 * Retrieves this customers credit card serial number.
	 */
	public int getCreditNumber() {
		return this.creditCard;
	}

	public void setavailableAmountInCreditCard(int amount) {
		synchronized (this) {
			this.availableAmountInCreditCard = amount;
		}
	}

}
