
package ls.lesm.restcontroller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.exception.SupervisorAlreadyExistException;
import ls.lesm.model.Address;
import ls.lesm.model.AtClientAllowances;
import ls.lesm.model.Attachment;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeePhoto;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.History;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.ReleaseEmpDetails;
import ls.lesm.model.Role;
import ls.lesm.model.Salary;
import ls.lesm.model.SecondaryManager;
import ls.lesm.model.Sub_Profit;
import ls.lesm.model.User;
import ls.lesm.model.UserRole;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.RoleRepository;
import ls.lesm.service.impl.AdminServiceImpl;
import ls.lesm.service.impl.DeleteEmployeeService;
import ls.lesm.service.impl.UserServiceImpl;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RoleRepository roleRopository;

	@Autowired
	private AdminServiceImpl adminServiceImpl;
	@Autowired
	private DeleteEmployeeService deleteEmployeeService;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private ReleaseEmpDetailsRepository releaseEmpDetailsRepository;

	@Autowired
	DeleteEmployeeService deleteEmp;

	// create User
	// UMER
	@PostMapping("/sign-up")
	public User createUser(@RequestBody @Valid User user, @RequestParam String roleName) throws Exception {
		LOG.info("Enterd into createUser Method");

		MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findByLancesoft(user.getUsername());

		try {
			if (employee.getDesignations().getDesgNames().equalsIgnoreCase("Consultant")) {
				throw new SupervisorAlreadyExistException("Consultant not allowed to be credentialled");
			}
			user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

			user.setUsername(user.getUsername().toUpperCase());
			user.setEmail(employee.getEmail());
			user.setPhoneNo(employee.getPhoneNo());
			user.setFirstName(employee.getFirstName());
			user.setLastName(employee.getLastName());
			LOG.debug("Encrypted password");
		} catch (NullPointerException npe) {

		}
		Set<UserRole> userRoleSet = new HashSet<>();

		Role role = new Role(); // default role "User"

		role.setRoleName(roleName);
		Role role1 = this.roleRopository.findByRoleName(roleName);
		role.setRoleId(role1.getRoleId());

		user.setRoleName(role.getRoleName());

		UserRole userRole = new UserRole();
		userRole.setRole(role);
		userRole.setUser(user);
		userRoleSet.add(userRole);
		return this.userService.createUser(user, userRoleSet);
	}

	// UMER
	@PostMapping("/create-roles")
	public ResponseEntity<?> createRoles(@RequestBody Role role) {

		this.adminServiceImpl.createNewRole(role);
		return new ResponseEntity<Role>(HttpStatus.ACCEPTED);
	}

	// UMER
	@DeleteMapping("/delete-roles/{roleName}")
	public ResponseEntity<?> deleteRole(@PathVariable String roleName, Role role) {
		this.adminServiceImpl.deleteRoles(roleName);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	// UMER
	@GetMapping("/all-roles")
	public ResponseEntity<List<Role>> allRoles() {
		List<Role> allroles = this.roleRopository.findAll();
		return new ResponseEntity<List<Role>>(allroles, HttpStatus.ACCEPTED);

	}

	// Teju
	@GetMapping("/ShowDesinationsForDeleteModule")
	public List<Designations> getDesignationsForDeleteModule()

	{

		return deleteEmployeeService.getDesignationsForDelete();
	}

	// Teju
	@GetMapping("/ShowEmployeesToDelete/{desg_id}")
	public List<MasterEmployeeDetails> getEmployeesToDelete(@PathVariable int desg_id)

	{

		return deleteEmployeeService.getEmployeesToDelete(desg_id);
	}

	@DeleteMapping("/DeleteEmployee")
	@Transactional
	public ResponseEntity<?> temp(@RequestParam Integer empId, @RequestParam boolean flag) {

		Optional<MasterEmployeeDetails> emp = this.masterEmployeeDetailsRepository.findById(empId);

		if (emp.isPresent()) {

			List<MasterEmployeeDetails> supEmp = this.masterEmployeeDetailsRepository.findBySupervisor(emp.get());

			flag = deleteEmp.employeesUnderMeInMasterTable(emp.get(), flag);

//				  for(MasterEmployeeDetails m: supEmp) {
//					  m.setSupervisor(null);
//					  this.masterEmployeeDetailsRepository.save(m);
//				  }

			if (flag) {

				List<ReleaseEmpDetails> rEmp = this.releaseEmpDetailsRepository
						.findsBymasterEmployeeDetails_Id(emp.get().getEmpId());
				if (!rEmp.isEmpty()) {
					for (ReleaseEmpDetails r : rEmp) {
						r.setMasterEmployeeDetailssupervisor(null);
						this.releaseEmpDetailsRepository.save(r);
					}
				}
				Optional<ReleaseEmpDetails> OpREmp = this.releaseEmpDetailsRepository
						.findByMasterEmployeeDetailsId(emp.get());
				if (OpREmp.isPresent()) {
					OpREmp.get().setMasterEmployeeDetailsId(null);
					this.releaseEmpDetailsRepository.save(OpREmp.get());
				}

				List<History> history = this.historyRepository.findsBymasterEmployeeDetails_Id(emp.get().getEmpId());
				List<History> updateByH = this.historyRepository.findByUpdatedBy(emp.get());
				for (History h : updateByH) {
					h.setUpdatedBy(null);
					// h.setSupervisor(null);
					this.historyRepository.save(h);
				}
				for (History h : history) {
					h.setSupervisor(null);
					// h.setUpdatedBy(null);
					this.historyRepository.save(h);
				}
				emp.get().setEmployeePhoto(null);
				emp.get().setDepartments(null);
				emp.get().setSubDepartments(null);
				emp.get().setEmployeeType(null);
				emp.get().setDesignations(null);
				emp.get().setSupervisor(null);

				this.masterEmployeeDetailsRepository.save(emp.get());

				CriteriaBuilder builder = entityManager.getCriteriaBuilder();

				CriteriaDelete<SecondaryManager> secondaryManagerDelete2 = builder
						.createCriteriaDelete(SecondaryManager.class);
				Root<SecondaryManager> secondaryManagerRoot2 = secondaryManagerDelete2.from(SecondaryManager.class);
				secondaryManagerDelete2.where(builder.equal(secondaryManagerRoot2.get("employee"), empId));
				entityManager.createQuery(secondaryManagerDelete2).executeUpdate();

				CriteriaDelete<SecondaryManager> secondaryManagerDelete = builder
						.createCriteriaDelete(SecondaryManager.class);
				Root<SecondaryManager> secondaryManagerRoot = secondaryManagerDelete.from(SecondaryManager.class);
				secondaryManagerDelete.where(builder.equal(secondaryManagerRoot.get("secondaryManager"), empId));
				entityManager.createQuery(secondaryManagerDelete).executeUpdate();

				CriteriaDelete<InternalExpenses> internalExpensesDelete = builder
						.createCriteriaDelete(InternalExpenses.class);
				Root<InternalExpenses> internalExpensesRoot = internalExpensesDelete.from(InternalExpenses.class);
				internalExpensesDelete.where(builder.equal(internalExpensesRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(internalExpensesDelete).executeUpdate();

				CriteriaDelete<Salary> salaryDelete = builder.createCriteriaDelete(Salary.class);
				Root<Salary> salaryRoot = salaryDelete.from(Salary.class);
				salaryDelete.where(builder.equal(salaryRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(salaryDelete).executeUpdate();

				CriteriaDelete<Sub_Profit> subDelete = builder.createCriteriaDelete(Sub_Profit.class);
				Root<Sub_Profit> subRoot = subDelete.from(Sub_Profit.class);
				subDelete.where(builder.equal(subRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(subDelete).executeUpdate();

				CriteriaDelete<Address> addressDelete = builder.createCriteriaDelete(Address.class);
				Root<Address> addressRoot = addressDelete.from(Address.class);
				addressDelete.where(builder.equal(addressRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(addressDelete).executeUpdate();

				CriteriaDelete<EmployeesAtClientsDetails> employeesAtClientsDetailsDelete = builder
						.createCriteriaDelete(EmployeesAtClientsDetails.class);
				Root<EmployeesAtClientsDetails> employeesAtClientsDetailsRoot = employeesAtClientsDetailsDelete
						.from(EmployeesAtClientsDetails.class);
				employeesAtClientsDetailsDelete
						.where(builder.equal(employeesAtClientsDetailsRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(employeesAtClientsDetailsDelete).executeUpdate();

				CriteriaDelete<Attachment> attachmentDelete = builder.createCriteriaDelete(Attachment.class);
				Root<Attachment> attachmentRoot = attachmentDelete.from(Attachment.class);
				attachmentDelete.where(builder.equal(attachmentRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(attachmentDelete).executeUpdate();

				CriteriaDelete<ReleaseEmpDetails> releaseEmpDetailsDelete = builder
						.createCriteriaDelete(ReleaseEmpDetails.class);
				Root<ReleaseEmpDetails> releaseEmpDetailsRoot = releaseEmpDetailsDelete.from(ReleaseEmpDetails.class);
				releaseEmpDetailsDelete
						.where(builder.equal(releaseEmpDetailsRoot.get("masterEmployeeDetailsId"), empId));
				entityManager.createQuery(releaseEmpDetailsDelete).executeUpdate();
				
				CriteriaDelete<AtClientAllowances> atClientAllowances = builder.createCriteriaDelete(AtClientAllowances.class);
				Root<AtClientAllowances> atClientAllowancesRoot = atClientAllowances.from(AtClientAllowances.class);
				atClientAllowances.where(builder.equal(atClientAllowancesRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(atClientAllowances).executeUpdate();

				CriteriaDelete<EmployeePhoto> employeePhotoDelete = builder.createCriteriaDelete(EmployeePhoto.class);
				Root<EmployeePhoto> employeePhotoRoot = employeePhotoDelete.from(EmployeePhoto.class);
				employeePhotoDelete.where(builder.equal(employeePhotoRoot.get("masterEmployeeDetails"), empId));
				entityManager.createQuery(employeePhotoDelete).executeUpdate();
				
				

				CriteriaDelete<MasterEmployeeDetails> masterEmpDetailsDelete = builder
						.createCriteriaDelete(MasterEmployeeDetails.class);
				Root<MasterEmployeeDetails> masterEmpDetailsRoot = masterEmpDetailsDelete
						.from(MasterEmployeeDetails.class);
				masterEmpDetailsDelete.where(builder.equal(masterEmpDetailsRoot.get("empId"), empId));
				entityManager.createQuery(masterEmpDetailsDelete).executeUpdate();

				deleteEmp.userCredentials(emp.get());

			}

			else {
				throw new RecordNotFoundException(
						"Assign people under employee to his/her supervisor then DELETE the Employee");
			}

		}
		
		else
		{
			return new ResponseEntity<>("EmpId is not found", HttpStatus.OK);
			
		}
//       
		return new ResponseEntity<>("Success", HttpStatus.OK);

	}
}
