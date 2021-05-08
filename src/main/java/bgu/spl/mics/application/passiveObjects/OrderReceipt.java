package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should be sent to a customer
 * after the completion of a BookOrderEvent. You must not alter any of the given
 * public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public
 * methods).
 */
@SuppressWarnings("serial")
public class OrderReceipt implements Serializable{
	private int orderId;
	private String seller;
	private int idOfCustomer;
	private String bookTitle;
	private int price;
	private int issuedTick;
	private int orderTick;
	private int proccessTick;
	
	public OrderReceipt(int orderId, String seller, int idOfCustomer, String bookTitle, int price, int issuedTick) {
		this.orderId=orderId;
		this.seller=seller;
		this.idOfCustomer=idOfCustomer;
		this.bookTitle=bookTitle;
		this.price=price;
		this.issuedTick=issuedTick;
	}
	
	public OrderReceipt(Boolean failed) {
		if(!failed)
			this.orderId=-11;
	}

	/**
	 * Retrieves the orderId of this receipt.
	 */
	public int getOrderId() {
		return this.orderId;
	}

	/**
	 * Retrieves the name of the selling service which handled the order.
	 */
	public String getSeller() {
		return this.seller;
	}

	/**
	 * Retrieves the ID of the customer to which this receipt is issued to.
	 * <p>
	 * 
	 * @return the ID of the customer
	 */
	public int getCustomerId() {
		return this.idOfCustomer;
	}

	/**
	 * Retrieves the name of the book which was bought.
	 */
	public String getBookTitle() {
		return this.bookTitle;
	}

	/**
	 * Retrieves the price the customer paid for the book.
	 */
	public int getPrice() {
		return this.price;
	}

	/**
	 * Retrieves the tick in which this receipt was issued.
	 */
	public int getIssuedTick() {
		return this.issuedTick;
	}

	/**
	 * Retrieves the tick in which the customer sent the purchase request.
	 */
	public int getOrderTick() {
		return this.orderTick;
	}

	/**
	 * Retrieves the tick in which the treating selling service started processing
	 * the order.
	 */
	public int getProcessTick() {
		return this.proccessTick;
	}
	
	public void setProcessTick(int tick) {
		this.proccessTick=tick;
	}
	
	public void setOrderTick(int tick) {
		this.orderTick=tick;
	}
}
