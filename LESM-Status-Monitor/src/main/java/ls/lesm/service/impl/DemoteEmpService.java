package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SecondaryManagerRepository;
import ls.lesm.repository.UserRepository;

@Service
public class DemoteEmpService {

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	HistoryRepository historyRepository;

	@Autowired
	SalaryRepository salaryRepository;

	@Autowired
	SecondaryManagerRepository subordinateManagerRepository;

	@Autowired
	DesignationsRepository designationsRepository;

	// get the list of designations
	public List<Designations> getEmp() {

		List<Designations> desg = designationsRepository.findAll();

		List<Designations> filter = new ArrayList<>();

		if (desg == null) {

			throw new RecordNotFoundException("records not found");

		}

		for (Designations d : desg) {

			if (!d.getDesgNames().toUpperCase().equals("SUPER ADMIN")) {

				filter.add(d);

			}

		}

		return filter;

	}

	// get the list of designations to Assign

	public List<Designations> chooseDesignstionToAssign(Designations des) {

		List<Designations> desg = designationsRepository.findAll();

		List<Designations> filter = new ArrayList<>();

		if (desg == null) {

			throw new RecordNotFoundException("records not found");

		}

		for (Designations d : desg) {

			if (!d.getDesgNames().toUpperCase().equals("SUPER ADMIN")) {

				filter.add(d);

			}

		}

		filter.remove(des);

		return filter;

	}

	// get the list of employees by desgId

	public List<MasterEmployeeDetails> getAllDesignateEmployes(int id) {

		Optional<Designations> desgnations = designationsRepository.findById(id);

		List<MasterEmployeeDetails> filter1 = new ArrayList<>();

		List<MasterEmployeeDetails> med = masterEmployeeDetailsRepository

				.findByAlldesgIdWithNames(desgnations.get().getDesgId());

		if (med == null) {

			throw new UserNameNotFoundException("Designations are not found");

		}

		System.err.println(med);

		for (MasterEmployeeDetails m1 : med) {

			if ((m1.getStatus() != EmployeeStatus.EXIT) && (m1.getStatus() != EmployeeStatus.ABSCONDED)

					&& (m1.getStatus() != EmployeeStatus.TERMINATED)) {

				filter1.add(m1);

			}

		}

		if (filter1.isEmpty()) {

			throw new UserNameNotFoundException("data not found");

		}

		return filter1;

	}

	// add Primary Supervisor

	public List<?> addSupervisor1lan(int desId, String empId) {

		Designations desg = designationsRepository.findById(desId).get().getDesignations();

		List<MasterEmployeeDetails> finallist = new ArrayList<>();

		for (; desg.getDesgId() != 1; desg = desg.getDesignations()) {

			List<MasterEmployeeDetails> employess = masterEmployeeDetailsRepository

					.findBydesignations_Id(desg.getDesgId());

			for (MasterEmployeeDetails m1 : employess) {

				if ((m1.getStatus() == EmployeeStatus.EXIT) || (m1.getStatus() == EmployeeStatus.ABSCONDED)

						|| (m1.getStatus() == EmployeeStatus.TERMINATED)) {

				} else {

					finallist.add(m1);

				}

			}

		}

		finallist.remove(masterEmployeeDetailsRepository.findByLancesoft(empId));

		// System.out.println(finallist);

		//finallist.add("N/A");

		return finallist;

	}

	// add Second Supervisor

	public List<MasterEmployeeDetails> addSecondSupervisor1(int desId, String superId, String empId) {

		List<MasterEmployeeDetails> finallist = new ArrayList<>();

		if (superId != null) {

			try {

				// Designations desg =

				// masterEmployeeDetailsRepository.findByLancesoft(empId).getDesignations();

				Designations desg = designationsRepository.findById(desId).get();

				for (; desg.getDesgId() != 1; desg = desg.getDesignations()) {

					List<MasterEmployeeDetails> employess = masterEmployeeDetailsRepository

							.findBydesignations_Id(desg.getDesgId());

					for (MasterEmployeeDetails m1 : employess) {

						if ((m1.getStatus() == EmployeeStatus.EXIT) || (m1.getStatus() == EmployeeStatus.ABSCONDED)

								|| (m1.getStatus() == EmployeeStatus.TERMINATED)) {

						} else {

							finallist.add(m1);

						}

					}

				}

			} catch (Exception e) {

				// TODO: handle exception

			}

			finallist.remove(masterEmployeeDetailsRepository.findByLancesoft(empId));

			finallist.remove(masterEmployeeDetailsRepository.findByLancesoft(superId));

			// System.out.println(finallist);

			// finallist.add(null);

			return finallist;

		} else {

			return null;

		}

	}

	// submit

	public ResponseEntity<String> demote1(String emp, String superId1, String superId2, double salary,

			Principal principal, LocalDate updatedAt, Designations newDesignation) {

		User u = userRepository.findByUsername(principal.getName());

		String s = u.getUsername();

		MasterEmployeeDetails upadtedBy = masterEmployeeDetailsRepository.findByLancesoft(s);

		MasterEmployeeDetails med = masterEmployeeDetailsRepository.findByLancesoft(emp);

		if (med != null) {

			User u1 = userRepository.findByUsername(med.getLancesoft());

			if (u1 != null) {

				userRepository.delete(u1);

			}

			List<MasterEmployeeDetails> undermeList = masterEmployeeDetailsRepository
					.findBymasterEmployeeDetails_Id(med.getEmpId());

			System.err.println(undermeList + " undermeList");
			if (!undermeList.isEmpty()) {
				for (MasterEmployeeDetails ulist : undermeList) {
					ulist.setSupervisor(med.getSupervisor());
					masterEmployeeDetailsRepository.save(ulist);

				}
			}

			Designations desg = med.getDesignations();

			UpdatedStatus updateStatus = UpdatedStatus.DEMOTED;

			for (; desg.getDesgId() != 1; desg = desg.getDesignations()) {

				if (desg.equals(newDesignation)) {

					updateStatus = UpdatedStatus.PROMOTED;

				}

			}

			History hs = new History(med.getLancesoft(), med.getFirstName(), med.getLastName(), med.getJoiningDate(),

					med.getDOB(), med.getLocation(), med.getGender(), med.getEmail(), med.getCreatedAt(),

					med.getVertical(), med.getStatus(), med.getAge(), med.getIsInternal(), med.getPhoneNo(),

					med.getCreatedBy(), med.getSubDepartments(), med.getDepartments(), med.getDesignations(),

					med.getSupervisor(), med.getEmployeeType(), updateStatus, updatedAt, upadtedBy);

			historyRepository.save(hs);

			MasterEmployeeDetails sup = masterEmployeeDetailsRepository.findByLancesoft(superId1);

			med.setSupervisor(sup);

			med.setDesignations(newDesignation);

			// for (Designations d : designationsRepository.findAll()) {

			//

			// if (med.getDesignations() == d.getDesignations())

			//

			// {

			//

			// med.setDesignations(d);

			// break;

			// }

			//

			// }

			masterEmployeeDetailsRepository.save(med);

			if (superId2 != null) {

				int empId = med.getEmpId();

				MasterEmployeeDetails sup2 = masterEmployeeDetailsRepository.findByLancesoft(superId2);

				if (sup2 != null) {

					SecondaryManager sub = subordinateManagerRepository.findByEmployee(empId);// get employee in
					if (sub != null) {// subordinate table

						sub.setSecondaryManager(sup2);

						subordinateManagerRepository.save(sub);

					}

				}

			}

			if (salary != 0) {

				Salary s2 = new Salary();

				s2.setSalary(salary);

				s2.setUpdatedAt(updatedAt);

				s2.setMasterEmployeeDetails(med);

				s2.setCreatedBy(med.getCreatedBy());

				s2.setCreatedAt(med.getCreatedAt());

				salaryRepository.save(s2);

			}

//			return new ResponseEntity(
//
//					"DEMOTED\nNote:If the people under me have a higher or equal designation, please transfer them.",
//
//					HttpStatus.ACCEPTED);

			if (updateStatus == UpdatedStatus.DEMOTED) {
				return new ResponseEntity("DEMOTED", HttpStatus.ACCEPTED);
			} else {

				return new ResponseEntity("PROMOTED", HttpStatus.ACCEPTED);
			}

		}

		else {

			return new ResponseEntity("Data not found with id :" + emp, HttpStatus.BAD_REQUEST);

		}

	}

}
