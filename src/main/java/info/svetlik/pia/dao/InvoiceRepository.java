package info.svetlik.pia.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.svetlik.pia.domain.Invoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

	Invoice findByOrderNumber(long orderNumber);
	
	Invoice findFirstByOrderByOrderNumberDesc();

}
