package bgu.spl.mics.application.passiveObjects;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Passive data-object representing the store inventory. It holds a collection
 * of {@link BookInventoryInfo} for all the books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton. You must
 * not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
@SuppressWarnings("serial")
public class Inventory implements Serializable {
	private ArrayList<BookInventoryInfo> stock;
	private static Inventory single_instance = null;
	private HashMap<String, Integer> ToSerializ;

	/**
	 * Retrieves the single instance of this class.
	 */
	public synchronized static Inventory getInstance() {
		if (single_instance == null)
			single_instance = new Inventory();
		return single_instance;

	}

	private Inventory() {
		stock = new ArrayList<BookInventoryInfo>();
		this.ToSerializ = new HashMap<String, Integer>();
	}

	/**
	 * Initializes the store inventory. This method adds all the items given to the
	 * store inventory.
	 * <p>
	 * 
	 * @param inventory Data structure containing all data necessary for
	 *                  initialization of the inventory.
	 */
	public void load(BookInventoryInfo[] inventory) {
		for (int i = 0; i < inventory.length; i++) {
			stock.add(inventory[i]);
		}
	}

	/**
	 * Attempts to take one book from the store.
	 * <p>
	 * 
	 * @param book Name of the book to take from the store
	 * @return an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN. The
	 *         first should not change the state of the inventory while the second
	 *         should reduce by one the number of books of the desired type.
	 */
	public OrderResult take(String book) {
			for (int i = 0; i < this.stock.size(); i++)
				if (this.stock.get(i).getBookTitle().compareTo(book) == 0) {
					synchronized (this.stock.get(i)) {
						boolean flag = this.isExsist(book);
						if(flag) {
						this.stock.get(i).SetAmontofInventory(this.stock.get(i).getAmountInInventory() - 1);
						return OrderResult.SUCCESSFULLY_TAKEN;
						}
					}
				}
		return OrderResult.NOT_IN_STOCK;

	}

	private Boolean isExsist(String book) {
		for (int i = 0; i < stock.size(); i++)
			if (stock.get(i).getBookTitle().compareTo(book) == 0) {
				synchronized(stock.get(i)) {
				if (stock.get(i).getAmountInInventory() > 0)
					return true;
				}
			}
		return false;
	}

	/**
	 * Checks if a certain book is available in the inventory.
	 * <p>
	 * 
	 * @param book Name of the book.
	 * @return the price of the book if it is available, -1 otherwise.
	 */
	public int checkAvailabiltyAndGetPrice(String book) {
		boolean flag = this.isExsist(book);
		int price = -1;
		if (flag) {
			for (int i = 0; i < this.stock.size(); i++) {
				if (this.stock.get(i).getBookTitle().compareTo(book) == 0)
					price = this.stock.get(i).getPrice();
			}
		}
		return price;

	}

	/**
	 * 
	 * <p>
	 * Prints to a file name @filename a serialized object HashMap<String,Integer>
	 * which is a Map of all the books in the inventory. The keys of the Map (type
	 * {@link String}) should be the titles of the books while the values (type
	 * {@link Integer}) should be their respective available amount in the
	 * inventory. This method is called by the main method in order to generate the
	 * output.
	 */
	public void printInventoryToFile(String filename) {
		try {
			this.FillToSerializ();
			FileOutputStream a = new FileOutputStream(filename);
			ObjectOutputStream b = new ObjectOutputStream(a);
			b.writeObject(this.ToSerializ);
			b.close();
			a.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

	private void FillToSerializ() {
		for (int i = 0; i < this.stock.size(); i++) {
			this.ToSerializ.put(this.stock.get(i).getBookTitle(), this.stock.get(i).getAmountInInventory());
		}
	}
}
