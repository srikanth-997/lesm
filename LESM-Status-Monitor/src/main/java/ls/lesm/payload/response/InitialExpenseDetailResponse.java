package ls.lesm.payload.response;

import java.time.LocalDate;
//UMER
public interface InitialExpenseDetailResponse {
	
	Integer getEmpId();
	String geLancesofId();
	String getFirstName();
	String getLastName();
	LocalDate getStartDate();
	LocalDate getEndDate();
	Long getEmpTotal();
	Integer getTotalEmpExpId();

}
