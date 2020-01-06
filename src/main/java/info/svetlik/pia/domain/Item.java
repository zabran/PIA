package info.svetlik.pia.domain;

import java.sql.Time;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@Embeddable
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Item extends EntityParent{
	private String commodityName;
	private double price;
	private int quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Invoice invoice;
	
	public Item(String name, double price, int quantity) {
		this.commodityName=name;
		this.price=price;
		this.quantity=quantity;
	}
	
	//public String getCommodityName() {return this.commodityName;}
	//public double getPrice() {return this.price;}
	//public int getQuantity() {return this.quantity;}
}
