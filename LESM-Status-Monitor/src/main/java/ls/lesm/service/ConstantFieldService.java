package ls.lesm.service;

import java.security.Principal;
import java.util.List;

import ls.lesm.model.AddressType;
import ls.lesm.model.Clients;
import ls.lesm.model.Departments;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.OnsiteExpensesType;
import ls.lesm.model.SubDepartments;

public interface ConstantFieldService {
	//UMER
	Designations insertDesg(Designations desig ,Principal Principal,Integer id);
	//UMER
	Departments insertDepart(Departments depart, Principal Principal );
	//UMER
	SubDepartments insertSubDepart(SubDepartments subDepart, Principal principal, int departId);
	//UMER
	Clients insertClient(Clients clients ,Principal principal);
	//UMER
	EmployeeType insertEmpTypes(EmployeeType emoType);
	//UMER
	OnsiteExpensesType insertExpType(OnsiteExpensesType expType, Principal principal);
	//UMER
	AddressType insertAddressTyp(AddressType addType);
	//UMER
	List<SubDepartments>getAllSubDepartments();
	//UMER
	List<Departments> getAllDepartments();
	//UMER
	List<AddressType> getAllAddType();
	
}
