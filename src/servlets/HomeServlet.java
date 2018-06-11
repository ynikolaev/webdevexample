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

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	
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
		int userid = 0;
		String name = "";
		HttpSession session = req.getSession(false);
		if(session != null && session.getAttribute("logged") != null) {
			userid = (int)session.getAttribute("userid");
			name = (String)session.getAttribute("username");
		} else {
			session = req.getSession(true);
			session.setAttribute("message","error");
			resp.sendRedirect("login");
		}
		String page = getHTMLString(req.getServletContext().getRealPath("/html/homePage.html"), name);
		resp.getWriter().write(page);
	}
}
