package com.amazonaws.lambda.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Service {
	
	private Logger log = LoggerFactory.getLogger(Service.class);
    private EmailService eservice = new EmailService();
	public Service(){}
	
    public Response select(Request req) {
		Response res = new Response();
		switch(req.getResource()) {
		case("/case"): return plantiff(req,res);
		case("/revapp/{caseid}"):return revapp(req,res);
		case("/revrej/{caseid}"): return revrej(req,res);
		case("/approve/{caseid}"): return approve(req,res);
		case("/reject/{caseid}"): return reject(req,res);
		default:break;
		}
		res.setStatusCode(500);
		res.setBody("Not an expected resource");
		return res;
	}
	public Connection getConnection() {
		Connection con = null;
		try {
	      con = DriverManager.getConnection("jdbc:mysql://database-1.cse1mvnkc219.us-west-2.rds.amazonaws.com:3306/ARSystem","admin","Jaguar789");
	    }
		catch(SQLException s) {
			log.error("Error", s);
		}
		return con;
	}
	
    public Response plantiff(Request req, Response res) {
    	Connection con = getConnection();
    	Random r = new Random();
    	int caseid = r.nextInt(1000);
    	String q = "insert into Plantiff (case_id ,name ,email, case_file_time) values (?,?,?,?)";
       	ObjectMapper obj = new ObjectMapper();
    	PreparedStatement ps = null;
    	Plantiff p = null;
    	
    	try {		
    		p = obj.readValue(req.getBody(),Plantiff.class);	
    		ps = con.prepareStatement(q);
    		ps.setInt(1,caseid);
            ps.setString(2, p.getName());
            ps.setString(3, p.getEmail());
            ps.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
               if(ps.executeUpdate() == 1) {res.setBody("Case saved"); res.setStatusCode(200); }
               else {res.setBody("There is a problem in plantiff details");res.setStatusCode(500);}
    	}
    	catch(Exception e) {
    		log.error("error",e);
    		res.setStatusCode(500);
    		res.setBody(e.getMessage());
    	}
    	
    	finally {
    		safeClose(con);
    		safeClose(ps);
    	}
    	//sending confirmation mail to plantiff
    	eservice.mail("sarang.t@theintellify.com", p.getEmail(),"Case registered","Your caseid is  "+caseid);
    	//sending mail to reviewer
    	eservice.mail("sarang.t@theintellify.com","sarangtak8056@gmail.com","Review case" + caseid, new StringBuilder().append("Click on this link after review : https://fnd288we8d.execute-api.us-west-2.amazonaws.com/pro/revapp/"+caseid).append('\n').append("Click on this link to reject case : https://fnd288we8d.execute-api.us-west-2.amazonaws.com/pro/revrej/"+caseid).toString());
    	return res;
   }
    
    public Response revapp(Request req, Response res) {
    	Connection con = getConnection();
    	String[] ar = req.getPath().split("/");
    	String caseid = ar[ar.length -1];
    	String q = "update Plantiff set case_status = ? where case_id = ?";
    	PreparedStatement ps = null;
    	try {
    		ps = con.prepareStatement(q);
    		ps.setString(1,"REVIEWED");
    		ps.setString(2,caseid);
    		if(ps.executeUpdate() == 1) {
    			res.setBody("Case id  "+ caseid + " has been REVIEWED");
    		    res.setStatusCode(200); }
    		else {res.setBody("Problem in saving data to DB"); res.setStatusCode(500);}
    		
    	}
    	catch(Exception e) {
    		log.error("error", e);
    		res.setStatusCode(500);
    		res.setBody(e.getMessage());
    	}
    	finally {
    		safeClose(con);
    		safeClose(ps);
    	}
       //sending email to approver
       eservice.mail("sarang.t@theintellify.com","160770119501@socet.edu.in","Case for Approval",new StringBuilder().append("Click on this line to Approve : https://fnd288we8d.execute-api.us-west-2.amazonaws.com/pro/approve/"+caseid).append('\n').append("Click on this link to Reject : https://fnd288we8d.execute-api.us-west-2.amazonaws.com/pro/reject/"+caseid).toString()); 	
       return res;
   }
    
    public Response revrej(Request req, Response res) {
    	Connection con = getConnection();
    	String[] ar = req.getPath().split("/");
    	int caseid = Integer.parseInt(ar[ar.length -1]);
    	String q = "update Plantiff set case_status = ? where case_id = ?";
    	String qs = "select email from Plantiff where case_id = ?";
    	String cemail = null;
    	PreparedStatement ps = null;
    	PreparedStatement pss = null;
    	ResultSet rs = null;
    	try {
    		ps = con.prepareStatement(q);
    		ps.setString(1,"REJECTED");
    		ps.setInt(2,caseid);
    		if(ps.executeUpdate() == 1) {
    			res.setBody("Case id  "+ caseid + "  has been REJECTED");
    		    res.setStatusCode(200); }
    		else {res.setBody("Problem in saving data to DB"); res.setStatusCode(500);}
    		
    		pss = con.prepareStatement(qs);
    		pss.setInt(1,caseid);
            rs = pss.executeQuery();
            while(rs.next()) {
            	cemail = rs.getString(1);
            }
    	}
    	catch(Exception e) {
    		log.error("error", e);
    		res.setStatusCode(500);
    		res.setBody(e.getMessage());
    	}
    	finally {
    		safeClose(con);
    		safeClose(ps);
    		safeClose(pss);
            safeClose(rs);
    	}
       //sending email to client about rejection
       eservice.mail("sarang.t@theintellify.com",cemail,"Case id : " + caseid,"Your case has been rejected "); 	
       return res;
   }
    
    public Response approve(Request req, Response res) {
    	Connection con = getConnection();
    	String[] ar = req.getPath().split("/");
    	int caseid = Integer.parseInt(ar[ar.length -1]);
       	String q = "update Plantiff set case_status = ? where case_id = ?";
       	String qs = "select email from Plantiff where case_id = ?";
    	String cemail = null;
    	PreparedStatement ps = null;
    	PreparedStatement pss = null;
    	ResultSet rs = null;
    	try {
       		ps = con.prepareStatement(q);
    		ps.setString(1,"APPROVED");
    		ps.setInt(2,caseid);
    		if(ps.executeUpdate() == 1) {
    			res.setBody("Case id  "+ caseid + "  has been APPROVED");
    		    res.setStatusCode(200); }
    		else {res.setBody("Problem in saving data to DB"); res.setStatusCode(500);}
    		
    		pss = con.prepareStatement(qs);
    		pss.setInt(1,caseid);
            rs = pss.executeQuery();
            while(rs.next()) {
            	cemail = rs.getString(1);
            }
    		
    	}
    	catch(Exception e) {
    		log.error("error", e);
    		res.setStatusCode(500);
    		res.setBody(e.getMessage());
    	}
    	finally {
    		safeClose(con);
    		safeClose(ps);
    		safeClose(pss);
            safeClose(rs);
    	}
       //sending email to client about approval
       eservice.mail("sarang.t@theintellify.com",cemail,"Case id : " + caseid , "Your case has been approved"); 	
       //sending email to reviewer about approval
       eservice.mail("sarang.t@theintellify.com","sarangtak8056@gmail.com" ,"Case id : " + caseid, "Case has been approved");
       return res;
   }
    
    public Response reject(Request req, Response res) {
    	Connection con = getConnection();
    	String[] ar = req.getPath().split("/");
    	int caseid = Integer.parseInt(ar[ar.length -1]);
    	String q = "update Plantiff set case_status = ? where case_id = ?";
    	String qs = "select email from Plantiff where case_id = ?";
    	String cemail = null;
    	PreparedStatement ps = null;
    	PreparedStatement pss = null;
    	ResultSet rs = null;
    	try {
    		ps = con.prepareStatement(q);
    		ps.setString(1,"REJECTED");
    		ps.setInt(2,caseid);
    		if(ps.executeUpdate() == 1) {
    			res.setBody("Case id  "+ caseid + "  has been REJECTED");
    		    res.setStatusCode(200); }
    		else {res.setBody("Problem in saving data to DB"); res.setStatusCode(500);}
    		
    		pss = con.prepareStatement(qs);
    		pss.setInt(1,caseid);
            rs = pss.executeQuery();
            while(rs.next()) {
            	cemail = rs.getString(1);
            }
    	}
    	catch(Exception e) {
    		log.error("error", e);
    		res.setStatusCode(500);
    		res.setBody(e.getMessage());
    	}
    	finally {
    		safeClose(con);
			safeClose(ps);
    		safeClose(pss);
            safeClose(rs);
    	}
       //sending email to client
       eservice.mail("sarang.t@theintellify.com",cemail,"Case id :" + caseid,"Your case has been rejected");
       //sending email to reviewer 
       eservice.mail("sarang.t@theintellify.com","sarangtak8056@gmail.com" ,"Case id : " + caseid, "Case has been rejected");
       return res;
   }
    public void safeClose(Connection c) {
        if(c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                log.error("", e);
            }
        }
    }
    
    private void safeClose(PreparedStatement stmt) {
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            	 log.error("", e);
            }
        }
    }
    
    private void safeClose(ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
        }
    }
}