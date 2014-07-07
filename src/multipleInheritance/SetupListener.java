package multipleInheritance;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;

public class SetupListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
         
		addPojoServices(servletContext);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}
	

	private void addPojoServices(ServletContext servletContext) {
        List<Class<?>> classes = getClasses("");
		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(Controller.class)) {
				addControllersMappings(servletContext, clazz);
			}//if
		}//for
	}

	private void addControllersMappings(ServletContext servletContext, Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(RequestMapping.class)) {
				addServletWithMapping(servletContext, clazz, method);
			}//if
		}//for
	}

	private void addServletWithMapping(ServletContext servletContext, Class<?> clazz, Method method) {
		Annotation annotation = method.getAnnotation(RequestMapping.class);
		RequestMapping mapping = (RequestMapping) annotation;
		String path = mapping.value();
		try {
			Object newInstance = clazz.newInstance();
			PojoService pojoService = new PojoService(newInstance,  method);
			
			Dynamic addServlet = servletContext.addServlet(method.getName(),pojoService);
			addServlet.addMapping(path);

		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private List<Class<?>> getClasses(String packageName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL classesURL = classLoader.getResource("");
		
		String rootFile = classesURL.getFile();
		return getClasses(new File(rootFile), null); 
	}

	private List<Class<?>> getClasses(File path, String packageName) {
		ArrayList<Class<?>>classes = new ArrayList<Class<?>>();
		if (path.isDirectory()) {
			addSubdirectoryClasses(path, packageName, classes);
		} else {
			String fileName = path.getName();
			if (fileName.endsWith(".class")) {
				addClass(packageName, classes, fileName);
			}	
		}
		return classes;
	}

	private void addSubdirectoryClasses(File path, String packageName,
			ArrayList<Class<?>> classes) {
		String fullName = buildPackageName(path, packageName);
		
		for (File file : path.listFiles()) {
			List<Class<?>> classesList = getClasses(file, fullName);
			if (classesList != null)
			  classes.addAll(classesList);
		}//for
	}

	private String buildPackageName(File path, String packageName) {
		String fullName;
		if (packageName == null)
			fullName = "";
		else if (packageName.isEmpty())
			fullName = path.getName();
		else 
			fullName = packageName + "." + path.getName();
		return fullName;
	}

	private void addClass(String packageName, ArrayList<Class<?>> classes,
			String fileName) {
		String classPath = packageName + "." + fileName.substring(0, fileName.length() - 6);
		try {
			Class<?> clazz = Class.forName(classPath);
			classes.add(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
