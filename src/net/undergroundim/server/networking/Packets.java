package net.undergroundim.server.networking;

import net.undergroundim.server.Client;
import net.undergroundim.server.Constants;

/**
 * 
 * @author Troy
 *
 */
public class Packets {
	
	/**
	 * This will generate the friend's list
	 * for the selected user id.
	 * 
	 * @param user_id
	 * @return String
	 */
	public static String friendList(int user_id){
		String packet = PacketHeaders.FRIEND_LIST.getHeader() + "1ª";
		
		for(Client c : Constants.getJdbc().getFriends(user_id)){
			packet += c.getUser_id() + "º" + 
					c.getUsername() + "º" + 
					c.getScreen_name() + "º" +
					c.getStatus() + "º" +
					c.isOnline() + ",";
		}

		return packet;
	}
	
	/**
	 * Get all friend Requests.
	 * 
	 * @param user_id
	 * @return String
	 */
	public static String friendRequests(int user_id){
		String packet = PacketHeaders.FRIEND_ADD.getHeader() + "1ª";
		
		packet += Constants.getJdbc().getFriendRequests(user_id);

		if(!packet.equals("9ª1ª"))
			return packet;
		else
			return null;
	}
	
	/**
	 * View a user's profile.
	 * 
	 * @param user_id
	 * @return String
	 */
	public static String viewProfile(int user_id, int friend_id){
		String packet = PacketHeaders.VIEW_PROFILE.getHeader() + "º";
		
		packet += Constants.getJdbc().viewProfile(user_id, friend_id);
		
		return packet;
	}

}