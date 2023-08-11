package ls.lesm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.OnsiteBillExpenses;
import ls.lesm.model.OnsiteExpensesType;

public interface OnsiteExpensesTypeRepository extends JpaRepository<OnsiteExpensesType, Integer> {

	OnsiteExpensesType findByExpType(String expType);

	OnsiteBillExpenses findByExpId(int expTypeId);

}
