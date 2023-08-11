package ls.lesm.payload.request;

import java.util.List;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.model.exp.FinanceApprovalStatus;
import ls.lesm.model.exp.ManagerApprovalStatus;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//UMER
public class ManagerAndFinanceApprovalRequest {
	
    @Nullable
	private List<ManagerApprovalStatus> managerApprovalStatus;
    @Nullable
	private List<FinanceApprovalStatus> financeApprovalStatus;

}
