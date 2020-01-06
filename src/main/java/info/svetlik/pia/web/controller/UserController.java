package info.svetlik.pia.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import info.svetlik.pia.domain.User;
import info.svetlik.pia.service.RoleManager;
import info.svetlik.pia.service.UserManager;

@Controller
public class UserController {

	@Autowired
	UserManager users;
	@Autowired
	RoleManager roles;
	
	@Secured("ADMIN") 
	@GetMapping("/createuser")
	  public String createUserForm(Model model) {
	    model.addAttribute("user", new User());
	    model.addAttribute("allRoles", roles.getRoles());
	    return "createuser";
	  }
	
	@Secured("ADMIN") 
	@PostMapping("/createuser")
	  public String createUserSubmit(@ModelAttribute User user, Model model, @RequestParam String password2) {
		if(!password2.equals(user.getPassword())) {
			model.addAttribute("user", user);
			model.addAttribute("allRoles", roles.getRoles());
			model.addAttribute("error", "Passwords not equal!");
			return "createuser";
		}
		try {
		users.addUser(user);
		} catch (Exception e) {
			model.addAttribute("user", user);
			model.addAttribute("allRoles", roles.getRoles());
			model.addAttribute("error", e.getMessage());
			return "createuser";
		}
		model.addAttribute("users", users.getUsers());
	    return "users";
	  }
	
	@Secured("ADMIN") 
	@RequestMapping(value="/edituser", method=RequestMethod.POST, params={"edit", "!change"})
	  public String editUser(@ModelAttribute User user, Model model, @RequestParam(defaultValue = "false") Boolean delete, 
			  @RequestParam String password2 , Authentication authentication) {	
		if(delete) {
			if(!authentication.getName().contentEquals(user.getUsername())) users.removeUser(user);
			else {
				model.addAttribute("error", "You cannot remove yourself!");
				model.addAttribute("user", user);
			    model.addAttribute("allRoles", roles.getRoles());
				return "user";
			}
		}
		else { 
			if(!password2.equals(user.getPassword())) {
				model.addAttribute("user", user);
				model.addAttribute("allRoles", roles.getRoles());
				model.addAttribute("error", "Passwords not equal!");
				return "user";
			}
			try {
			users.editUser(user);
			} catch (Exception e) {
				model.addAttribute("user", user);
				model.addAttribute("allRoles", roles.getRoles());
				model.addAttribute("error", e.getMessage());
				return "user";
			}
			}

		model.addAttribute("users", users.getUsers());
	    return "users";
	  }
	
	@RequestMapping(value="/edituser", method=RequestMethod.POST, params={"!edit", "change"})
	  public String changePassword(@ModelAttribute User user, Model model, Authentication authentication, 
			  @RequestParam String password2, @RequestParam String passwordOld, @RequestParam Long id, 
			  @RequestParam String usernameOld) {	
		model.addAttribute("error", "");

		if(!authentication.getName().equals(usernameOld)) {
			model.addAttribute("error", "You are "+authentication.getName()+" and cannot change other user's passwords.");
		}
		else if(!password2.equals(user.getPassword())) {
			model.addAttribute("error", "Password not equal!");
		}
		else {	
			try {
				user.setUsername(usernameOld);
				users.changePassword(user, passwordOld);
			} catch (Exception e) {
				model.addAttribute("error", e.getMessage());
			}
		}
		if(model.getAttribute("error").equals("")) model.addAttribute("suc", "Password changed.");
		model.addAttribute("user", users.getUser(id));
		model.addAttribute("allRoles", roles.getRoles());
	    return "user";
	  }
	
	@Secured("ADMIN") 
	@PostMapping("/user")
	public String getUser(Model model, @RequestParam Long id) {
	    model.addAttribute("user", users.getUser(id));
	    model.addAttribute("allRoles", roles.getRoles());
	    return "user";
	}
	
	@GetMapping("/changepassword")
	public String getChangePassword(Model model, Authentication authentication) {
	    model.addAttribute("user", users.getUser(authentication.getName()));
	    model.addAttribute("allRoles", roles.getRoles());
	    return "user";
	}

	
	@GetMapping("/users")
	public String getUsers(Model model) {
	    model.addAttribute("users", users.getUsers());
	    return "users";
	}

}
