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

import database.ApplicationDatabase;
import database.DBConnection;

@WebServlet("/searchmessage")
public class SearchMessagesAjax extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	resp.setContentType("application/json");
	PrintWriter out = resp.getWriter();
	int userid = 0;
	HttpSession session = req.getSession(false);
	if (session != null && session.getAttribute("logged") != null) {
	    userid = (int) session.getAttribute("userid");
	    String searchVal = req.getParameter("search");
	    ApplicationDatabase data = new ApplicationDatabase();
	    JSONObject jResults = new JSONObject();
	    try {
		jResults = data.searchMessages(searchVal, userid);
	    } catch (JSONException e) {
		e.printStackTrace();
	    }
	    out.println(jResults.toString());
	} else {
	    session = req.getSession(true);
	    session.setAttribute("message", "error");
	    resp.sendRedirect("login");
	}
    }
}