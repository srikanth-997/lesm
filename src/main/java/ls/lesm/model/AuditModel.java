package ls.lesm.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {},
        allowGetters = true
)
public abstract class AuditModel implements Serializable{

	  //  @Temporal(TemporalType.TIMESTAMP)
	    @Column(name = "created_at", nullable = false, updatable = false)
	    //@CreatedDate
	    private LocalDate createdAt;

//	    @Temporal(TemporalType.TIMESTAMP)
//	    @Column(name = "updated_at", nullable=true)
//	    @LastModifiedDate
//	    private LocalDate updatedAt;
	    
	   /* @Getter(value = AccessLevel.NONE)
	    @Setter(value = AccessLevel.NONE)
	    Principal principal;
	   
	    private String createdBy=principal.getName();*/
	    
	   
	   
}
