package t;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class T
 */
@WebServlet("/T")
public class T extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public T() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		if(session.getAttribute("x") == null)
			session.setAttribute("x", 1);
		else
			session.setAttribute("x", Integer.parseInt(session.getAttribute("x").toString()) + 1);
		System.out.println("|" + request.getParameter("x") + "|");
		response.getWriter().write(session.getAttribute("x").toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("post....");
		
		String a = request.getParameter("a");
		String b = request.getParameter("b");
		boolean match = true;
		if (a.length() != b.length())
			match = false;
		else {
			for (int i = 0; i < a.length(); i++) {
				if (a.charAt(i) != b.charAt(a.length() - i - 1)) {
					match = false;
					break;
				}
			}
		}
		if (match)
			response.getOutputStream().write("true".getBytes());
		else
			response.getOutputStream().write("false".getBytes());
	}

}
