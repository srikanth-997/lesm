package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.RecordNotFoundException;
import ls.lesm.exception.UserNameNotFoundException;
import ls.lesm.model.Designations;
import ls.lesm.model.History;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Salary;
import ls.lesm.model.SecondaryManager;
import ls.lesm.model.User;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.enums.UpdatedStatus;
import ls.lesm.payload.request.TransferRequest;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SecondaryManagerRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.UserRepository;

@Service
public class TransferEmployeeService {

	@Autowired
	MasterEmployeeDetailsRepository employeeDetailsRepository;
	@Autowired
	UserRepository userRepo;
	@Autowired
	HistoryRepository historyRepo;
	@Autowired
	SalaryRepository salaryRepository;

	@Autowired
	SubDepartmentsRepository subDepartmentsRepository;

	@Autowired
	DesignationsRepository designationsRepository;
	@Autowired
	SecondaryManagerRepository secondaryManagerRepository;

	public List<Designations> GetAllDesignation() {
		List<Designations> designations = designationsRepository.findAll();

		return designations;
	}

	public List<MasterEmployeeDetails> GetEmployees(int designation) {

		List<MasterEmployeeDetails> masterEmployeeDetails = employeeDetailsRepository
				.findBydesignations_Id(designation);

		if (masterEmployeeDetails.isEmpty()) {

			throw new RecordNotFoundException("No records are found");
		} else {

			List<MasterEmployeeDetails> filter = new ArrayList<MasterEmployeeDetails>();

			for (MasterEmployeeDetails m : masterEmployeeDetails) {
				if (m.getStatus() != EmployeeStatus.EXIT && m.getStatus() != EmployeeStatus.ABSCONDED
						&& m.getStatus() != EmployeeStatus.TERMINATED) {
					filter.add(m);
				}

			}
			return filter;
		}

	}

	public List<MasterEmployeeDetails> getnewSupervisiorsinHrDashboard(String lancesoftId) {

		Integer s = employeeDetailsRepository.findByLancesoft(lancesoftId).getSupervisor().getEmpId();

		Integer designation = employeeDetailsRepository.findById(s).get().getDesignations().getDesgId();

		List<MasterEmployeeDetails> masterEmployeeDetails2 = employeeDetailsRepository
				.findBydesignations_Id(designation);

		return masterEmployeeDetails2;

	}
	
	
	
	
	
	
	
	
	
	

	public List<MasterEmployeeDetails> reportstoDashboard(String lancesoftId) {
		Designations desg = employeeDetailsRepository.findByLancesoft(lancesoftId).getDesignations();

		List<MasterEmployeeDetails> finallist = new ArrayList<>();

		desg = desg.getDesignations();

		for (; desg.getDesgId() != 1; desg = desg.getDesignations())

		{
			List<MasterEmployeeDetails> singledes = employeeDetailsRepository.findBydesignations_Id(desg.getDesgId());
			for(MasterEmployeeDetails m:singledes) {
			if (m.getStatus() != EmployeeStatus.EXIT && m.getStatus() != EmployeeStatus.ABSCONDED
					&& m.getStatus() != EmployeeStatus.TERMINATED) {
			
			
			       finallist.add(m);
			
			}
			}
			
		}

		//finallist.remove(employeeDetailsRepository.findByLancesoft(lancesoftId));
		// finallist.remove(employeeDetailsRepository.findByLancesoft(lancesoftId).getSupervisor());

		return finallist;

	}

	public List<MasterEmployeeDetails> secondaryreportstoDashboard(String lancesoftId, String primaryLancesoftId) {
		Designations desg = employeeDetailsRepository.findByLancesoft(lancesoftId).getDesignations();

		List<MasterEmployeeDetails> finallist = new ArrayList<>();

		for (; desg.getDesgId() != 1; desg = desg.getDesignations())

		{
			List<MasterEmployeeDetails> singledes = employeeDetailsRepository.findBydesignations_Id(desg.getDesgId());
			for(MasterEmployeeDetails m:singledes) {
				if (m.getStatus() != EmployeeStatus.EXIT && m.getStatus() != EmployeeStatus.ABSCONDED
						&& m.getStatus() != EmployeeStatus.TERMINATED) {
				
				
				       finallist.add(m);
				
				}
				}
		}
		finallist.remove(employeeDetailsRepository.findByLancesoft(primaryLancesoftId));
		finallist.remove(employeeDetailsRepository.findByLancesoft(lancesoftId));
		// finallist.remove(employeeDetailsRepository.findByLancesoft(lancesoftId).getSupervisor());
		return finallist;

	}

	public void transferEmployeeToSameDesignation(String lancesoftId, String primaryLancesoftId,
			String secondaryLancesoftId, String Location, Double Salary, Principal principal) {

		String Location1 = null;
		Double Salary1 = null;
		Predicate<MasterEmployeeDetails> p1 = i -> i == null;

		System.out.println("S");

		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findByLancesoft(lancesoftId);

		List<Salary> sal = salaryRepository.findsBymasterEmployeeDetails_Id(trasferEmp.getEmpId());
		for (Salary s : sal) {

			Location1 = trasferEmp.getLocation();

			Salary1 = s.getSalary();

		}

		if (p1.test(trasferEmp)) {
			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
		}
		MasterEmployeeDetails trasferToEmp = employeeDetailsRepository.findByLancesoft(primaryLancesoftId);
		if (p1.test(trasferToEmp)) {
			throw new UserNameNotFoundException("trasferToEmp not there with this lancesoftId");
		}
		String id = this.userRepo.findByUsername(principal.getName()).getUsername();

		MasterEmployeeDetails updatedBy = this.employeeDetailsRepository.findByLancesoft(id);

		MasterEmployeeDetails secondary = employeeDetailsRepository.findByLancesoft(secondaryLancesoftId);

		if (!Location.equals(null) && Salary != 0) {

			Location1 = Location;

			Salary1 = Salary;
		}

		else if (!Location.equals(null) && Salary == 0) {

			Location1 = Location;

		} else if (Location.equals(null) && Salary != 0) {

			Salary1 = Salary;

		}

		History historyEmp = new History(trasferEmp.getLancesoft(), trasferEmp.getFirstName(), trasferEmp.getLastName(),
				trasferEmp.getJoiningDate(), trasferEmp.getDOB(), trasferEmp.getLocation(), trasferEmp.getGender(),
				trasferEmp.getEmail(), trasferEmp.getCreatedAt(), trasferEmp.getVertical(), trasferEmp.getStatus(),
				trasferEmp.getAge(), trasferEmp.getIsInternal(), trasferEmp.getPhoneNo(), trasferEmp.getCreatedBy(),
				trasferEmp.getSubDepartments(), trasferEmp.getDepartments(), trasferEmp.getDesignations(),
				trasferEmp.getSupervisor(), trasferEmp.getEmployeeType(), UpdatedStatus.TRANSFERRED, LocalDate.now(),
				updatedBy);

		historyRepo.save(historyEmp);

		trasferEmp.setSupervisor(trasferToEmp);

		trasferEmp.setLocation(Location);

		employeeDetailsRepository.save(trasferEmp);

		System.out.println("DONE");

//		if(secondaryLancesoftId==null)
//		{
//			SecondaryManager s = secondaryManagerRepository.findByEmployees(trasferEmp);
//			secondaryManagerRepository.save(s);
//
//		}
		if (secondary != null) {

			Optional<SecondaryManager> s1 = secondaryManagerRepository.findByEmployee(trasferEmp);

			if (s1.isPresent())

			{

				SecondaryManager s = s1.get();

				System.out.println(secondary.getEmpId() + "   " + trasferEmp.getEmpId());

				s.setSecondaryManager(secondary);

				secondaryManagerRepository.save(s);

				System.out.println("saved");

			}

		}

		if (Salary != 0)

		{

			List<Salary> salrec = salaryRepository.findsBymasterEmployeeDetails_Id(trasferEmp.getEmpId());

			for (Salary sall : salrec) {

				// Salary sal =
				// salaryRepository.findBymasterEmployeeDetails_Id(trasferEmp.getEmpId()).get();

				salaryRepository.save(
						new Salary(Salary1, sall.getCreatedAt(), LocalDate.now(), sall.getCreatedBy(), trasferEmp));
				break;
			}
		}
	}

////1
//
//	public List<MasterEmployeeDetails> getAllEmployeeUnderCL(Principal principal) {
//
//		String id = this.userRepo.findByUsername(principal.getName()).getUsername();
//
//		int dbPk = this.employeeDetailsRepository.findByLancesoft(id).getEmpId();
//
//		List<MasterEmployeeDetails> K = employeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);
//
//		return K;
//
//	}
//
////2
//	public List<MasterEmployeeDetails> transferService(String LancesoftId) {
//		Predicate<MasterEmployeeDetails> p1 = employee -> employee == null;
//		// List<MasterEmployeeDetails> listOfEmp = new ArrayList<>();
//		MasterEmployeeDetails employee = this.employeeDetailsRepository.findByLancesoft(LancesoftId);
//
//		if (p1.test(employee)) {
//			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
//		} else {
//			List<MasterEmployeeDetails> employeeDesignation = this.employeeDetailsRepository
//					.findByDesignations(employee.getDesignations());
//
//			return employeeDesignation;
//		}
//	}
//
//	public List<MasterEmployeeDetails> transferServices(String LancesoftId) {
//		Predicate<MasterEmployeeDetails> p1 = employee -> employee == null;
//		// List<MasterEmployeeDetails> listOfEmp = new ArrayList<>();
//		MasterEmployeeDetails employee = this.employeeDetailsRepository.findByLancesoft(LancesoftId);
//
//		if (p1.test(employee)) {
//			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
//		} else {
//			List<MasterEmployeeDetails> employeeDesignation = this.employeeDetailsRepository
//					.findByDesignations(employee.getDesignations());
//
//			return employeeDesignation;
//		}
//	}
//
////3
//
//	public void saveAndnext(String lancesoftId, String assign_id) {
//		Predicate<MasterEmployeeDetails> p1 = employee -> employee == null;
//
//		MasterEmployeeDetails trasferingEmp = this.employeeDetailsRepository.findByLancesoft(lancesoftId);
//		if (p1.test(trasferingEmp)) {
//			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
//		} else {
//
//			List<MasterEmployeeDetails> employeeDesignation = this.employeeDetailsRepository
//					.findBymasterEmployeeDetails_Id(trasferingEmp.getEmpId());
//
//			MasterEmployeeDetails trasferingToEmp = this.employeeDetailsRepository.findByLancesoft(assign_id);
//			if (p1.test(trasferingToEmp)) {
//				throw new UserNameNotFoundException("Assingto Employee not there with this lancesoftId");
//			}
////			for (MasterEmployeeDetails masterEmployeeDetails : employeeDesignation) {
////
////				masterEmployeeDetails.setSupervisor(trasferingToEmp);
////				employeeDetailsRepository.save(masterEmployeeDetails);
////
////			}
//
//			employeeDesignation.stream().forEach(masterEmployeeDetails -> {
//				masterEmployeeDetails.setSupervisor(trasferingToEmp);
//				employeeDetailsRepository.save(masterEmployeeDetails);
//			});
//
//		}
//	}
//
//	/// 4
//
//	public List<MasterEmployeeDetails> newsupervisiorIDs(Principal principal) {
//		String lancesoft = this.userRepo.findByUsername(principal.getName()).getUsername();
//		Predicate<MasterEmployeeDetails> p1 = i -> i == null;
//		MasterEmployeeDetails masterEmployeeDetails = employeeDetailsRepository.findByLancesoft(lancesoft);
//		if (p1.test(masterEmployeeDetails)) {
//			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
//		} else {
//			List<MasterEmployeeDetails> under_masterEmployeeDetails = employeeDetailsRepository
//					.findBydesignations_Id(masterEmployeeDetails.getDesignations().getDesgId());
//			return under_masterEmployeeDetails;
//		}
//	}
//
////5

///lead transfer
//	public void transferServiceMR(String lancesoftId, Principal principal) {
//
////		
////		User loggedU=this.userRepo.findByUsername(principal.getName());
////	
////		String id=loggedU.getUsername();
////	
////		MasterEmployeeDetails currentemployee=this.employeeDetailsRepository.findByLancesoft(id);
////	
////		int dbPk=currentemployee.getEmpId();     
////		
////		List<MasterEmployeeDetails> K = employeeDetailsRepository.findBymasterEmployeeDetails_Id(dbPk);
////		for (MasterEmployeeDetails l : K) {
////			System.out.println(l);
////		}
//
//		MasterEmployeeDetails employee = this.employeeDetailsRepository.findByLancesoft(lancesoftId);
//
//		Designations desg = employee.getDesignations();
//		SubDepartments sub_dep = employee.getSubDepartments();
//		MasterEmployeeDetails sup = employee.getSupervisor();
//		Integer i = sup.getEmpId();
//		List<MasterEmployeeDetails> H = this.employeeDetailsRepository
//				.findByDesignations_IdANDSubDepartmentsANDSupervisor(desg.getDesgId(), sub_dep.getSubDepartId(), i);
//
//		for (MasterEmployeeDetails masterEmployeeDetails : H) {
//
//			System.out.println(masterEmployeeDetails);
//
//		}
//
//	}

	// updated Add-ons///// need modifications............... HR dashborard

//	public List<MasterEmployeeDetails> reportstoDashboard(String lancesoftId) {
//		Designations s=employeeDetailsRepository.findByLancesoft(lancesoftId).getDesignations();
//		System.out.println("..........................................."+s);
//		List<MasterEmployeeDetails> m=new ArrayList();
//		Integer superdesignation=s.getDesgId();
//		System.out.println("////////////////////////////"+superdesignation);
//		for(int i=superdesignation;i>=s.getDesignations().getDesgId();i--)
//		{
//		
//		List<Designations> d=designationsRepository.findBySupervisorId(i);
//		System.out.println("////////////////////////////"+d);  
//		
//		}
//		return m;
//
//	}

	///// ManagerDashboard Lead and consultant

//	public List<MasterEmployeeDetails> consultantsinManagerDashboard(Principal principal) {
//
//		String id = this.userRepo.findByUsername(principal.getName()).getUsername();
//
//		MasterEmployeeDetails manager = this.employeeDetailsRepository.findByLancesoft(id);
//		if (manager == null) {
//			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
//		}
//
//		List<MasterEmployeeDetails> leads = this.employeeDetailsRepository
//				.findBymasterEmployeeDetails_Id(manager.getEmpId());
//
//		List<MasterEmployeeDetails> consultantslist = new ArrayList<MasterEmployeeDetails>();
//		List<MasterEmployeeDetails> consultants = null;
//		for (MasterEmployeeDetails leads_id : leads) {
//
//			consultants = this.employeeDetailsRepository.findBymasterEmployeeDetails_Id(leads_id.getEmpId());
//
//		}
//		consultantslist.addAll(leads);
//		consultantslist.addAll(consultants);
//		return consultantslist;
//	}
//
//	public List<MasterEmployeeDetails> reportstoDropdown(String lancesoftId) {
//		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findByLancesoft(lancesoftId);
//		List<MasterEmployeeDetails> reportsto = this.employeeDetailsRepository
//				.findsBymasterEmployeeDetails_Id(trasferEmp.getSupervisor().getDesignations().getDesgId());
//
//		return reportsto;
//	}

//	public List<MasterEmployeeDetails> leadsinManagerDashboard(Principal principal) {
//		String id = this.userRepo.findByUsername(principal.getName()).getUsername();
//
//		MasterEmployeeDetails manager = this.employeeDetailsRepository.findByLancesoft(id);
//		if (manager == null) {
//			throw new UserNameNotFoundException("Employee not there with this lancesoftId");
//		}
//
//		Integer manager_id = manager.getEmpId();
//
//		List<MasterEmployeeDetails> leads = this.employeeDetailsRepository.findBymasterEmployeeDetails_Id(manager_id);
//
//		return leads;
//	}

//	
//	
//	
//
//	public void updateLocation(int employeeId, String newLocation, int newsupervisor, Double newsalary,
//			MasterEmployeeDetails updatedBy) {
//
//		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findById(employeeId).get();
//
//		MasterEmployeeDetails newmasterEmployeeDetails = employeeDetailsRepository.findById(newsupervisor).get();
//
//		History historyEmp = new History(trasferEmp.getLancesoft(), trasferEmp.getFirstName(), trasferEmp.getLastName(),
//				trasferEmp.getJoiningDate(), trasferEmp.getDOB(), trasferEmp.getLocation(), trasferEmp.getGender(),
//				trasferEmp.getEmail(), trasferEmp.getCreatedAt(), trasferEmp.getVertical(), trasferEmp.getStatus(),
//				trasferEmp.getAge(), trasferEmp.getIsInternal(), trasferEmp.getPhoneNo(), trasferEmp.getCreatedBy(),
//				trasferEmp.getExitAt(), trasferEmp.getSubDepartments(), trasferEmp.getDepartments(),
//				trasferEmp.getDesignations(), trasferEmp.getSupervisor(), trasferEmp.getEmployeeType(),
//				UpdatedStatus.TRANSFERRED, LocalDate.now(), updatedBy);
//
//		historyRepo.save(historyEmp);
//
//		trasferEmp.setLocation(newLocation);
//
//		trasferEmp.setSupervisor(newmasterEmployeeDetails);
//
//		employeeDetailsRepository.save(trasferEmp);
//
//		salaryRepository.save(new Salary(newsalary, LocalDate.now(), trasferEmp));
//
//		//
//
//	}
//
//	public List<MasterEmployeeDetails> GetEmployeesByDesignationandSubD(int empId) {
//
//		MasterEmployeeDetails employeeDetails = employeeDetailsRepository.findById(empId).get();
//
//		List<MasterEmployeeDetails> masterEmployeeDetails = employeeDetailsRepository
//				.findBydesignations_Id(employeeDetails.getDesignations().getDesgId());
//
//		String sub_dep = employeeDetails.getSubDepartments().getSubDepartmentNames();
//
//		List<MasterEmployeeDetails> employees = new ArrayList<MasterEmployeeDetails>();
//
//		for (MasterEmployeeDetails me : masterEmployeeDetails) {
//
//			if (me.getSubDepartments().getSubDepartmentNames().equals(sub_dep)) {
//
//				employees.add(me);
//			}
//
//		}
//
//		System.out.println(employees);
//
//		return employees;
//
//	}
//
//	public List<MasterEmployeeDetails> updateEmployeeDetailsUpdate(int updatedSupervisio_id, int superviserId) {
//
//		List<MasterEmployeeDetails> list = employeeDetailsRepository.findBymasterEmployeeDetails_Id(superviserId);
//		System.out.println(list);
//		MasterEmployeeDetails list1 = employeeDetailsRepository.findById(updatedSupervisio_id).get();
//		System.out.println(list1);
//
//		for (MasterEmployeeDetails mes : list) {
//			System.out.println(mes);
//
//			mes.setSupervisor(list1);
//
//			employeeDetailsRepository.save(mes);
//
//		}
//
//		return list;
//
//	}
//
//	public void updateDetails(int employeeId, String newLocation, Double newsalary, int sub_department,
//			int newsupervisor, MasterEmployeeDetails updatedBy) {
//
//		MasterEmployeeDetails trasferEmp = employeeDetailsRepository.findById(employeeId).get();
//
//		MasterEmployeeDetails newmasterEmployeeDetails = employeeDetailsRepository.findById(newsupervisor).get();
//
//		// code
//
//		SubDepartments sudp = subDepartmentsRepository.findById(sub_department).get();
//
//		History historyEmp = new History(trasferEmp.getLancesoft(), trasferEmp.getFirstName(), trasferEmp.getLastName(),
//				trasferEmp.getJoiningDate(), trasferEmp.getDOB(), trasferEmp.getLocation(), trasferEmp.getGender(),
//				trasferEmp.getEmail(), trasferEmp.getCreatedAt(), trasferEmp.getVertical(), trasferEmp.getStatus(),
//				trasferEmp.getAge(), trasferEmp.getIsInternal(), trasferEmp.getPhoneNo(), trasferEmp.getCreatedBy(),
//				trasferEmp.getExitAt(), trasferEmp.getSubDepartments(), trasferEmp.getDepartments(),
//				trasferEmp.getDesignations(), trasferEmp.getSupervisor(), trasferEmp.getEmployeeType(),
//				UpdatedStatus.TRANSFERRED, LocalDate.now(), updatedBy);
//
//		historyRepo.save(historyEmp);
//
//		trasferEmp.setLocation(newLocation);
//
//		trasferEmp.setSubDepartments(sudp);
//		trasferEmp.setSupervisor(newmasterEmployeeDetails);
//
//		salaryRepository.save(new Salary(newsalary, LocalDate.now(), trasferEmp));
//
//		employeeDetailsRepository.save(trasferEmp);
//
//	}

}
