package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import content.Messages;
import content.Users;

public class ApplicationDatabase {

	public int registerUser(Users user) {
		int updatedRows = 0;
		String email = user.getEmail();
		if (checkEmail(email) == 0) {
			try {
				Connection connection = DBConnection.getConnectionToDatabase();
				String sql = "insert into Users (firstname, lastname, email, password) values (?,?,?,?)";
				java.sql.PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, user.getFirstname());
				statement.setString(2, user.getLastname());
				statement.setString(3, user.getEmail());
				statement.setString(4, user.getPassword());

				updatedRows = statement.executeUpdate();
				statement.close();
				connection.close();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		} else {
			updatedRows = 404;
		}
		return updatedRows;
	}

	public int checkEmail(String email) {
		int result = 0;
		try {
			Connection connection = DBConnection.getConnectionToDatabase();
			String sql = "select count(email) AS email from Users where email='" + email + "'";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				result = set.getInt("email");
			}
			statement.close();
			connection.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return result;
	}
	
	public int checkUserid(String email) {
		int result = 0;
		try {
			Connection connection = DBConnection.getConnectionToDatabase();
			String sql = "select userid from Users where email='" + email + "'";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			while (set.next()) {
				result = set.getInt("userid");
			}
			statement.close();
			connection.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return result;
	}

	public String loginUser(String email, String password) {
		String result = null;
		try {
			Connection connection = DBConnection.getConnectionToDatabase();
			String sql = "select password from Users where email='" + email + "'";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				result = set.getString(1);
			} else {
				result = null;
			}
			statement.close();
			connection.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return result;
	}
	
	public String findName(int userid) {
		String result = null;
		try {
			Connection connection = DBConnection.getConnectionToDatabase();
			String sql = "select firstname, lastname from Users where userid='" + userid + "'";
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			if (set.next()) {
				result = set.getString("firstname")+" "+set.getString("lastname");
			} else {
				result = null;
			}
			statement.close();
			connection.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return result;
	}
	
	public JSONObject findMessages(int userid) {
	    JSONObject j = new JSONObject();
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "SELECT Messages.msgdate, Messages.msgid, Messages.userid, Users.firstname, Users.lastname, Messages.content, Messages.status, DATE_FORMAT(Messages.msgdate, \"%e.%m.%Y | %H:%i\") AS formatDate FROM Users, Messages WHERE ((Users.userid=Messages.userid) AND (Messages.userid=?)) OR (Users.userid=Messages.userid AND Messages.status='public')  ORDER BY msgdate DESC;";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    //String name = req.getParameter("name");
		    statement.setString(1, Integer.toString(userid));
		    ResultSet resultSet = statement.executeQuery();
		    JSONArray myContent = new JSONArray();
		    JSONArray myFirstname = new JSONArray();
		    JSONArray myLastname = new JSONArray();
		    JSONArray myDate = new JSONArray();
		    JSONArray msgId = new JSONArray();
		    JSONArray msgUsers = new JSONArray();
		    JSONArray currUser = new JSONArray();
		    JSONArray msgStatus = new JSONArray();
		    while (resultSet.next()) {
			myContent.put(resultSet.getString("content"));
			myFirstname.put(resultSet.getString("firstname"));
			myLastname.put(resultSet.getString("lastname"));
			myDate.put(resultSet.getString("formatDate"));
			msgId.put(resultSet.getString("msgid"));
			msgUsers.put(resultSet.getString("userid"));
			msgStatus.put(Character.toUpperCase(resultSet.getString("status").charAt(0)) + resultSet.getString("status").substring(1));
		    }
		    currUser.put(Integer.toString(userid));
		    j.put("content",myContent);
		    j.put("firstname",myFirstname);
		    j.put("lastname",myLastname);
		    j.put("date",myDate);
		    j.put("msgid",msgId);
		    j.put("users",msgUsers);
		    j.put("currUser",currUser);
		    j.put("status",msgStatus);
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		} catch (JSONException e) {
		    System.out.println(e.getMessage());
		}
	    return j;
	}
	
	public JSONObject lastMsgid() throws JSONException {
	    JSONObject j = new JSONObject();
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "SELECT max(msgid) AS msgid FROM Messages;";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    //String name = req.getParameter("name");
		    ResultSet resultSet = statement.executeQuery();
		    JSONArray msgId = new JSONArray();
		    while (resultSet.next()) {
			msgId.put(resultSet.getString("msgid"));
		    }
		    j.put("msgid",msgId);
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		} catch (JSONException e) {
		    System.out.println(e.getMessage());
		}
	    return j;
	}
	
	public int addMessage(Messages usrMsg) {
	    int updatedRows = 0;
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "INSERT INTO Messages (msgdate, content, status, userid) VALUES (NOW(),?,?,?)";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    statement.setString(1, usrMsg.getMsgcontent());
		    statement.setString(2, usrMsg.getMsgStatus());
		    statement.setString(3, Integer.toString(usrMsg.getUserId()));
		    updatedRows = statement.executeUpdate();
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
	    return updatedRows;
	}
	
	public int deleteMessage(String msgId) {
	    int updatedRows = 0;
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "DELETE FROM Messages WHERE msgid=?";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    statement.setString(1, msgId);
		    updatedRows = statement.executeUpdate();
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
	    return updatedRows;
	}
	
	public JSONObject findMessage(String msgId) throws JSONException {
	    JSONObject j = new JSONObject();
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "SELECT * FROM Messages WHERE msgid=?";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    statement.setString(1, msgId);
		    ResultSet resultSet = statement.executeQuery();
		    JSONArray myContent = new JSONArray();
		    JSONArray msgStatus = new JSONArray();
		    JSONArray messageId = new JSONArray();
		    while (resultSet.next()) {
			messageId.put(resultSet.getString("msgid"));
			myContent.put(resultSet.getString("content"));
			msgStatus.put(Character.toUpperCase(resultSet.getString("status").charAt(0)) + resultSet.getString("status").substring(1));
		    }
		    j.put("content",myContent);
		    j.put("status",msgStatus);
		    j.put("msgid",messageId);
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		} catch (JSONException e) {
		    System.out.println(e.getMessage());
		}
	    return j;
	}
	
	public int updateMessage(String msgId, String status, String message) {
	    int updatedRows = 0;
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "UPDATE Messages SET content=?, status=? WHERE msgid=?";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    statement.setString(1, message);
		    statement.setString(2, status);
		    statement.setString(3, msgId);
		    updatedRows = statement.executeUpdate();
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
	    return updatedRows;
	}
	
	public JSONObject searchMessages(String searchVal, int userid) throws JSONException {
	    JSONObject j = new JSONObject();
	    try {
		    Connection connection = DBConnection.getConnectionToDatabase();
		    String sql = "SELECT Messages.msgdate, Messages.msgid, Messages.userid, Users.firstname, Users.lastname, Messages.content, Messages.status, DATE_FORMAT(Messages.msgdate, \"%e.%m.%Y | %H:%i\") AS formatDate FROM Users, Messages WHERE ((Users.userid=Messages.userid) AND (Messages.userid=?) AND (Messages.content LIKE ?)) OR ((Users.userid=Messages.userid) AND (Messages.status='public') AND (Messages.content LIKE ?))  ORDER BY msgdate DESC;";
		    java.sql.PreparedStatement statement = connection.prepareStatement(sql);
		    statement.setString(1, Integer.toString(userid));
		    statement.setString(2, "%"+searchVal+"%");
		    statement.setString(3, "%"+searchVal+"%");
		    ResultSet resultSet = statement.executeQuery();
		    JSONArray myContent = new JSONArray();
		    JSONArray myFirstname = new JSONArray();
		    JSONArray myLastname = new JSONArray();
		    JSONArray myDate = new JSONArray();
		    JSONArray msgId = new JSONArray();
		    JSONArray msgUsers = new JSONArray();
		    JSONArray currUser = new JSONArray();
		    JSONArray msgStatus = new JSONArray();
		    while (resultSet.next()) {
			myContent.put(resultSet.getString("content"));
			myFirstname.put(resultSet.getString("firstname"));
			myLastname.put(resultSet.getString("lastname"));
			myDate.put(resultSet.getString("formatDate"));
			msgId.put(resultSet.getString("msgid"));
			msgUsers.put(resultSet.getString("userid"));
			msgStatus.put(Character.toUpperCase(resultSet.getString("status").charAt(0)) + resultSet.getString("status").substring(1));
		    }
		    currUser.put(Integer.toString(userid));
		    j.put("content",myContent);
		    j.put("firstname",myFirstname);
		    j.put("lastname",myLastname);
		    j.put("date",myDate);
		    j.put("msgid",msgId);
		    j.put("users",msgUsers);
		    j.put("currUser",currUser);
		    j.put("status",msgStatus);
		    statement.close();
		    connection.close();
		    //System.out.println(myContent);
		    //System.out.println(sql);
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		} catch (JSONException e) {
		    System.out.println(e.getMessage());
		}
	    return j;
	}
}
