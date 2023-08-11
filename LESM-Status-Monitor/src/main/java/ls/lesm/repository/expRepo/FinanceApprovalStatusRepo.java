package ls.lesm.repository.expRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.exp.FinanceApprovalStatus;

public interface FinanceApprovalStatusRepo extends JpaRepository<FinanceApprovalStatus, Integer> {

}
