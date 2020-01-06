package info.svetlik.pia.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController implements ErrorController{

	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@Override
    public String getErrorPath() {
        return "/error";
    }
    
    @RequestMapping(value = "/error")
    public String error() {
        return "error";
    }

}
