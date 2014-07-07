package pojo;

import multipleInheritance.Controller;
import multipleInheritance.RequestMapping;

@Controller
public class DoServices {

	@RequestMapping("/greet")
	public String greet() {
		return "Hello there";
	}

	@RequestMapping("/cu")
	public String seeYou() {
		return "Bye, Please come back";
	}
}
