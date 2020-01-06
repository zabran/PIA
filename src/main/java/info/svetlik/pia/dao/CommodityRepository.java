package info.svetlik.pia.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import info.svetlik.pia.domain.Commodity;

@Repository
public interface CommodityRepository extends CrudRepository<Commodity, Long> {

	Commodity findByName(String name);

}
