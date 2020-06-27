package com.amazonaws.lambda.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
	
	 private Logger log = LoggerFactory.getLogger(Database.class);
	public Connection getConnection() {
		Connection con = null;
		try {
		  con = DriverManager.getConnection("jdbc:mysql://database-1.cse1mvnkc219.us-west-2.rds.amazonaws.com:3306/Bank","admin","Jaguar789");
		}
		catch(SQLException s) {
			log.error("error",s);
		}
		return con;
	}
	public Response post(Request req) {
		Connection con = getConnection();
		User user = req.getUser();
		Response res = new Response();
		String q = "insert into user(name, state, email) values(?,?,?)";
		PreparedStatement p = null;
		int u = 0;
		try {
		 p = con.prepareStatement(q);
		 p.setString(1,user.getName());
		 p.setString(2,user.getState());
		 p.setString(3, user.getEmail());
		 u = p.executeUpdate();
		 if(u > 0) res.setStatuscode(200);
		 else res.setStatuscode(500);
		}
		catch(SQLException s) {
			log.error("error",s);
			res.setStatuscode(500);
		}
		return res;
	}
	
	public Response get(Request req) {
		Connection con = getConnection();
		int id = req.getId();
		User u = new User();
		String q = "select * from user where id = ?";
		Response res = new Response();
		PreparedStatement p = null;
		ResultSet s = null;
		try {
		 p = con.prepareStatement(q);
		 p.setInt(1,id);
		 s = p.executeQuery();
			 while(s.next()) {
			 u.setId(s.getInt(1));
			 u.setName(s.getString(2));
			 u.setState(s.getString(3));
			 u.setEmail(s.getString(4));
			 res.setStatuscode(200);
		  }
		}
		catch(SQLException e) {
			log.error("error",e);
			res.setStatuscode(500);
		}
		res.setUser(u);
	    return res;
	}
	
}
