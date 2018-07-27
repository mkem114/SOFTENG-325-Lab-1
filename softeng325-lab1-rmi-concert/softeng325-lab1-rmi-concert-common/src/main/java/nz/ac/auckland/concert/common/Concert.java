package nz.ac.auckland.concert.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to represent remotely accessible Concerts. This interface is
 * intended to be implemented by classes whose instances' methods can be invoked
 * remotely.
 * 
 */
public interface Concert extends Remote {

	/**
	 * Returns a local reference to a Graphic object whose attributes describe the
	 * state of the Remote object this method is called on.
	 */
	Concert getDetails() throws RemoteException;

	/**
	 * Returns the unique ID of the Concert object. The ConcertFactory that created
	 * this Concert object assigned the value; the ConcertFactory assigns a unique
	 * ID which is a count of the number of concerts it has previously created.
	 * Hence, the first Concert created by a ConcertFactory will have a unique ID of
	 * zero, with Concerts created subsequently having IDs of 1 through n-1.
	 */
	Long getId() throws RemoteException;
	
	/**
	 * Sets the unique ID of the concert 
	 */
	void setId(int id) throws RemoteException;
}
