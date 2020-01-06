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
public class Company extends EntityParent {

	@Column(unique = true)
	private String name;
	
	@Column(unique = true)
	private String email;

	private String location;
	private String IC;
	private String DIC;
	private String tel;

	public Company(String email, String name) {
		this.email = email;
		this.name = name;
	}

}
