package bgu.spl.mics;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus
 * interface. Write your implementation here! Only private fields and methods
 * can be added to this class.
 */
@SuppressWarnings("unused")
public class MessageBusImpl implements MessageBus {
	private static MessageBusImpl single_Message_Bus = null;

	private ConcurrentHashMap<Event<?>, Future<?>> future_of_events;
	private ConcurrentHashMap<MicroService, LinkedBlockingDeque<Message>> micro_service_queue;
	private ConcurrentHashMap<Class<? extends Event<?>>, ConcurrentLinkedDeque<MicroService>> micro_service_event;
	private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<MicroService>> micro_service_broadcast;

	synchronized public static MessageBusImpl getInstance() { // Said that the PS6 solution will still work so we
																// counted on that.
		if (single_Message_Bus == null)
			single_Message_Bus = new MessageBusImpl();
		return single_Message_Bus;
	}

	private MessageBusImpl() {
		micro_service_queue = new ConcurrentHashMap<>();
		micro_service_event = new ConcurrentHashMap<>();
		micro_service_broadcast = new ConcurrentHashMap<>();
		future_of_events = new ConcurrentHashMap<>();
	}

	@Override
	// the 2 subscribe methods need a lock because we create new objects here and we
	// want one object per key not multiple.
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (micro_service_event) {
			if (micro_service_event.containsKey(type)) {
				micro_service_event.get(type).addLast(m);

			} else {
				ConcurrentLinkedDeque<MicroService> tmp = new ConcurrentLinkedDeque<>();
				tmp.add(m);
				micro_service_event.put(type, tmp);

			}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (micro_service_broadcast) {
			if (micro_service_broadcast.containsKey(type)) {
				micro_service_broadcast.get(type).add(m);
			} else {
				ConcurrentLinkedQueue<MicroService> tmp = new ConcurrentLinkedQueue<>();
				tmp.add(m);
				micro_service_broadcast.put(type, tmp);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> void complete(Event<T> e, T result) {
		((Future<T>) future_of_events.get(e)).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (micro_service_broadcast) {
			if (micro_service_broadcast.get(b.getClass()) == null)
				return;
			for (int i = 0; i < micro_service_broadcast.get(b.getClass()).size(); i++) {
				MicroService x = micro_service_broadcast.get(b.getClass()).remove();
				micro_service_queue.get(x).add(b);
				micro_service_broadcast.get(b.getClass()).add(x);
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	// the method had to be synchronized because of the RoundRobin pattern and
	// because the MS we are giving instructions could be deleted concurrently.

	// Quick Note: I found out that my CPU was too fast that it added the event to
	// some MS's queue and that MicroService completed it even before
	// it reached the return statement down here, I think because the waiting MS in
	// awaitMessage just dont care about the synchronization here
	// so please TAKE IN CONSIDERATION that this code worked on our CPUs perfectly
	// and we had to
	// tune it down a bit.
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (micro_service_queue) {
			if (micro_service_event.get(e.getClass()) == null || micro_service_event.get(e.getClass()).size() == 0) {
				return null;
			} else {
				MicroService micro_service;
				synchronized (micro_service_event.get(e.getClass())) {
					micro_service = micro_service_event.get(e.getClass()).pop();
					if (micro_service != null) {
						Future<T> tmp = new Future<T>();
						micro_service_event.get(e.getClass()).addLast(micro_service);
						future_of_events.put(e, tmp);
						micro_service_queue.get(micro_service).add(e);
						return (Future<T>) future_of_events.get(e);
					} else
						return null;
				}
			}
		}
	}

	@Override
	public void register(MicroService m) {
		if (!micro_service_queue.containsKey(m)) {
			micro_service_queue.put(m, new LinkedBlockingDeque<Message>());
		}
	}

	@Override
	// Checks the messages queue of m, if it contains events then it resolves them
	// with null to indicate that m wont handle these events and there
	// would be no result, so it changes the boolean variable in Future to indicate
	// that it won't be handled, and the method continues to delete
	// everything related to m.
	public void unregister(MicroService m) {
		if (micro_service_queue.get(m) != null) {
			synchronized (micro_service_queue.get(m)) {
				if (micro_service_queue.get(m).size() > 0) {
					for (Message msgToTerminate : micro_service_queue.get(m)) {
						if (Event.class.isAssignableFrom(msgToTerminate.getClass())) {
							future_of_events.get(msgToTerminate).resolve(null);
						}
					}
				}
				if (micro_service_queue.containsKey(m))
					micro_service_queue.remove(m);
			}
			synchronized (micro_service_event) {
				for (ConcurrentLinkedDeque<MicroService> blox : micro_service_event.values())
					blox.remove(m);
			}
			synchronized (micro_service_broadcast) {
				for (ConcurrentLinkedQueue<MicroService> blox : micro_service_broadcast.values())
					blox.remove(m);
			}
		}
	}

	@Override
	// As the javadoc says to throw an exception if m isn't registered, else and
	// tries to grab a message from m queue and becuase it's a blocking queue it
	// will go into wait mode.
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (micro_service_queue.get(m) == null)
			throw new InterruptedException();
		return micro_service_queue.get(m).take();
	}
}
