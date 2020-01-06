package info.svetlik.pia.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.repository.cdi.Eager;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Invoice extends EntityParent {

	@Column(unique = true)
	private long orderNumber;
	private boolean received = true;
	private boolean canceled = false;
	
	//@Temporal(TemporalType.DATE)
	//@DateTimeFormat (pattern="dd.MM.YYYY")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date timeCreated;// = Date.valueOf(LocalDate.now());
	
	//@Temporal(TemporalType.DATE)
	//@DateTimeFormat (pattern="dd.MM.YYYY")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date timeCompleted;
	private String content;
	private double fullPrice;
	
	//company1
	private String location1;
	private String DIC1;
	private String tel1;
	private String companyName1;
	
	//company1
	private String location2;
	private String DIC2;
	private String tel2;
	private String companyName2;
	
	//commodity
	//@ElementCollection(fetch = FetchType.EAGER)
	//@OneToMany(/*fetch = FetchType.EAGER, */cascade = CascadeType.ALL, orphanRemoval = true)
	//@JoinColumn(name = "invoice_id")
	@OneToMany(
	        mappedBy = "invoice",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	public List<Item> Commodities = new ArrayList<>();
	//private String commodityName;
	//private double price;
	//private int quantity;

	public Invoice(long orderNumber) {
		this.orderNumber = orderNumber;
		this.timeCreated = java.sql.Date.valueOf(LocalDate.now());
	}

	public void setCompanies(Company company1, Company company2) {
		this.location1=company1.getLocation();
		this.DIC1=company1.getDIC();
		this.tel1=company1.getTel();
		this.companyName1=company1.getName();
		
		this.location2=company2.getLocation();
		this.DIC2=company2.getDIC();
		this.tel2=company2.getTel();
		this.companyName2=company2.getName();
		
	}

	

}

/*@Embeddable
//@NoArgsConstructor
//@Entity
class Item{
	private String commodityName;
	private double price;
	private int quantity;
	
	public Item(String name, double price, int quantity) {
		this.commodityName=name;
		this.price=price;
		this.quantity=quantity;
	}
	
	public String getCommodityName() {return this.commodityName;}
	public double getPrice() {return this.price;}
	public int getQuantity() {return this.quantity;}
}*/
