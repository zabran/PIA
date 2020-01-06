package info.svetlik.pia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Commodity extends EntityParent {

	@Column(unique = true)
	private String name;
	
	private double priceFull;
	private double priceReducted;

	public Commodity(String name, double fullPrice, double reductedPrice) {
		this.name = name;
		this.priceFull = fullPrice;
		this.priceReducted = reductedPrice;
	}

}
