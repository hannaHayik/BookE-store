package bgu.spl.mics.application;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderSchedule;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.services.APIService;
import bgu.spl.mics.application.services.InventoryService;
import bgu.spl.mics.application.services.LogisticsService;
import bgu.spl.mics.application.services.ResourceService;
import bgu.spl.mics.application.services.SellingService;
import bgu.spl.mics.application.services.TimeService;
import jsonPojo.jsonHolder;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system. In the
 * end, you should output serialized objects.
 */
public class BookStoreRunner {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		HashMap<Integer, Customer> Toseralize = new HashMap<Integer, Customer>();
		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
			Gson gson = new Gson();
			jsonHolder holder = gson.fromJson(reader, jsonHolder.class);
			// creating new objects to fill the different parts of our store with.(cars and
			// books stock)
			BookInventoryInfo[] booksToLoad = new BookInventoryInfo[holder.getInitialInventory().length];
			for (int i = 0; i < holder.getInitialInventory().length; i++) {
				booksToLoad[i] = new BookInventoryInfo(holder.getInitialInventory()[i].getBookTitle(),
						Integer.parseInt(holder.getInitialInventory()[i].getAmount()),
						Integer.parseInt(holder.getInitialInventory()[i].getPrice()));
			}
			DeliveryVehicle[] carsToLoad = new DeliveryVehicle[holder.getInitialResources()[0].getVehicles().length];
			for (int i = 0; i < carsToLoad.length; i++) {
				carsToLoad[i] = new DeliveryVehicle(
						Integer.parseInt(holder.getInitialResources()[0].getVehicles()[i].getLicense()),
						Integer.parseInt(holder.getInitialResources()[0].getVehicles()[i].getSpeed()));
			}
			// loading the needed objects.
			ResourcesHolder resourcesInCharge = ResourcesHolder.getInstance();
			resourcesInCharge.load(carsToLoad);
			Inventory stock = Inventory.getInstance();
			stock.load(booksToLoad);
			int timeServiceSpeed = Integer.parseInt(holder.getServices().getTime().getSpeed());
			int timeServiceDuration = Integer.parseInt(holder.getServices().getTime().getDuration());
			int numOfSellingServices = Integer.parseInt(holder.getServices().getSelling());
			int numOfLogisticsServices = Integer.parseInt(holder.getServices().getLogistics());
			int numOfResourcesServices = Integer.parseInt(holder.getServices().getResourcesService());
			int numOfInventoryServices = Integer.parseInt(holder.getServices().getInventoryService());
			// creating customers and their OrderSchedules list to pass them to API
			// services.
			Customer[] customersArray = new Customer[holder.getServices().getCustomers().length];
			for (int i = 0; i < holder.getServices().getCustomers().length; i++) {
				customersArray[i] = new Customer(Integer.parseInt(holder.getServices().getCustomers()[i].getId()),
						holder.getServices().getCustomers()[i].getName(),
						holder.getServices().getCustomers()[i].getAddress(),
						Integer.parseInt(holder.getServices().getCustomers()[i].getDistance()),
						Integer.parseInt(holder.getServices().getCustomers()[i].getCreditCard().getNumber()),
						Integer.parseInt(holder.getServices().getCustomers()[i].getCreditCard().getAmount()));
				for (int j = 0; j < holder.getServices().getCustomers()[i].getOrderSchedule().length; j++) {
					customersArray[i].addToOrders(new OrderSchedule(customersArray[i].getId(),
							holder.getServices().getCustomers()[i].getOrderSchedule()[j].getBookTitle(),
							Integer.parseInt(holder.getServices().getCustomers()[i].getOrderSchedule()[j].getTick())));
				}

			}
			// although some instances won't be used here, we do this so the instances is
			// created before the MS uses them so they be ready, we don't just count on the
			// class loader.
			List<Thread> threadsList = new ArrayList<Thread>();
			TimeService onlyTimeService = TimeService.Getinstance(timeServiceSpeed, timeServiceDuration);
			MessageBusImpl theMessageBus = MessageBusImpl.getInstance();
			// Creating a thread for every MS, every kind in a FOR loop.
			List<SellingService> listOfSellingServices = new ArrayList<SellingService>();
			for (int i = 0; i < numOfSellingServices; i++) {
				listOfSellingServices.add(new SellingService("selling " + (i + 1)));
				threadsList.add(new Thread(listOfSellingServices.get(i)));
			}
			List<LogisticsService> listOfLogisticsServices = new ArrayList<LogisticsService>();
			for (int i = 0; i < numOfLogisticsServices; i++) {
				listOfLogisticsServices.add(new LogisticsService("logistics " + (i + 1)));
				threadsList.add(new Thread(listOfLogisticsServices.get(i)));
			}
			List<ResourceService> listOfResourcesService = new ArrayList<ResourceService>();
			for (int i = 0; i < numOfResourcesServices; i++) {
				listOfResourcesService.add(new ResourceService("resources " + (i + 1)));
				threadsList.add(new Thread(listOfResourcesService.get(i)));
			}
			List<APIService> listOfAPIServices = new ArrayList<APIService>();
			for (int i = 0; i < customersArray.length; i++) {
				listOfAPIServices.add(
						new APIService("API Service " + (i + 1), customersArray[i].getOrdersList(), customersArray[i]));
				threadsList.add(new Thread(listOfAPIServices.get(i)));
			}
			List<InventoryService> listOfInventoryServices = new ArrayList<InventoryService>();
			for (int i = 0; i < numOfInventoryServices; i++) {
				listOfInventoryServices.add(new InventoryService("inventory " + (i + 1)));
				threadsList.add(new Thread(listOfInventoryServices.get(i)));
			}
			// running all threads and doing a sleep so we can let them register all and
			// after that we let the timeService in so everybody
			// won't miss the first timeTick.
			for (int i = 0; i < threadsList.size(); i++)
				threadsList.get(i).start();
			Thread.sleep(500);
			threadsList.add(new Thread(onlyTimeService));
			threadsList.get(threadsList.size() - 1).start();
			// make the main wait for every thread we created before to finish.
			for (int i = 0; i < threadsList.size(); i++)
				threadsList.get(i).join();
			// serialize everything asked for in the assignment.
			FelltToSealize(customersArray, Toseralize);
			printCustomersToFile(args[1], Toseralize);
			Inventory.getInstance().printInventoryToFile(args[2]);
			MoneyRegister.getInstance().printOrderReceipts(args[3]);
			printMoneyRegister(args[4], MoneyRegister.getInstance());
		} catch (Exception e) {
		}
	}

	public static void FelltToSealize(Customer[] customersArray, HashMap<Integer, Customer> Toseralize) {
		for (int q = 0; q < customersArray.length; q++) {
			Toseralize.put(customersArray[q].getId(), customersArray[q]);
		}
	}

	public static void printCustomersToFile(String filename, HashMap<Integer, Customer> Toseralize) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Toseralize);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void printMoneyRegister(String filename, MoneyRegister kesef) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(kesef);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}