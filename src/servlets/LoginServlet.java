package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import content.Users;
import database.ApplicationDatabase;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String infoMessage = null;
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		ApplicationDatabase data = new ApplicationDatabase();
		int emailRows = data.checkEmail(email);
		if (emailRows == 1) {
			String storedPasswordHash = data.loginUser(email, password);
			System.out.println(storedPasswordHash);
			if (BCrypt.checkpw(password, storedPasswordHash) == false) {
				infoMessage = "Email or Password is invalid!";
				String page = getHTMLString(req.getServletContext().getRealPath("/html/loginForm.html"), infoMessage);
				resp.getWriter().write(page);
			} else {
				infoMessage = "User was found! Success!";
				int userid = data.checkUserid(email);
				String username = data.findName(userid);
				HttpSession session = req.getSession(true);
				session.setAttribute("logged", "yes");
				session.setAttribute("userid", userid);
				session.setAttribute("username", username);
				resp.sendRedirect("home");
			}
		} else {
			infoMessage = "Email or Password is invalid!";
			String page = getHTMLString(req.getServletContext().getRealPath("/html/loginForm.html"), infoMessage);
			resp.getWriter().write(page);
		}
	}  
	
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
		HttpSession session = req.getSession(true);
		String message = "";
		if(session.getAttribute("message") != null) {
			message = "You need to login first!";
			session.invalidate();
		}
		String page = getHTMLString(req.getServletContext().getRealPath("/html/loginForm.html"), message);
		resp.getWriter().write(page);
	}
}
