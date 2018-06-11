package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import content.Messages;
import content.Users;
import database.ApplicationDatabase;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
		
		Users user = new Users(firstname, lastname, email, passwordHash);
		ApplicationDatabase data = new ApplicationDatabase();
		int rows = data.registerUser(user);
		String infoMessage = null;
		if (rows == 0) {
			infoMessage = "User was not registred! Error occured!";
		} else if (rows == 404){
			infoMessage = "Email is already taken! Choose another one!";
		} else {
			resp.sendRedirect("login");
		}
		
		String page = getHTMLString(req.getServletContext().getRealPath("/html/registerForm.html"), infoMessage);
		resp.getWriter().write(page);
	}  
	
	public String getHTMLString(String filePath, String message) throws IOException{
		BufferedReader reader = new BufferedReader (new FileReader(filePath));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while((line=reader.readLine())!=null) {
			buffer.append(line);
		}
		reader.close();
		String page = buffer.toString();
		
		page = MessageFormat.format(page, message);
		
		return page;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String page = getHTMLString(req.getServletContext().getRealPath("/html/registerForm.html"), "");
		resp.getWriter().write(page);
	}
}
