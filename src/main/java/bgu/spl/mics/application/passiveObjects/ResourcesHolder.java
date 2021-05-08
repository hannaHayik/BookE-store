package bgu.spl.mics.application.passiveObjects;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.mics.Future;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private List<DeliveryVehicle> carList;
	private static ResourcesHolder instance=null;
	private List<Indicator> carIndicatorList;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ResourcesHolder() {
		carList=new LinkedList();
		this.carIndicatorList=new LinkedList();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public synchronized static ResourcesHolder getInstance() {
		if(instance==null)
			instance=new ResourcesHolder();
		return instance;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() { 
		synchronized(this) {
		boolean found=false;
		Future<DeliveryVehicle> carToReturn=new Future<DeliveryVehicle>();	
		while(!found) {
		int indexOfCar = 0;
		boolean flag = false;
		for (int i = 0; i < this.carIndicatorList.size()&&!flag; i++) {
			if (this.carIndicatorList.get(i).isActive == false) {
				this.carIndicatorList.get(i).isActive = true;
				flag = true;
				indexOfCar=i;
			}
		}
		if(flag==false) {
			return null;
		}
		else {
			carToReturn.resolve(this.carIndicatorList.get(indexOfCar).car);
			found=true;
		}
		}
		return carToReturn;
		}
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		synchronized(this) {
		for(int i=0; i<this.carIndicatorList.size(); i++ ) {
			if(vehicle==this.carIndicatorList.get(i).car) {
				this.carIndicatorList.get(i).isActive=false;
			}
		}
	}
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		for(int i=0; i<vehicles.length; i++) {
			this.carList.add(vehicles[i]);
			this.carIndicatorList.add(new Indicator(vehicles[i]));
		}
	}
	
	//A Do It Yourself Pair class, we were comfortable with making our own classes because it easier to understand.
	private class Indicator {
		public boolean isActive;
		public DeliveryVehicle car;

		public Indicator(DeliveryVehicle car) {
			this.isActive = false;
			this.car=car;
		}

	}

}