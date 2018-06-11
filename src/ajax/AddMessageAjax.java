package ajax;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import content.Messages;
import content.Users;
import database.ApplicationDatabase;
import database.DBConnection;

@WebServlet("/addmessage")
public class AddMessageAjax extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	resp.setContentType("application/json");
	PrintWriter out = resp.getWriter();
	int userid = 0;
	HttpSession session = req.getSession(false);
	if (session != null && session.getAttribute("logged") != null) {
	    userid = (int) session.getAttribute("userid");
	    if (req.getParameter("name").equals("list")) {
		String usrMessage = req.getParameter("usrMessage");
		String msgStatus = req.getParameter("msgStatus");
		Messages newMsg = new Messages(usrMessage, msgStatus, userid);
		ApplicationDatabase data = new ApplicationDatabase();
		int result = 0;
		result = data.addMessage(newMsg);
		out.println(result);
	    }
	} else {
	    session = req.getSession(true);
	    session.setAttribute("message", "error");
	    resp.sendRedirect("login");
	}
    }
}