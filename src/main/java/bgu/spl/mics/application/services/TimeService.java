package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.Terminate;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this
 * micro-service. It keeps track of the amount of ticks passed since
 * initialization and notifies all other micro-services about the current time
 * tick using {@link Tick Broadcast}. This class may not hold references for
 * objects which it is not responsible for: {@link ResourcesHolder},
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
	private int speed;
	private int duration;
	private static TimeService instance = null;
	private int currentTime;

	private TimeService(int speed, int duration) {
		super("time");
		this.speed = speed;
		this.duration = duration;
	}

	public synchronized static TimeService Getinstance(int speed, int duration) {
		if (instance == null)
			instance = new TimeService(speed, duration);
		return instance;
	}

	@Override
	protected void initialize() {
		this.currentTime = 1;
		this.startWorking();
	}

	// Considering TimeService doesn't receive any messages so the Run Loop won't
	// help her, and we were told in the forums that it's ok to let the TimeService
	// do all it's work thorugh initialize() and at the last tick it sends a
	// Terminate broadcast to every MS in the system before self termination.
	public void startWorking() {
		while (currentTime < duration) {
			sendBroadcast(new TickBroadcast("TimeTick " + currentTime, currentTime));
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentTime++;
		}
		if (currentTime == duration) {
			sendBroadcast(new TickBroadcast("TimeTick " + currentTime, currentTime));
			sendBroadcast(new Terminate());
			this.terminate();
		}
	}

}