package bgu.spl.mics.application.passiveObjects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the store finance management. It should hold a
 * list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton. You must
 * not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
@SuppressWarnings("serial")
public class MoneyRegister implements Serializable {
	private List<OrderReceipt> receiptissued;
	private static MoneyRegister instance = null;

	private MoneyRegister() {
		receiptissued = new LinkedList<OrderReceipt>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public synchronized static MoneyRegister getInstance() {
		if (instance == null)
			instance = new MoneyRegister();
		return instance;
	}

	/**
	 * Saves an order receipt in the money register.
	 * <p>
	 * 
	 * @param r The receipt to save in the money register.
	 */
	public void file(OrderReceipt r) {
		this.receiptissued.add(r);
	}

	/**
	 * Retrieves the current total earnings of the store.
	 */
	public int getTotalEarnings() {
		int total = 0;
		for (int i = 0; i < this.receiptissued.size(); i++) {
			total = total + this.receiptissued.get(i).getPrice();
		}
		return total;

	}

	/**
	 * Charges the credit card of the customer a certain amount of money.
	 * <p>
	 * 
	 * @param amount amount to charge
	 */
	public void chargeCreditCard(Customer c, int amount) {
		c.setavailableAmountInCreditCard(c.getAvailableCreditAmount() - amount);
	}

	/**
	 * Prints to a file named @filename a serialized object List<OrderReceipt> which
	 * holds all the order receipts currently in the MoneyRegister This method is
	 * called by the main method in order to generate the output..
	 */
	public void printOrderReceipts(String filename) {
		try {
			FileOutputStream a = new FileOutputStream(filename);
			ObjectOutputStream b = new ObjectOutputStream(a);
			b.writeObject(this.receiptissued);
			b.close();
			a.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
}