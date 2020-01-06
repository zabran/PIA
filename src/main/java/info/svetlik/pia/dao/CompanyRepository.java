package info.svetlik.pia.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.svetlik.pia.domain.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

	Company findByName(String name);

	Company findByEmail(String email);
}
