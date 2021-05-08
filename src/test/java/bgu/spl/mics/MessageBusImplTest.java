package bgu.spl.mics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import bgu.spl.mics.application.services.SellingService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

public class MessageBusImplTest {
	
	private MessageBusImpl bus1;
	
	@Before
	public void setUp() {
		bus1=MessageBusImpl.getInstance();
	}
	
	@Test
	public void testGetInstance() {
		MessageBusImpl bus2=MessageBusImpl.getInstance();
		assertTrue(bus2==bus1);
	}

	@Test
	public void testSubscribeEvent(){
		this.testAwaitMessage();
	}

	@Test
	public void testSubscribeBroadcast() { //same as the rest but we changed Event functions to Broadcast functions.
		MicroService s1=new SellingService("Selling");
		bus1.register(s1);
		ExampleBroadcast examplBroadcast=new ExampleBroadcast("Jason");
		bus1.subscribeBroadcast(examplBroadcast.getClass(), s1);
		bus1.sendBroadcast(examplBroadcast);
		Message msg1=null;
		try {
			msg1 = bus1.awaitMessage(s1);
		} catch (InterruptedException e) {}
		assertTrue(msg1==examplBroadcast);
	}

	@Test
	public void testComplete() { //register a MS and an event to it, complete method will resolve resultToCompare object
		                         //to the string we sent and we compare them to see if it worked as expected.
		MicroService s1=new SellingService("Selling1");
		ExampleEvent examplEvent=new ExampleEvent("Vector");
		bus1.register(s1);
		Future<String> resultToCompare=bus1.sendEvent(examplEvent);
		bus1.complete(examplEvent,"Vector Status:Done");
		assertTrue(resultToCompare.get().compareTo("Vector Status:Done")==0);
	}

	@Test
	public void testRegister() {
		MicroService s1=new SellingService("Selling2");
		bus1.register(s1);
		boolean thrown=false;
		try {
			bus1.awaitMessage(s1);
		}
		catch(InterruptedException e) {}
		assertTrue(!thrown);
	}

	@Test
	public void testUnregister() {
		MicroService s1=new SellingService("Selling3");
		bus1.register(s1);
		bus1.unregister(s1);
		boolean thrown=false;
		try {
			bus1.awaitMessage(s1);
		}
		catch(InterruptedException e){
			thrown=true;
		}
		assertTrue(thrown); //should throw exception if the micro-service isn't registered
		
	}

	@Test
	public void testAwaitMessage(){
		//register the MS and give an event and then when it returns check the type of the event to compare to the one we sent.
		MicroService s1=new SellingService("Selling4");
		bus1.register(s1);
		ExampleEvent examplEvent=new ExampleEvent("Jason");
		bus1.subscribeEvent(examplEvent.getClass(), s1);
		bus1.sendEvent(examplEvent);
		Message msg1=null;
		try {
			msg1 = bus1.awaitMessage(s1);
		} catch (InterruptedException e) {}
		assertTrue(msg1==examplEvent);
	}
	

}
