package info.svetlik.pia.service;

import java.util.List;

import org.springframework.data.util.Pair;

import info.svetlik.pia.domain.Commodity;
import info.svetlik.pia.domain.Company;
import info.svetlik.pia.domain.Invoice;

public interface InvoiceManager {
	
	List<Invoice> getInvoices();

	void addInvoice(Company company1, Company company2, List<Pair<Commodity,Integer>> comodities);
	void addInvoice(Company company1, Company company2, Pair<Commodity,Integer> comodity);

	void addInvoice(Invoice invoice);

	void editInvoice(Invoice invoice);

	Invoice getInvoiceByID(Long id);

}
