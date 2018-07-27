package nz.ac.auckland.concert.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import nz.ac.auckland.concert.common.Config;
import nz.ac.auckland.concert.common.ConcertDetails;
import nz.ac.auckland.concert.common.Concert;
import nz.ac.auckland.concert.common.ConcertService;

/**
 * JUnit test client for the RMI concert application.
 */
public class Client {

	// Proxy object to represent the remote ConcertFactory service.
	private static ConcertService _proxy;

	/**
	 * One-time setup method to retrieve the ConcertFactory proxy from the RMI
	 * Registry.
	 */
	@BeforeClass
	public static void getProxy() {
		try {
			// Instantiate a proxy object for the RMI Registry, expected to be
			// running on the local machine and on a specified port.
			Registry lookupService = LocateRegistry.getRegistry("localhost", Config.REGISTRY_PORT);

			// Retrieve a proxy object representing the ConcertFactory.
			_proxy = (ConcertService) lookupService.lookup(Config.SERVICE_NAME);
		} catch (RemoteException e) {
			System.out.println("Unable to connect to the RMI Registry");
		} catch (NotBoundException e) {
			System.out.println("Unable to acquire a proxy for the Concert service");
		}
	}

	/**
	 * Test that, using the ConcertFactory proxy, we can invoke methods on the
	 * remote concertFactory to create remotely accessible concerts. This test also
	 * then invokes methods on the remote concerts objects, via their acquired
	 * proxies.
	 */
	// ASSUMES GET ALL WORKS
	@Test
	public void testCreate() throws RemoteException {
		try {
			// Use the concertFactory proxy to create a couple of remote concert
			// instances. newconcert() returns proxies for the new remote concerts.
			Concert concertA = _proxy.createConcert(
					(Concert) new ConcertDetails(new Long(0), "Louis the Child", new DateTime(1, 1, 1, 1, 1)));
			Concert concertB = _proxy
					.createConcert((Concert) new ConcertDetails(new Long(1), "Odesza", new DateTime(1, 1, 1, 1, 1)));

			// Query the remote factory.
			List<Concert> remoteconcerts = _proxy.getAllConcerts();
			
			// Tests that both concerts are there
			assertTrue(remoteconcerts.contains(concertA));
			assertTrue(remoteconcerts.contains(concertB));
			assertEquals(2, remoteconcerts.size());
			
			// Check that id is assigned to concert during create
			assertEquals(0, concertA.getId().intValue());
		} catch (RemoteException e) {
			fail();
		}
	}

	/**
	 * Test that, using the ConcertFactory proxy, we can invoke methods on the
	 * remote concertFactory to get remotely accessible concerts.
	 */
	// ASSUMES CREATE AND CLEAR TEST WORKS/PASSES
	@Test
	public void testGet() throws RemoteException {
		try {
			_proxy.clear();
			Concert concertC = _proxy.createConcert(
					(Concert) new ConcertDetails(new Long(0), "Lil Yachty", new DateTime(1, 1, 1, 1, 1)));
			
			// ConcertC should be returned
			assertEquals(concertC, _proxy.getConcert(new Long(0)));
			_proxy.clear();
			
			// NULL should be returned if not found
			assertEquals(null, _proxy.getConcert(new Long(0)));
		} catch (RemoteException e) {
			fail();
		}
	}

	/**
	 * Test that, using the ConcertFactory proxy, we can invoke methods on the
	 * remote concertFactory to update remotely accessible concerts.
	 */
	// ASSUMES CREATE, CLEAR AND GET WORKS/PASSES
	@Test
	public void testUpdate() throws RemoteException {
		try {
			_proxy.clear();
			Concert concertE = new ConcertDetails(new Long(0), "Wave Racer", new DateTime(1, 1, 1, 1, 1));
			
			// Should return false because nothing to update
			assertFalse(_proxy.updateConcert(concertE));
			Concert concertD = _proxy
					.createConcert((Concert) new ConcertDetails(new Long(0), "Wave Racr", new DateTime(1, 1, 1, 1, 1)));
			
			// Should return true on update
			assertTrue(_proxy.updateConcert(concertE));
			
			// Concert should be the same one after being deleted
			assertEquals(concertE, _proxy.getConcert(new Long(0)));
		} catch (RemoteException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test that, using the ConcertFactory proxy, we can invoke methods on the
	 * remote concertFactory to delete remotely accessible concerts.
	 */
	// ASSUMES GETALL, CLEAR AND CATCH WORKS/PASSES
	@Test
	public void testDelete() throws RemoteException {
		try {
			_proxy.clear();
			Concert concertF = _proxy.createConcert(
					(Concert) new ConcertDetails(new Long(0), "$UCIDEBOY$", new DateTime(1, 1, 1, 1, 1)));
			
			// Should return true when deleting
			assertTrue(_proxy.deleteConcert(new Long(0)));
			List<Concert> remoteconcerts = _proxy.getAllConcerts();
			
			// Should not contain concert after deletion
			assertFalse(remoteconcerts.contains(concertF));
			
			// Should return false because nothing to delete
			assertFalse(_proxy.deleteConcert(new Long(0)));
		} catch (RemoteException e) {
			fail();
		}
	}

	/**
	 * Test that, using the ConcertFactory proxy, we can invoke methods on the
	 * remote concertFactory to get all remotely accessible concerts.
	 */
	// ASSUMES CLEAR WORKS/PASSES
	@Test
	public void testGetAll() throws RemoteException {
		try {
			_proxy.clear();
			List<Concert> remoteconcerts = _proxy.getAllConcerts();
			
			// Should return nothing after being cleared
			assertEquals(0, remoteconcerts.size());
			
			// Same test as create because it relies on getAll
			testCreate();
		} catch (RemoteException e) {
			fail();
		}
	}

	/**
	 * Test that, using the ConcertFactory proxy, we can invoke methods on the
	 * remote concertFactory to clear all remotely accessible concerts.
	 */
	// ASSUMES CREATE AND GET ALL WORKS/PASSES
	@Test
	public void testClear() throws RemoteException {
		try {
			_proxy.clear();
			List<Concert> remoteconcerts = _proxy.getAllConcerts();

			// Should clear whatever was in it
			assertEquals(0, remoteconcerts.size());
			Concert concertG = _proxy
					.createConcert((Concert) new ConcertDetails(new Long(0), "Deaf Kev", new DateTime(1, 1, 1, 1, 1)));
			remoteconcerts = _proxy.getAllConcerts();
			
			// Check create/getall works
			assertEquals(1, remoteconcerts.size());
			_proxy.clear();
			remoteconcerts = _proxy.getAllConcerts();
			
			// Should clear with items in it
			assertEquals(0, remoteconcerts.size());
			_proxy.clear();
			
			// Should not have anything in it after already clearing and clearing again
			assertEquals(0, remoteconcerts.size());
		} catch (RemoteException e) {
			fail();
		}
	}
}
