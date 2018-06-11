package servlets;

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

import database.DBConnection;

@WebServlet("/messages")
public class MessagesServlet extends HttpServlet {
    
    public String getHTMLString(String filePath, String message) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(filePath));
	String line = "";
	StringBuffer buffer = new StringBuffer();
	while ((line = reader.readLine()) != null) {
	    buffer.append(line);
	}
	reader.close();
	String page = buffer.toString();

	page = MessageFormat.format(page, message);

	return page;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String message = null;
	int userid = 0;
	HttpSession session = req.getSession(false);
	if (session != null && session.getAttribute("logged") != null) {
	    message = (String)session.getAttribute("username");
	    userid = (int) session.getAttribute("userid");
	} else {
	    session = req.getSession(true);
	    session.setAttribute("message", "error");
	    resp.sendRedirect("login");
	}
	String page = getHTMLString(req.getServletContext().getRealPath("/html/messagesPage.html"), message);
	resp.getWriter().write(page);
    }
}
