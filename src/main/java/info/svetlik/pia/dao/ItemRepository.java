package info.svetlik.pia.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.svetlik.pia.domain.Invoice;
import info.svetlik.pia.domain.Item;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

}
