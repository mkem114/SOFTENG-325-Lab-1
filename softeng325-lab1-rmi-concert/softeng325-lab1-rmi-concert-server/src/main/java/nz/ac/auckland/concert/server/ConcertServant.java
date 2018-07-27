package nz.ac.auckland.concert.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nz.ac.auckland.concert.common.Concert;

/**
 * An implementation of the Concert interface. A ConcertServant instance is a
 * remotely accessible object that represents a particular Concert
 * 
 */
public class ConcertServant extends UnicastRemoteObject implements Concert {

	private static final long serialVersionUID = 1L;

	private Concert _state;
	private int _id;

	/**
	 * Creates a new ConcertServant instance.
	 * 
	 * @param graphic
	 *            the state of the new ConcertServant object.
	 * @param id
	 *            the unique ID of the new ConcertServant object.
	 * @throws RemoteException
	 *             if the ConcertServant instance cannot be created. This can happen
	 *             if the RMI run-time does not have sufficient resources (e.g.
	 *             sockets) to host an additional remote object.
	 */
	public ConcertServant(Concert details, int id) throws RemoteException {
		super();
		this._state = details;
		details.setId(id);
		this._id = id;
	}

	/**
	 * @see Concert.Concert#getAllState()
	 */
	public synchronized Concert getDetails() throws RemoteException {
		return _state;
	}

	/**
	 * @see Concert.Concert#getId()
	 */
	public synchronized Long getId() throws RemoteException {
		return new Long(_id);
	}

	/**
	 * @see Concert.Concert#setId()
	 */
	@Override
	public synchronized void setId(int id) {
		// Does nothing as should only be assigned on construction
	}
}