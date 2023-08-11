
package ls.lesm.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {

	@Id
	@GeneratedValue(generator = "address_gen",strategy = GenerationType.AUTO)
	private Integer addressId;
	
	@Column(length=80)
	private String 	street;
	
	@Column(length=10)
	private String zipCod;
	
	@Column(length=80)
	private String city;
	
	@Column(length=80)
	private String state;
	
	@Column(length=80)
	private String country;
	
	private LocalDate createdAt;// timeStamp;
	
	@Column(length=20)
	private String createdBy;// principal
	
	
 @JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="emp_id_fk")
	private MasterEmployeeDetails masterEmployeeDetails;
	
	@JsonIgnore
	@OneToOne( fetch=FetchType.EAGER)
	@JoinColumn(name="address_type_fk")
	private AddressType adressType;
	
}
