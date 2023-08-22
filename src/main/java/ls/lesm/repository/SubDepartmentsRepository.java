package ls.lesm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.Departments;
import ls.lesm.model.SubDepartments;

public interface SubDepartmentsRepository extends JpaRepository<SubDepartments, Integer> {

	

	SubDepartments findBySubDepartmentNames(String subDepartmentNames);

}
