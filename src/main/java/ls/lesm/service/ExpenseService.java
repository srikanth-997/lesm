package ls.lesm.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ls.lesm.model.Salary;
import ls.lesm.payload.request.ApprovalStatusRequest;
import ls.lesm.payload.request.ExpensesRequest;
import ls.lesm.payload.response.AllExpensesResponse;
import ls.lesm.payload.response.InitialExpenseDetailResponse;

public interface ExpenseService {
	
//	OnsiteBillExpenses insertBillExp(OnsiteBillExpenses billExp, Principal principal, int expTypeId, String empId);
//	
//	InternalExpenses insertIntExp(InternalExpenses intExp, Principal principal, String empId);
//	//UMER
	Salary inserSal(Salary sal, Principal principal, Integer empId);
	//UMER
    ExpensesRequest insertExpenses(Principal principal, ExpensesRequest expensesResponse);
  //UMER
    List<ApprovalStatusRequest> approveFoodExpByManager(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<ApprovalStatusRequest> approveFoodExpByFinance(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<ApprovalStatusRequest>  approveTravelExpByManager(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<ApprovalStatusRequest> approveTravelExpByFinance(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<ApprovalStatusRequest> approveAccomodationExpByManager(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<ApprovalStatusRequest> approveAccomodationExpByFinance(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    ApprovalStatusRequest approveAllowanceExpByManager(Principal principal, ApprovalStatusRequest req);
  //UMER
    ApprovalStatusRequest approveAllowanceExpByFinance(Principal principal, ApprovalStatusRequest req);
  //UMER
    List<ApprovalStatusRequest> approveOtherExpByManager(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<ApprovalStatusRequest> approveOtherExpByFinance(Principal principal, List<ApprovalStatusRequest> req);
  //UMER
    List<InitialExpenseDetailResponse> getCardDetails(Principal principal);
  //UMER
    AllExpensesResponse getAllExpByTotalId(Integer totalId );
  //UMER
    String uploadFile(MultipartFile file);


}
