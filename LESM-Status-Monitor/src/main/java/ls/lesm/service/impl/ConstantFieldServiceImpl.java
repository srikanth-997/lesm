package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.DuplicateEntryException;
import ls.lesm.model.AddressType;
import ls.lesm.model.Clients;
import ls.lesm.model.Departments;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeType;
import ls.lesm.model.OnsiteExpensesType;
import ls.lesm.model.SubDepartments;
import ls.lesm.repository.AddressTypeRepository;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.OnsiteExpensesTypeRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.service.ConstantFieldService;

@Service
public class ConstantFieldServiceImpl implements ConstantFieldService {

	@Autowired
	private DesignationsRepository designationsRepository;

	@Autowired
	private DepartmentsRepository departmentsRepository;

	@Autowired
	private SubDepartmentsRepository subDepartmentsRepository;

	@Autowired
	private ClientsRepository clientsRepository;

	@Autowired
	private EmployeeTypeRepository employeeTypeRepository;

	@Autowired
	private OnsiteExpensesTypeRepository onsiteExpensesTypeRepository;

	@Autowired
	private AddressTypeRepository addressTypeRepository;

	// UMER
	@Override
	public Designations insertDesg(Designations desig, Principal principal, Integer id) {
		desig.setCreatedBy(principal.getName());
		desig.setCreatedAt(new Date());

		this.designationsRepository.findById(id).map(idd -> {
			desig.setDesignations(idd);
			return idd;
		});
		return this.designationsRepository.save(desig);
	}

	// UMER
	@Override
	public Departments insertDepart(Departments depart, Principal principal) {
		depart.setCreatedAt(new Date());
		depart.setCreatedBy(principal.getName());

		return depart;
	}

	// UMER
	@Override
	public SubDepartments insertSubDepart(SubDepartments subDepart, Principal principal, int departId) {

		subDepart.setCreatedAt(LocalDate.now());
		subDepart.setCreatedBy(principal.getName());
		// subDepart.setDepartments(new Departments(departId,"",null,""));// assigning
		// foreign key

		return subDepart;
	}

	// UMER
	@Override
	public Clients insertClient(Clients clients, Principal principal) {

		clients.setCreatedAt(new Date());
		clients.setCreatedBy(principal.getName());

		return clients;
	}

	// UMER
	@Override
	public EmployeeType insertEmpTypes(EmployeeType empType) {
		EmployeeType local = employeeTypeRepository.findByTypeName(empType.getTypeName());
		if (local != null)
			throw new DuplicateEntryException(
					"This Employee Type with this name '" + local.getTypeName() + "' Already exist in database");
		else
			this.employeeTypeRepository.save(empType);
		return empType;
	}

	// UMER
	@Override
	public OnsiteExpensesType insertExpType(OnsiteExpensesType expType, Principal principal) {
		expType.setCreatedAt(new Date());
		expType.setCreatedBy(principal.getName());
		OnsiteExpensesType local = onsiteExpensesTypeRepository.findByExpType(expType.getExpType());
		if (local != null)
			throw new DuplicateEntryException(
					"This Expense Type with this name '" + local.getExpType() + "' Already exist in database");
		else
			this.onsiteExpensesTypeRepository.save(expType);
		return expType;
	}

	// UMER
	@Override
	public List<SubDepartments> getAllSubDepartments() {

		List<SubDepartments> all = this.subDepartmentsRepository.findAll();
		return all;
	}

	// UMER
	@Override
	public List<Departments> getAllDepartments() {
		return this.departmentsRepository.findAll();

	}

	// UMER
	@Override
	public AddressType insertAddressTyp(AddressType addType) {
		AddressType local = this.addressTypeRepository.findByAddType(addType.getAddType());
		if (local != null)
			throw new DuplicateEntryException(
					"This Address Type  Already exist in database");
		else
			this.addressTypeRepository.save(addType);
		return addType;
	}

	// UMER
	@Override
	public List<AddressType> getAllAddType() {

		return this.addressTypeRepository.findAll();
	}
	
	

}
