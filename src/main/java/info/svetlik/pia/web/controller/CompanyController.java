package info.svetlik.pia.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import info.svetlik.pia.domain.Company;
import info.svetlik.pia.service.CompanyManager;

@Controller
public class CompanyController {

	@Autowired
	CompanyManager companies;
	
	@Secured("SECRETARY") 
	@GetMapping("/createcontact")
	  public String createCompanyForm(Model model) {
	    model.addAttribute("company", new Company());
	    return "createcompany";
	  }
	
	@Secured("SECRETARY") 
	@PostMapping("/createcompany")
	  public String createCompany(@ModelAttribute Company company, Model model) {

		try {
		companies.addCompany(company);
		} catch (Exception e) {
			model.addAttribute("company", company);
			model.addAttribute("error", e.getMessage());
			return "createcontact";
		}
		model.addAttribute("companies", companies.getCompanies());
	    return "contacts";
	  }
	
	@Secured("SECRETARY") 
	@PostMapping("/editcontact")
	  public String ediContact(@ModelAttribute Company company, Model model, 
			  @RequestParam(defaultValue = "false") Boolean delete) {
		if(delete) {
			 companies.removeCompany(company);
		}
		else { 
			try {
			companies.editCompany(company);
			} catch (Exception e) {
				model.addAttribute("company", company);
				model.addAttribute("error", e.getMessage());
				return "company";
			}
		}
		model.addAttribute("companies", companies.getCompanies());
	    return "contacts";
	  }
	
	
	@PostMapping("/company")
	public String getCompany(Model model, @RequestParam Long id) {
	    model.addAttribute("company", companies.getCompanyByID(id));
	    return "company";
	}

	
	@GetMapping("/contacts")
	public String getContacts(Model model) {
	    model.addAttribute("companies", companies.getCompanies());
	    return "contacts";
	}

}
