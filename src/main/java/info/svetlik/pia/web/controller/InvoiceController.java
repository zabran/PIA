package info.svetlik.pia.web.controller;

import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import info.svetlik.pia.domain.Commodity;
import info.svetlik.pia.domain.Company;
import info.svetlik.pia.domain.Invoice;
import info.svetlik.pia.domain.Item;
import info.svetlik.pia.service.CommodityManager;
import info.svetlik.pia.service.CompanyManager;
import info.svetlik.pia.service.InvoiceManager;

@Controller
public class InvoiceController {

	@Autowired
	InvoiceManager invoices;
	@Autowired
	CommodityManager commodities;
	@Autowired
	CompanyManager companies;
	
	/*----------Vytváření invoice----------------*/
	
	/**Vrací forulář pro vytváření nové invoice.*/
	@Secured("ACCOUNTANT") 
	@GetMapping("/createinvoice")
	  public String createInvoiceForm(Model model) {
		Invoice i=new Invoice();
		i.setContent("Current VAT: "+commodities.getVAT()+"%.");
		i.setTimeCreated(Date.valueOf(LocalDate.now()));
		i.setTimeCompleted(Date.valueOf(LocalDate.now()));
		updateModel(model, i);
	    return "createinvoice";
	  }
	
	@Secured("ACCOUNTANT") 
	@RequestMapping(value="/createinvoice", method=RequestMethod.POST, params={"!addRow", "!removeRow", "create"})
	  public String createInvoice(@ModelAttribute Invoice invoice, Model model, @RequestParam Company company1, 
			  @RequestParam Company company2) {
		invoice.setCompanies(company1, company2);
		try {
		invoices.addInvoice(invoice);
		} catch (Exception e) {
			updateModel(model, invoice);
			model.addAttribute("error", e.getMessage());
			return "createinvoice";
		}
		model.addAttribute("invoices", invoices.getInvoices());
	    return "invoices";
	  }
	
	@Secured("ACCOUNTANT") 
	@RequestMapping(value="/createinvoice", method=RequestMethod.POST, params={"addRow", "!removeRow", "!create"})
	  public String createInvoiceAddRow(@ModelAttribute Invoice invoice, Model model, @RequestParam Commodity selected) {
		addRow(invoice, selected);
		updateModel(model, invoice);
		return "createinvoice";
	  }
	
	@Secured("ACCOUNTANT") 
	@RequestMapping(value="/createinvoice", method=RequestMethod.POST, params={"!addRow", "removeRow", "!create"})
	  public String createInvoiceRemoveRow(@ModelAttribute Invoice invoice, Model model, final HttpServletRequest req) {
		final Integer id = Integer.valueOf(req.getParameter("removeRow"));
		removeRow(invoice, id.intValue());
		updateModel(model, invoice);
		return "createinvoice";
	  }
	
	/*----------------Editace invoice--------------------*/
	
	@Secured("ACCOUNTANT") 
	@RequestMapping(value="/editinvoice", method=RequestMethod.POST, params={"!addRow", "!removeRow", "edit"})
	  public String editInvoice(@ModelAttribute Invoice invoice, Model model, @RequestParam Company company1, 
			  @RequestParam Company company2) {
		invoice.setCompanies(company1, company2);
			try {
			invoices.editInvoice(invoice);
			} catch (Exception e) {
				updateModel(model, invoice);
				model.addAttribute("error", e.getMessage());
				return "invoice";
			}
		model.addAttribute("invoices", invoices.getInvoices());
	    return "invoices";
	  }
	
	@Secured("ACCOUNTANT") 
	@RequestMapping(value="/editinvoice", method=RequestMethod.POST, params={"addRow", "!removeRow", "!edit"})
	  public String editInvoiceAddRow(@ModelAttribute Invoice invoice, Model model, @RequestParam Company company1, 
			  @RequestParam Company company2, @RequestParam Commodity selected) {
		invoice.setCompanies(company1, company2);
		addRow(invoice, selected);
		updateModel(model, invoice);
		return "invoice";
	  }
	
	@Secured("ACCOUNTANT") 
	@RequestMapping(value="/editinvoice", method=RequestMethod.POST, params={"!addRow", "removeRow", "!edit"})
	  public String editInvoiceRemoveRow(@ModelAttribute Invoice invoice, Model model, @RequestParam Company company1, 
			  @RequestParam Company company2, final HttpServletRequest req) {
		invoice.setCompanies(company1, company2);
		final Integer id = Integer.valueOf(req.getParameter("removeRow"));
		removeRow(invoice, id.intValue());
		updateModel(model, invoice);
		return "invoice";
	  }
	
	/**Vrací invoice ze seznamu pro nahlédnutí, nebo editaci.*/
	@PostMapping("/invoice")
	public String getInvoice(Model model, @RequestParam Long id) {
		Invoice invoice = invoices.getInvoiceByID(id);
		updateModel(model, invoice);
	    return "invoice";
	}
	
	/**Pomocná metoda, která updatuje model o potřebné atributy.
	 * */
	public void updateModel(Model model, Invoice invoice) {
		model.addAttribute("invoice", invoice);
	    model.addAttribute("commodities", commodities.getCommodities());
	    model.addAttribute("companies", companies.getCompanies());
	}

	/**Vrací seznam všech invoices k prohlédnutí a výběru.*/
	@GetMapping("/invoices")
	public String getInvoices(Model model) {
	    model.addAttribute("invoices", invoices.getInvoices());
	    return "invoices";
	}
	
	/**Pomocná metoda, která přidá do invoice v pohledu nový řádek dle commodity.*/
	private Invoice addRow(Invoice invoice, Commodity commodity) {
		Item i= new Item(commodity.getName(), commodity.getPriceFull(), 1);
		i.setInvoice(invoice);
		invoice.Commodities.add(i);
		return invoice;
	}
	
	/**Pomocná metoda, která odebere od invoice v pohledu řádek s indexem i.*/
	private Invoice removeRow(Invoice invoice, int i) {
		invoice.Commodities.remove(i);
		return invoice;
	}

}
