package ls.lesm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.EmployeeType;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Integer> {

	EmployeeType findByTypeName(String typeName);

}
