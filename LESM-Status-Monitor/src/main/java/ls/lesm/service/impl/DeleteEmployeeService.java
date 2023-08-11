package ls.lesm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.model.Address;
import ls.lesm.model.Attachment;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeePhoto;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.History;
import ls.lesm.model.InternalExpenses;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.OnsiteBillExpenses;
import ls.lesm.model.ReleaseEmpDetails;
import ls.lesm.model.Role;
import ls.lesm.model.Salary;
import ls.lesm.model.SecondaryManager;
import ls.lesm.model.Sub_Profit;
import ls.lesm.model.User;
import ls.lesm.model.UserRole;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.AttachementRepo;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeePhotoRepo;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.OnsiteBillExpensesRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.RoleRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SecondaryManagerRepository;
import ls.lesm.repository.Sub_ProfitRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.repository.UserRoleRepo;

@Component
@Getter
@Setter
@NoArgsConstructor
public class DeleteEmployeeService {

	@Autowired
	private DesignationsRepository designationsRepository;

//	@Autowired
//	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
//	

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRopository;

	@Autowired
	private AdminServiceImpl adminServiceImpl;
	@Autowired
	private DeleteEmployeeService deleteEmployeeService;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	private AddressRepositoy addressRepositoy;

	@Autowired
	private SalaryRepository salaryRepository;

	@Autowired
	private EmployeePhotoRepo employeePhotoRepo;

	@Autowired
	private SecondaryManagerRepository secondaryManagerRepository;

	@Autowired
	private Sub_ProfitRepository sub_ProfitRepository;

	@Autowired
	private ClientsRepository clientsRepository;

	@Autowired
	private InternalExpensesRepository internalExpensesRepository;

	@Autowired
	private AttachementRepo attachementRepo;

	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@Autowired
	private OnsiteBillExpensesRepository onsiteBillExpensesRepository;

	@Autowired
	private ReleaseEmpDetailsRepository releaseEmpDetailsRepository;
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepo userRoleRepo;

	public List<Designations> getDesignationsForDelete()

	{

		List<Designations> filter = new ArrayList<>();

		for (Designations d : designationsRepository.findAll())

		{

			String verify = d.getDesgNames().toUpperCase();

			if (!verify.equals("HR") && verify.equals("SUPER ADMIN")) {
				filter.add(d);

			}

		}

		return filter;

	}

	public List<MasterEmployeeDetails> getEmployeesToDelete(int desgid) {

		List<MasterEmployeeDetails> employees = masterEmployeeDetailsRepository.findBydesignations_Id(desgid);
		List<MasterEmployeeDetails> filter = new ArrayList<>();
		for (MasterEmployeeDetails m : employees)

		{

			//if (m.getStatus() != EmployeeStatus.EXIT && m.getStatus() != EmployeeStatus.ABSCONDED
				//	&& m.getStatus() != EmployeeStatus.TERMINATED) {

				filter.add(m);

		//}

		}

		return filter;
	}

	public String deleteEmployeeSubmit(String LancesoftId, boolean flag)

	{
		try {

			MasterEmployeeDetails masterEmployeeDetails = masterEmployeeDetailsRepository.findByLancesoft(LancesoftId);

			if (masterEmployeeDetails != null) {
				flag = employeesUnderMeInMasterTable(masterEmployeeDetails, flag);

				if (flag) {
					photo(masterEmployeeDetails);
					releaseEmployee(masterEmployeeDetails);
					history(masterEmployeeDetails);
					internalExpenses(masterEmployeeDetails);
					subProfit(masterEmployeeDetails);
					secondaryManager(masterEmployeeDetails);
					salaryEmp(masterEmployeeDetails);
					// history(masterEmployeeDetails);
					address(masterEmployeeDetails);
					// releaseEmployee(masterEmployeeDetails);
					onSiteBillExpenses(masterEmployeeDetails);
					resume(masterEmployeeDetails);
					userCredentials(masterEmployeeDetails);
					employeeAtClient(masterEmployeeDetails);
					// history(masterEmployeeDetails);

					deleteMasterEmployeeRecord(masterEmployeeDetails);

				} else {
					throw new RecordNotFoundException(
							"Assign people under employee to his/her supervisor then DELETE the Employee");

				}

				return "Employee is deleted with id" + LancesoftId;
			}

			else {
				return "Employee id is not found with id " + LancesoftId;

			}

		}

		catch (Exception e) {
			return "Unable to delete the record of " + LancesoftId + " due to" + e;

		}

	}

	// done

	public void internalExpenses(MasterEmployeeDetails masterEmployeeDetails)

	{

		List<InternalExpenses> internalExpenses = internalExpensesRepository
				.findsBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
		if (!internalExpenses.isEmpty()) {

			internalExpensesRepository.deleteAll(internalExpenses);

		}

		System.out.println("InternalExpenses deleted");

	}

//done
	public void subProfit(MasterEmployeeDetails masterEmployeeDetails)

	{

		Optional<Sub_Profit> subProfit = sub_ProfitRepository
				.findBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
		if (subProfit.isPresent()) {

			sub_ProfitRepository.delete(subProfit.get());

			System.out.println("sub_profit  deleted");

		}

	}

//done
	public void secondaryManager(MasterEmployeeDetails masterEmployeeDetails)

	{
		List<SecondaryManager> empsUnderMe = secondaryManagerRepository.findBySecondaryManager(masterEmployeeDetails);

		if (!empsUnderMe.isEmpty()) {

			secondaryManagerRepository.deleteAll(empsUnderMe);

		}

		SecondaryManager secondaryManager = secondaryManagerRepository.findByEmployee(masterEmployeeDetails.getEmpId());

		if (secondaryManager != null) {

			secondaryManagerRepository.delete(secondaryManager);
		}
		System.out.println("secondary manager deleted");

	}

//done
	public void salaryEmp(MasterEmployeeDetails masterEmployeeDetails)

	{

		List<Salary> salary = salaryRepository.findsBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());

		salaryRepository.deleteAll(salary);

		System.out.println("salary  deleted");

	}

//done
	public void history(MasterEmployeeDetails masterEmployeeDetails)

	{

		List<History> history = this.historyRepository
				.findsBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
		List<History> updateByH = this.historyRepository.findByUpdatedBy(masterEmployeeDetails);
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

//		List<History> histories = historyRepository.findsBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
//
//		if (!histories.isEmpty()) {
//			historyRepository.deleteAll(histories);

//			for (History emps : histories) {
//
//				emps.setSupervisor(null);
//				historyRepository.save(emps);
//
//			}

		// }

//		List<History> myRecord = historyRepository.findByLancesoft(masterEmployeeDetails.getLancesoft());
//
//		historyRepository.deleteAll(myRecord);

	}

//done
	public void address(MasterEmployeeDetails masterEmployeeDetails)

	{

		Address address = addressRepositoy.findByEmpId(masterEmployeeDetails.getEmpId());

		if (address != null) {
			addressRepositoy.delete(address);

		}
		System.out.println("address  deleted");

	}

//done
	public void releaseEmployee(MasterEmployeeDetails masterEmployeeDetails)

	{

		List<ReleaseEmpDetails> releaseEmpDetails = releaseEmpDetailsRepository
				.findsBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());

		if (!releaseEmpDetails.isEmpty()) {

			for (ReleaseEmpDetails R : releaseEmpDetails) {

				R.setMasterEmployeeDetailssupervisor(null);
				releaseEmpDetailsRepository.save(R);

			}
		}

		Optional<ReleaseEmpDetails> releaseEmps = releaseEmpDetailsRepository
				.findBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());

		if (releaseEmps.isPresent()) {

			releaseEmpDetailsRepository.delete(releaseEmps.get());

		}

		System.out.println("ReleaseEmployeeDetails deleted");

	}

//done
	public void onSiteBillExpenses(MasterEmployeeDetails masterEmployeeDetails)

	{

		List<OnsiteBillExpenses> onsiteBillExpenses = onsiteBillExpensesRepository
				.findByMasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());

		if (!onsiteBillExpenses.isEmpty()) {

			for (OnsiteBillExpenses onsiteBillExpenses1 : onsiteBillExpenses) {

				onsiteBillExpenses1.setMasterEmployeeDetails(null);
				onsiteBillExpensesRepository.save(onsiteBillExpenses1);

			}
		}

		System.out.println("OnsiteBillExpenses deleted");

	}

	public void photo(MasterEmployeeDetails masterEmployeeDetails)

	{

		EmployeePhoto employeePhoto = employeePhotoRepo.findByEmployee(masterEmployeeDetails.getEmpId());

		if (employeePhoto != null) {

			employeePhoto.setMasterEmployeeDetails(null);
			employeePhotoRepo.save(employeePhoto);

			masterEmployeeDetails.setEmployeePhoto(null);

			masterEmployeeDetailsRepository.save(masterEmployeeDetails);

		}

		System.out.println("photo deleted");

	}
	// done

	public void resume(MasterEmployeeDetails masterEmployeeDetails)

	{

		Optional<Attachment> attachment = attachementRepo
				.findBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
		if (attachment.isPresent()) {
			attachment.get().setMasterEmployeeDetails(null);
			attachementRepo.save(attachment.get());
		}

	}
	// done

	public void employeeAtClient(MasterEmployeeDetails masterEmployeeDetails)

	{

		List<EmployeesAtClientsDetails> employeesAtClientsDetails = employeesAtClientsDetailsRepository
				.findsBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
		if (!employeesAtClientsDetails.isEmpty()) {

			employeesAtClientsDetailsRepository.deleteAll(employeesAtClientsDetails);

		}
		System.out.println("EmployeesAtClientDetails deleted");

	}
	// done

	public void userCredentials(MasterEmployeeDetails masterEmployeeDetails)

	{

		User u = userRepository.findByUsername(masterEmployeeDetails.getLancesoft());

		if (u != null) {
			Optional<UserRole> role = this.userRoleRepo.findByUser(u);
			if (role.isPresent()) {
				userRoleRepo.delete(role.get());
			}
			userRepository.delete(u);

		}
		System.out.println("user  deleted");

	}

	public void deleteMasterEmployeeRecord(MasterEmployeeDetails masterEmployeeDetailss)

	{

		if (masterEmployeeDetailss != null) {

			masterEmployeeDetailsRepository.delete(masterEmployeeDetailss);
		}

	}
	// done

	public boolean employeesUnderMeInMasterTable(MasterEmployeeDetails masterEmployeeDetails, boolean flag)

	{

		List<MasterEmployeeDetails> k = masterEmployeeDetailsRepository
				.findBymasterEmployeeDetails_Id(masterEmployeeDetails.getEmpId());
		if (!k.isEmpty()) {

			if (flag) {

				for (MasterEmployeeDetails em : k) {

					if (masterEmployeeDetails.getSupervisor() != null) {

						em.setSupervisor(masterEmployeeDetails.getSupervisor());
					} else {
						em.setSupervisor(null);

					}

					masterEmployeeDetailsRepository.save(em);

				}

				return true;

			} else {

				return false;
			}

		}

		return true;
	}

}
