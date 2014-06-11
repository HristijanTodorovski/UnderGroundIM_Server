package net.undergroundim.server.networking;

import net.undergroundim.server.Client;
import net.undergroundim.server.ClientThread;
import net.undergroundim.server.Constants;


/**
 * This class will send packets to all clients.
 * 
 * @author Troy
 *
 */
public class Broadcaster {
	
	/**
	 * Send a packet to all clients.
	 * 
	 * @param packet
	 */
	public static void broadcast(String packet){
		for(ClientThread ct : Constants.getClients())
			ct.sendPacket(packet);
	}
	
	/**
	 * Send a packet to all clients except
	 * the specified ID.
	 * 
	 * @param packet
	 * @param id
	 */
	public static void broadcastExcept(String packet, int id){
		for(ClientThread ct : Constants.getClients())
			if(ct.clientID != id)
				ct.sendPacket(packet);
	}
	
	/**
	 * Broadcast a packet to all online friends.
	 * 
	 * @param packet
	 * @param user_id
	 */
	public static void broadcastFriends(String packet, int user_id){
		for(Client c : Constants.getJdbc().getFriends(user_id)){
			for(ClientThread ct : Constants.getClients()){
				if(ct.client != null && ct.client.getUser_id() == c.getUser_id())
					ct.sendPacket(packet);
			}
		}
	}

}