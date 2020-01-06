package info.svetlik.pia.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import info.svetlik.pia.dao.InvoiceRepository;
import info.svetlik.pia.dao.ItemRepository;
import info.svetlik.pia.domain.Commodity;
import info.svetlik.pia.domain.Company;
import info.svetlik.pia.domain.Invoice;
import info.svetlik.pia.domain.Item;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvoiceManagerImpl implements InvoiceManager {

	private final InvoiceRepository invoiceRepo;
	private final ItemRepository itemRepo;
	private long orderNumber = 1;

	@Autowired
	public InvoiceManagerImpl(InvoiceRepository invoiceRepo, ItemRepository itemRepo) {
		this.invoiceRepo = invoiceRepo;
		this.itemRepo = itemRepo;
	}

	@EventListener(classes = {
			ContextRefreshedEvent.class
	})
	@Order(1)
	public void setup() {
		Invoice last = this.invoiceRepo.findFirstByOrderByOrderNumberDesc();
		if(last!=null) {
			if (last.getOrderNumber() >= orderNumber) {
				log.info("Invoices in repository found, inicializing order number.");
				orderNumber=last.getOrderNumber()+1;
			}
		}
	}

	@Override
	public List<Invoice> getInvoices() {
		List<Invoice> retVal = new LinkedList<>();
		this.invoiceRepo.findAll().forEach(retVal::add);
		return retVal;
	}

	@Override
	public void addInvoice(Company company1, Company company2, List<Pair<Commodity, Integer>> comodities) {
		Invoice inv = new Invoice(orderNumber++);
		
		inv.setCompanyName1(company1.getName());
		inv.setDIC1(company1.getDIC());
		inv.setLocation1(company1.getLocation());
		inv.setTel1(company1.getTel());
		
		inv.setCompanyName2(company2.getName());
		inv.setDIC2(company2.getDIC());
		inv.setLocation2(company2.getLocation());
		inv.setTel2(company2.getTel());
		
		List<info.svetlik.pia.domain.Item> list = new LinkedList<>();
		for(Pair<Commodity, Integer> comodity : comodities){
			list.add( new Item(comodity.getFirst().getName(), comodity.getFirst().getPriceFull(), comodity.getSecond()));
		}
		inv.setCommodities(list);
		
		calculateFullPrice(inv);
		this.invoiceRepo.save(inv);
	}

	@Override
	public void addInvoice(Company company1, Company company2, Pair<Commodity, Integer> comodity) {
		List<Pair<Commodity, Integer>> list = new LinkedList<>();
		list.add(comodity);
		addInvoice(company1, company2, list);
		
	}
	
	//TODO update all
	
	private static void calculateFullPrice(Invoice inv) {
		double sum =0;
		for(Item item : inv.getCommodities()) {
			sum+= item.getPrice()*item.getQuantity();
		}
		inv.setFullPrice(sum);
	}

	@Override
	public void addInvoice(Invoice invoice) {
		invoice.setOrderNumber(orderNumber++);
		//invoice.setTimeCreated(Date.valueOf(LocalDate.now()));
		
		calculateFullPrice(invoice);
		
		this.invoiceRepo.save(invoice);
		for(Item i : invoice.getCommodities()) {
			i.setInvoice(invoice);
			this.itemRepo.save(i);
		}
	}

	@Override
	public void editInvoice(Invoice invoice) {
		Invoice old = this.invoiceRepo.findById(invoice.getId()).get();
		if(old.isCanceled())invoice.setCanceled(true);
		invoice.setOrderNumber(old.getOrderNumber());
		calculateFullPrice(invoice);
		this.invoiceRepo.save(invoice);
		
		for(Item i : invoice.getCommodities()) {
			i.setInvoice(invoice);
			this.itemRepo.save(i);
		}
	}

	@Override
	public Invoice getInvoiceByID(Long id) {
		return this.invoiceRepo.findById(id).get();
	}

}
