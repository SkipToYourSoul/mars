package com.zeedoo.mars.service;


/**
 * Service that maintains the states of SUN systems
 *
 */
public interface SunManagementService {
	
	/**
	 * Business logic when we receive a message from a Sun device
	 * 
	 * Binds the current Sun Id with the given Socket Address
	 * duplicate ip address, update sun id
	 * duplicate sun id, update ip address
	 * @param sunStatus
	 * @return
	 */
	void onSunMessageReceived(String sunMacAddress, String ipAddress, Integer port);

	/**
	 * Business logic when sun connection is established
	 * Inserts socket address / port pair with a null sun id upon connection establishment with a Sun
	 * This method will set sunId to NULL if socket address / port already exists
	 * @param ipAddress
	 * @param port
	 * @return
	 */
	void onSunConnectionEstablished(String ipAddress, Integer port);
	
	/**
	 * Business logic when sun connection is established
	 * Inserts socket address / port pair with a null sun id upon connection establishment with a Sun
	 * This method will set sunId to NULL if socket address / port already exists
	 * @param ipAddress
	 * @param port
	 * @return
	 */
	void onSunConnectionInterrupted(String ipAddress, Integer port);
}
