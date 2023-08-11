package ls.lesm.repository.expRepo;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.exp.TotalEmpExpenses;

public interface TotalEmpExpensesRepo extends JpaRepository<TotalEmpExpenses, Integer> {

}
