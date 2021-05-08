package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.passiveObjects.OrderResult;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InventoryTest {

	private Inventory stock;
	private BookInventoryInfo[] inventory1;

	@Before
	public void setUp() {
		stock = Inventory.getInstance();
		inventory1 = new BookInventoryInfo[3];
		BookInventoryInfo book1 = new BookInventoryInfo("Me Talk Pretty One Day", 1, 100);
		BookInventoryInfo book2 = new BookInventoryInfo("The Silence of the Lambs", 2, 50);
		BookInventoryInfo book3 = new BookInventoryInfo("Extremely Loud and Incredibly Close", 3, 120);
		inventory1[0] = book1;
		inventory1[1] = book2;
		inventory1[2] = book3;

	}

	@Test
	public void testGetInstance() {
		Inventory s1 = Inventory.getInstance();
		assertTrue(s1 == stock);

	}
	@Test
	public void testload() {
	BookInventoryInfo book4=new BookInventoryInfo("Road To Glory",1,120);
	BookInventoryInfo book5=new BookInventoryInfo("How To Get Away With Murder",2,70);
	BookInventoryInfo[] toAdd2=new BookInventoryInfo[2];
	toAdd2[0]=book4;
	toAdd2[1]=book5;
	stock.load(toAdd2);
	assertTrue(stock.take("Road To Glory")==OrderResult.SUCCESSFULLY_TAKEN);
	assertFalse(stock.take("Road To Glory")==OrderResult.SUCCESSFULLY_TAKEN);
	assertTrue(stock.take("Road To Glory")==OrderResult.NOT_IN_STOCK);
	
	}

	@Test
	public void testTake() {
		this.stock.load(inventory1);
		OrderResult result1 = stock.take("Extremely Loud and Incredibly Close");
		assertEquals(result1, OrderResult.SUCCESSFULLY_TAKEN);
		OrderResult result2 = stock.take("The Giver");
		assertEquals(result2, OrderResult.NOT_IN_STOCK);
		result1 = stock.take("Extremely Loud and Incredibly Close");
		result1 = stock.take("Extremely Loud and Incredibly Close");
		result1 = stock.take("Extremely Loud and Incredibly Close");
		assertEquals(result1, OrderResult.NOT_IN_STOCK);
		System.out.println(result2);

	}

	@Test
	public void testCheckAvailabiltyAndGetPrice() {

		int price1 = this.stock.checkAvailabiltyAndGetPrice("The Silence of the Lambs");
		assertTrue(price1 == 50);
		assertFalse(price1 == 40);
		int price2 = this.stock.checkAvailabiltyAndGetPrice("The Giver");
		assertTrue(price2 == -1);
	}

}
