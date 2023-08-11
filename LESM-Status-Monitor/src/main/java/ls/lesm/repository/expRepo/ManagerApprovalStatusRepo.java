package ls.lesm.repository.expRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.exp.ManagerApprovalStatus;

public interface ManagerApprovalStatusRepo extends JpaRepository<ManagerApprovalStatus, Integer> {

}
