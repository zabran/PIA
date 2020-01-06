package info.svetlik.pia.service;

import java.util.List;

import info.svetlik.pia.domain.Company;

public interface CompanyManager {
	
	List<Company> getCompanies();

	void addCompany(String name, String mail);
	void addCompany(String name, String mail, String location,
	String IC,
	String DIC,
	String tel);

	Company getCompanyByID(Long id);

	void removeCompany(Company company);

	void editCompany(Company company);

	void addCompany(Company company);

}
