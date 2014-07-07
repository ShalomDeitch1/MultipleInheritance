package multipleInheritance;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PojoService extends HttpServlet  {

	private static final long serialVersionUID = 1L;
	private Object owner;
	private Method method;

	   
	
	   public PojoService(Object owner, Method method) {
		this.owner = owner;
		this.method = method;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
          	try (PrintWriter out = response.getWriter()) {
				Object content = method.invoke(owner);
				out.println(content.toString());
			} catch (IllegalAccessException  | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			} 
	   }

}
