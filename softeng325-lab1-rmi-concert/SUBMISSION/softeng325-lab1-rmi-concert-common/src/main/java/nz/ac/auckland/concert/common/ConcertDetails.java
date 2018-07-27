package nz.ac.auckland.concert.common;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

/**
 * Class to represent the state of a Concert object. ConcertDetail instances are
 * intended to be passed between clients and servers and so ConcertDetail
 * necessarily implements the Serializable interface.
 * 
 */
public class ConcertDetails implements Concert, Serializable {

	private static final long serialVersionUID = 1L;

	private Long _id; // unique id of concert
	private String _title; // title of concert
	private DateTime _date; // date and time of concert

	/**
	 * Constructs a ConcertDetails object with a set id, title and date
	 */
	public ConcertDetails(Long id, String title, DateTime date) {
		_id = id;
		_title = title;
		_date = date;
	}

	/**
	 * Constructs a ConcertDetails object with a set id, title and date
	 */
	public ConcertDetails(String title, DateTime date) {
		this(null, title, date);
	}

	/**
	 * Sets the unique id of the concert 
	 */
	public void setId(int id) {
		_id = new Long(id);
	}

	/**
	 * Sets the time of the concert
	 */
	public void setDate(DateTime date) {
		_date = date;
	}

	/**
	 * Gets the id of the concert
	 */
	public Long getId() {
		return _id;
	}

	/**
	 * Gets the title of the concert
	 */
	public String getTitle() {
		return _title;
	}

	/**
	 * Gets the date of the concert
	 */
	public DateTime getDate() {
		return _date;
	}

	/**
	 * Checks two ConcertDetails are equal on id, title and date/time
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ConcertDetails))
			return false;
		if (other == this)
			return true;

		ConcertDetails rhs = (ConcertDetails) other;
		return new EqualsBuilder().append(_id, rhs.getId()).append(_title, rhs.getTitle()).append(_date, rhs.getDate())
				.isEquals();
	}

	/**
	 * Creates a hashcode based on id and title
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(getClass().getName()).append(_id).append(_title).toHashCode();
	}

	/**
	 * Prints all the concert details to a string
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName());
		buffer.append(":id=");
		buffer.append(_id);
		buffer.append(",title=");
		buffer.append(_title);
		buffer.append(",date=");
		buffer.append(_date);
		buffer.append("]");
		return buffer.toString();
	}

	/**
	 * see toString
	 */
	@Override
	public Concert getDetails() throws RemoteException {
		return this;
	}
}
