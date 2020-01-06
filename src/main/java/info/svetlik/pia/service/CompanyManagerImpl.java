package info.svetlik.pia.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import info.svetlik.pia.dao.CompanyRepository;
import info.svetlik.pia.domain.Company;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyManagerImpl implements CompanyManager {

	private final CompanyRepository companyRepo;

	@Autowired
	public CompanyManagerImpl(CompanyRepository companyRepo) {
		this.companyRepo = companyRepo;
	}

	@EventListener(classes = {
			ContextRefreshedEvent.class
	})
	@Order(1)
	public void setup() {
	}

	@Override
	public List<Company> getCompanies() {
		List<Company> retVal = new LinkedList<>();
		this.companyRepo.findAll().forEach(retVal::add);
		return retVal;
	}


	@Override
	public void addCompany(String name, String mail) {
		Company com = new Company(name, mail);//TODO check name and mail unique
		this.companyRepo.save(com);
	}

	@Override
	public void addCompany(String name, String mail, String location, String IC, String DIC, String tel) {
		Company com = new Company(name, mail);
		com.setLocation(location);
		com.setIC(IC);
		com.setDIC(DIC);
		com.setTel(tel);
		this.companyRepo.save(com);
	}

	@Override
	public Company getCompanyByID(Long id) {
		return companyRepo.findById(id).get();
	}

	@Override
	public void removeCompany(Company company) {
		this.companyRepo.deleteById(company.getId());
		
	}

	@Override
	public void editCompany(Company company) {
		if(company.getName().equals("")) {
			throw new IllegalArgumentException("Name cannot be empty!");
		}
		if(company.getEmail().equals("")) {
			throw new IllegalArgumentException("E-mail cannot be empty!");
		}
		
		Company old = this.companyRepo.findById(company.getId()).get();
		if (this.companyRepo.findByName(company.getName()) != null && !old.getName().equals(company.getName())) {
			throw new IllegalArgumentException("There is already company with same name!");
		}
		if (this.companyRepo.findByEmail(company.getEmail()) != null && !old.getEmail().equals(company.getEmail())) {
			throw new IllegalArgumentException("There is already company with same e-mail!");
		}

		this.companyRepo.save(company);
		
	}

	@Override
	public void addCompany(Company company) {
		if(company.getName().equals("")) {
			throw new IllegalArgumentException("Name cannot be empty!");
		}
		if(company.getEmail().equals("")) {
			throw new IllegalArgumentException("E-mail cannot be empty!");
		}
		
		if (this.companyRepo.findByName(company.getName()) != null ) {
			throw new IllegalArgumentException("There is already company with same name!");
		}
		if (this.companyRepo.findByEmail(company.getEmail()) != null) {
			throw new IllegalArgumentException("There is already company with same e-mail!");
		}
		

		this.companyRepo.save(company);
		
	}
	

}
