package ls.lesm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.EmployeeResponsibilities;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.enums.ResponsibilitiesTypes;

public interface EmployeeResponsibilitiesRepository extends JpaRepository<EmployeeResponsibilities, Integer> {

	List<EmployeeResponsibilities> findByMasterEmployeeDetails(MasterEmployeeDetails employee);

	List<EmployeeResponsibilities> findByResponsibilitiesTypes(ResponsibilitiesTypes towerHead);

}
