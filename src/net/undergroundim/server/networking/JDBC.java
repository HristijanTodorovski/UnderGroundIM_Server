package net.undergroundim.server.networking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.undergroundim.server.Client;
import net.undergroundim.server.Constants;
import net.undergroundim.server.Server;



/**
 * 
 * @author Troy
 *
 */
public class JDBC {
	public Connection connection;
	private PreparedStatement preparedStatement;
	private String sql;
	private ResultSet rs;
	private int count = 0;
	
	/**
	 * Constructor
	 */
	public JDBC(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}catch(InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		//Connection String
		String url = "jdbc:mysql://" + Constants.getMysqlHost() + ":" + Constants.getMysqlPort() + "/" + Constants.getMysqlDatabase();
		
		try {
			connection = DriverManager.getConnection(url, Constants.getMysqlUsername(), Constants.getMysqlPassword());
		}catch(SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Check if a MySQL connection is established
	 * else connect.
	 */
	public void check(){
		try{
			if(connection.isClosed()){
				//Connection String
				String url = "jdbc:mysql://" + Constants.getMysqlHost() + ":" + Constants.getMysqlPort() + "/" + Constants.getMysqlDatabase();
				
				connection = DriverManager.getConnection(url, Constants.getMysqlUsername(), Constants.getMysqlPassword());
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Register a user.
	 * 
	 * @param username
	 * @param password
	 * @return boolean
	 */
	public boolean registerUser(String username, String password){
		check();
		
		try{
			//First check to make sure this user name doesn't exist
			sql = "SELECT COUNT(*) FROM users WHERE username = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			count = 0;
			
			while(rs.next())
				count = rs.getInt(1);
			
			//If this User name is available, insert the new user.
			if(count < 1){
				sql = "INSERT INTO users (username, screen_name, password, made_date) VALUES (?,?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, username);
				preparedStatement.setString(3, password);
				preparedStatement.setString(4, Server.getDate());
				preparedStatement.executeUpdate();
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get the user on login.
	 * 
	 * @param username
	 * @param password
	 * @return Client
	 */
	public Client getUser(int id, String username, String password){
		check();
		
		try{
			sql = "SELECT user_id, username, screen_name, email, age, sex, location, about, made_date " +
					"FROM users WHERE username = ? AND password = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			rs = preparedStatement.executeQuery();
			
			while(rs.next())
				return new Client(rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getByte(5),
						rs.getByte(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						0,
						true);

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get the friends list from a user's id.
	 * 
	 * @param user_id
	 * @return ArrayList<Client>
	 */
	public ArrayList<Client> getFriends(int user_id){
		check();
		
		ArrayList<Client> friends = new ArrayList<Client>();
		
		try{
			sql = "SELECT u.user_id, u.username, u.screen_name " +
					"" +
					"FROM friends f " +
					"INNER JOIN users u ON f.friend_id = u.user_id " +
					"" +
					"WHERE f.user_id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user_id);
			rs = preparedStatement.executeQuery();
			
			while(rs.next()){
				friends.add(new Client(rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						null,
						(byte) -1,
						(byte) -1,
						null,
						null,
						null,
						0,
						Constants.isClientOnline(rs.getInt(1))));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return friends;
	}
	
	/**
	 * Send a friend request.
	 * 
	 * @param user_id
	 * @param friend_id
	 */
	public boolean addFriend(int user_id, String friend_name){
		check();
		
		try{
			//Get the friend_id
			sql = "SELECT user_id FROM users WHERE username = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, friend_name);
			rs = preparedStatement.executeQuery();
			int friend_id = 0;
			
			while(rs.next())
				friend_id = rs.getInt(1);
			
			if(friend_id > 0){
				//Check to make sure this request does not exist
				sql = "SELECT COUNT(*) FROM friend_requests WHERE friend_id = ? OR friend_id = ? AND user_id = ? OR user_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, friend_id);
				preparedStatement.setInt(2, user_id);
				preparedStatement.setInt(3, user_id);
				preparedStatement.setInt(4, friend_id);
				rs = preparedStatement.executeQuery();
				count = 0;
				
				while(rs.next())
					count = rs.getInt(1);
				
				//If the request doesn't exist.
				if(count < 1){
					sql = "INSERT INTO friend_requests (user_id, friend_id, made_date) VALUES (?,?,?)";
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, user_id);
					preparedStatement.setInt(2, friend_id);
					preparedStatement.setString(3, Server.getDate());
					preparedStatement.executeUpdate();
					return true;
				}else{
					System.out.println("Already friends?");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Delete friend.
	 * 
	 * @param user_id
	 * @param friend_id
	 */
	public boolean deleteFriend(int user_id, int friend_id){
		check();
		
		try{
			sql = "SELECT COUNT(*) FROM friends WHERE user_id = ? and friend_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, friend_id);
			rs = preparedStatement.executeQuery();
			count = 0;
			
			while(rs.next())
				count = rs.getInt(1);
			
			if(count > 0){
				sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, user_id);
				preparedStatement.setInt(2, friend_id);
				preparedStatement.executeUpdate();
				
				sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, friend_id);
				preparedStatement.setInt(2, user_id);
				preparedStatement.executeUpdate();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Send a friend request.
	 * 
	 * @param user_id
	 * @param friend_id
	 */
	public boolean acceptFriend(String username, int friend_id){
		check();
		
		try{
			//Check to make sure this request does exist
			sql = "SELECT user_id FROM users WHERE username = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			int user_id = 0;
				
			while(rs.next())
				user_id = rs.getInt(1);
			
			//Check to make sure this request does exist
			sql = "SELECT COUNT(*) FROM friend_requests WHERE friend_id = ? AND user_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, friend_id);
			preparedStatement.setInt(2, user_id);
			rs = preparedStatement.executeQuery();
			count = 0;
				
			while(rs.next())
				count = rs.getInt(1);
				
			//If the request doesn't exist.
			if(count > 0){
				sql = "INSERT INTO friends (user_id, friend_id, made_date) VALUES (?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, user_id);
				preparedStatement.setInt(2, friend_id);
				preparedStatement.setString(3, Server.getDate());
				preparedStatement.executeUpdate();
					
				sql = "INSERT INTO friends (user_id, friend_id, made_date) VALUES (?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, friend_id);
				preparedStatement.setInt(2, user_id);
				preparedStatement.setString(3, Server.getDate());
				preparedStatement.executeUpdate();
					
				sql = "DELETE FROM friend_requests WHERE user_id = ? AND friend_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, user_id);
				preparedStatement.setInt(2, friend_id);
				preparedStatement.executeUpdate();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Send a friend request.
	 * 
	 * @param user_id
	 * @param friend_id
	 */
	public boolean declineFriend(int user_id, String username){
		check();
		
		try{
			//Check to make sure this request does exist
			sql = "SELECT user_id FROM users WHERE username = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			int friend_id = 0;
				
			while(rs.next())
				friend_id = rs.getInt(1);

			//Check to make sure this request does exist
			sql = "SELECT COUNT(*) FROM friend_requests WHERE friend_id = ? AND user_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, friend_id);
			preparedStatement.setInt(2, user_id);
			rs = preparedStatement.executeQuery();
			count = 0;
				
			while(rs.next())
				count = rs.getInt(1);
				
			//If the request doesn't exist.
			if(count > 0){
				sql = "DELETE FROM friend_requests WHERE user_id = ? AND friend_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, user_id);
				preparedStatement.setInt(2, friend_id);
				preparedStatement.executeUpdate();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Get the friends list from a user's id.
	 * 
	 * @param user_id
	 * @return ArrayList<Client>
	 */
	public String getFriendRequests(int user_id){
		check();
		
		String response = "";
		String username = Constants.getClient(user_id).client.getUsername();
		
		try{
			sql = "SELECT u.username " +
					"FROM friend_requests f " +
					"INNER JOIN users u ON f.user_id = u.user_id " +
					"WHERE f.friend_id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user_id);
			rs = preparedStatement.executeQuery();
			
			while(rs.next()){
				response +=  rs.getString(1) + "º" + username + ",";
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Get the user's profile.
	 * 
	 * @return String
	 */
	public String viewProfile(int user_id, int friend_id){
		check();
		
		String response = "";
		
		try{
			sql = "SELECT COUNT(*) FROM friends WHERE user_id = ? and friend_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, friend_id);
			rs = preparedStatement.executeQuery();
			count = 0;
			
			while(rs.next())
				count = rs.getInt(1);
			
			if(count > 0){
				sql = "SELECT username, screen_name, email, age, sex, location, about, made_date " +
						"FROM users " +
						"WHERE user_id = ?";
				
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, friend_id);
				rs = preparedStatement.executeQuery();
				
				while(rs.next())
					response += rs.getString(1) + "º" +
							rs.getString(2) + "º" +
							rs.getString(3) + "º" +
							rs.getInt(4) + "º" +
							rs.getInt(5) + "º" +
							rs.getString(6) + "º" +
							rs.getString(7) + "º" +
							rs.getString(8);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Update the user's profile.
	 * 
	 * @param client
	 */
	public void updateProfile(Client client){
		check();
		
		try{
			sql = "UPDATE users SET screen_name = ?, email = ?, age = ?, sex = ?, location = ?, about = ? WHERE user_id = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, client.getScreen_name());
			preparedStatement.setString(2, client.getEmail());
			preparedStatement.setInt(3, client.getAge());
			preparedStatement.setInt(4, client.getSex());
			preparedStatement.setString(5, client.getLocation());
			preparedStatement.setString(6, client.getAbout());
			preparedStatement.setInt(7, client.getUser_id());
			preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the user's password.
	 * 
	 * @param user_id
	 * @param password
	 * @param newPassword
	 */
	public boolean updatePassword(int user_id, String password, String newPassword){
		check();
		
		try{
			sql = "SELECT COUNT(*) FROM users WHERE password = ? AND user_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, password);
			preparedStatement.setInt(2, user_id);
			rs = preparedStatement.executeQuery();
			count = 0;
				
			while(rs.next())
				count = rs.getInt(1);
				
			//If the password matches.
			if(count > 0){
				sql = "UPDATE users SET password = ? WHERE user_id = ?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, newPassword);
				preparedStatement.setInt(2, user_id);
				preparedStatement.executeUpdate();
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Disconnect from the database.
	 */
	public void disconnect(){
		try {
			connection.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}