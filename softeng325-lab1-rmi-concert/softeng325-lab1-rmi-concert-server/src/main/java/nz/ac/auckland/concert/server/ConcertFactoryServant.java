package nz.ac.auckland.concert.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nz.ac.auckland.concert.common.Concert;
import nz.ac.auckland.concert.common.ConcertService;

/**
 * An implementation of the ConcertFactory interface. A ConcertFactoryServant
 * instance is a remotely accessible object that creates and stores references
 * to remotely accessible Concert objects. Within a client/server application, a
 * single ConcertFactoryServant object runs on the server; the Concert objects
 * created by the factory also reside on the server. Clients acquire remote
 * references to the Concert objects from the factory.
 * 
 */
public class ConcertFactoryServant extends UnicastRemoteObject implements ConcertService {

	private static final long serialVersionUID = 1L;

	private HashMap<Integer, Concert> _concerts; // List of Concerts created a ConcertFactoryServant.
	private final int _maxConcerts; // Capacity of a ConcertFactoryServant.

	/**
	 * Creates a ConcertFactoryServant object.
	 * 
	 * @param maxConcerts
	 *            the factory's capacity in terms of the maximum number of concert
	 *            objects that can be created.
	 * @throws RemoteException
	 *             if the server-side RMI run-time cannot create the
	 *             ConcertFactoryServant instance. Construction can fail if the RMI
	 *             runtime has insufficient resources to host the new object.
	 */
	public ConcertFactoryServant(int maxConcerts) throws RemoteException {
		super();
		_concerts = new HashMap<Integer, Concert>();
		_maxConcerts = maxConcerts;
	}

	/**
	 * @see Concert.ConcertService#createConcert()
	 */
	@Override
	public synchronized Concert createConcert(Concert concert) throws RemoteException {
		int numberOfConcerts = _concerts.size();

		if (numberOfConcerts == _maxConcerts) {
			throw new RemoteException();
		}
		
		Concert newConcert = new ConcertServant(concert, numberOfConcerts);
		_concerts.put(newConcert.getId().intValue(), newConcert);
		return newConcert;
	}

	/**
	 * @see Concert.ConcertService#getConcert()
	 */
	@Override
	public synchronized Concert getConcert(Long id) throws RemoteException {
		return _concerts.get(id.intValue());
	}

	/**
	 * @see Concert.ConcertService#updateConcert()
	 */
	@Override
	public synchronized boolean updateConcert(Concert concert) throws RemoteException {
		if (_concerts.get(concert.getId().intValue()) == null) {
			return false;
		} else {
			_concerts.put(concert.getId().intValue(), concert);
			return true;
		}
	}

	/**
	 * @see Concert.ConcertService#deleteConcert()
	 */
	@Override
	public synchronized boolean deleteConcert(Long id) throws RemoteException {
		if (!_concerts.containsKey(id.intValue())) {
			return false;
		} else {
			_concerts.remove(id.intValue());
			return true;
		}
	}

	/**
	 * @see Concert.ConcertService#getAllConcerts()
	 */
	@Override
	public synchronized List<Concert> getAllConcerts() throws RemoteException {
		List<Concert> list = new ArrayList<Concert>();
		list.addAll(_concerts.values());
		return list;
	}

	/**
	 * @see Concert.ConcertService#clearConcerts()
	 */
	@Override
	public synchronized void clear() throws RemoteException {
		_concerts.clear();
	}

}
