package pojo;

import multipleInheritance.Controller;
import multipleInheritance.RequestMapping;

@Controller
public class MoreServices {

	@RequestMapping("/again")
	public String greetAgain() {
		return "Hello there AGAIN";
	}

	@RequestMapping("/more")
	public String doMore() {
		return "Please do MORE";
	}
}
