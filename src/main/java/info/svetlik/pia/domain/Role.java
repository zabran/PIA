package info.svetlik.pia.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Role extends EntityParent {

	@Column(unique = true)
	private String code;

	private String name;

	//@Fetch(FetchMode.SUBSELECT)
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private List<User> users;

	public Role(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
	
}
