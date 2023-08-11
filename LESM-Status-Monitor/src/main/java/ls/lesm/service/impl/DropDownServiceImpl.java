package ls.lesm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.SupervisorAlreadyExistException;
import ls.lesm.model.Designations;
import ls.lesm.model.EmployeeResponsibilities;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.enums.EmployeeStatus;
import ls.lesm.model.enums.ResponsibilitiesTypes;
import ls.lesm.payload.response.ConsultantDropDownRes;
import ls.lesm.payload.response.DesignatinsResponse;
import ls.lesm.payload.response.EmployeesDropDownResponse;
import ls.lesm.payload.response.ReportToDropDownRes;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.AddressTypeRepository;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeeResponsibilitiesRepository;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.DropDownService;

@Service
public class DropDownServiceImpl implements DropDownService {
	
	@Autowired
	private AddressRepositoy addressRepositoy;

	@Autowired
	private AddressTypeRepository addressTypeRepository;

	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	private DepartmentsRepository departmentsRepository;

	@Autowired
	private SubDepartmentsRepository subDepartmentsRepositorye;

	@Autowired
	private DesignationsRepository designationsRepository;

	@Autowired
	private EmployeesAtClientsDetailsRepository employeesAtClientsDetailsRepository;

	@Autowired
	private InternalExpensesRepository internalExpensesRepository;

	@Autowired
	private SalaryRepository salaryRepository;

	@Autowired
	private ClientsRepository clientsRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EmployeeTypeRepository employeeTypeRepo;
	
	@Autowired
	private EmployeeResponsibilitiesRepository employeeResponsibilityRepo;
	
	
	@Override
	public List<ConsultantDropDownRes> getAllConsByPractice(int subDId,String keyword) {
//		this.designationsRepository.findByDesgNames("Consultant");
//		this.subDepartmentsRepositorye.findBySubDepartmentNames("java");
		List<MasterEmployeeDetails> consultant=this.masterEmployeeDetailsRepository
				.findBySubDepartmentsAndDesignationsAndLancesoftContainingOrFirstNameContainingOrLastNameContaining(
				this.subDepartmentsRepositorye.findById(subDId).get(),
				this.designationsRepository.findByDesgNamesIgnoreCase("Consultant"),
				keyword,keyword,keyword);
		
		List<ConsultantDropDownRes> conList=new ArrayList<ConsultantDropDownRes>();
		for(MasterEmployeeDetails m: consultant) {
			if(!m.getStatus().equals(EmployeeStatus.ABSCONDED)
					||!m.getStatus().equals(EmployeeStatus.TERMINATED)
					||!m.getStatus().equals(EmployeeStatus.EXIT)) {
				
			ConsultantDropDownRes res=new ConsultantDropDownRes();
			
			if(m.getDesignations().getDesgNames().equals("Consultant")||
					m.getDesignations().getDesgNames().equals("consultant") ||
					m.getDesignations().getDesgNames().equals("Consultants")||
					m.getDesignations().getDesgNames().equals("consultants")) {
				
			res.setName(m.getFirstName()+" "+m.getLastName());
			res.setLancesoftId(m.getLancesoft());
			res.setEmpId(m.getEmpId());
			res.setDesignation(m.getDesignations().getDesgNames());
			conList.add(res);
			}
			}
		}
		return conList;
	}

	@Override
	public List<ReportToDropDownRes> getEmployeeByDesigAndSearch(int desgId,String keyword) {
		Designations desig=this.designationsRepository.findById(desgId).get();
		List<MasterEmployeeDetails> employees=this.masterEmployeeDetailsRepository
				.findByDesignationsAndFirstNameContainingOrLastNameContainingOrLancesoftContaining(
				desig,keyword,keyword,keyword);
		
		List<ReportToDropDownRes> employeesList=new ArrayList<ReportToDropDownRes>();
		
		for(MasterEmployeeDetails m: employees) {
			if(m.getStatus().equals(EmployeeStatus.CLIENT)
					||m.getStatus().equals(EmployeeStatus.BENCH)
					||m.getStatus().equals(EmployeeStatus.MANAGMENT)) {
			if(desig.getDesgNames().equals(m.getDesignations().getDesgNames())) {
			ReportToDropDownRes res=new ReportToDropDownRes();
			res.setEmpId(m.getEmpId());
			res.setLancesoftId(m.getLancesoft());
			res.setName(m.getFirstName()+" "+m.getLastName());
			res.setDesigId(m.getDesignations().getDesgId());
			employeesList.add(res);
			}
			}
			
		}
	
		return employeesList;
	}

	@Override
	public List<Designations> getAllDesignationsEqualOrAbove(int desigId) {
		Designations desig=this.designationsRepository.findById(desigId).get();
	try {
		List<Designations> greaterThenEqualDesigs=this.designationsRepository.findByLevelLessThanEqual(desig.getLevel());
		return greaterThenEqualDesigs;
	}catch (NoSuchElementException e) {
		
		return null;
	}
		
		
	}

	@Override
	public List<DesignatinsResponse> getAllDesignationExceptHR() {
		List<Designations> desigs=this.designationsRepository.findAll();
		List<DesignatinsResponse> desigExceptHr=new ArrayList<DesignatinsResponse>();
		for(Designations d: desigs) {
			if(d.getDesgNames().equals("HR") || d.getDesgNames().equals("Hr") ||d.getDesgNames().equals("hr")) {
				//do nothing
			}else if(d.getDesgNames()!="HR"){
				DesignatinsResponse desig=new DesignatinsResponse();
				desig.setName(d.getDesgNames());
				desig.setDesigId(d.getDesgId());
			
			desigExceptHr.add(desig);
			}
		}
		return desigExceptHr;
	}

	@Override
	public List<Designations> getAboveAllDesignations(int desigId) {
		//List<Designations> list=new ArrayList<Designations>();
		Designations desig=this.designationsRepository.findById(desigId).get();
		
		
		List<Designations> greaterThenDesigs=this.designationsRepository.findByLevelLessThan(desig.getLevel());
		
		
		return greaterThenDesigs;
	}

//	@Override
//	public List<ResponsibilitiesTypes> responsibilityDropDwon(Integer empId) {
//		
//	   MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findById(empId).orElseThrow(()->new SupervisorAlreadyExistException("Employee does not exist"));
//	   
//	    Set<ResponsibilitiesTypes> resList=new HashSet<ResponsibilitiesTypes>();
//	    	 
//	    List<ResponsibilitiesTypes> allResponsibilites=List.of(ResponsibilitiesTypes.values());
//	  
//	    Set<ResponsibilitiesTypes> employeeHasResponsiblities=new HashSet<ResponsibilitiesTypes>();
//	    
//	    List<EmployeeResponsibilities> responsibilities= this.employeeResponsibilityRepo.findByMasterEmployeeDetails(employee);
//	        for(EmployeeResponsibilities r: responsibilities) {
//	        	employeeHasResponsiblities.add(r.getResponsibilitiesTypes());
//	        }
//	        
//	       List<ResponsibilitiesTypes> missingResponsibilites=new ArrayList<ResponsibilitiesTypes>();
//	       for(ResponsibilitiesTypes res: allResponsibilites) {
//	    	   if(!employeeHasResponsiblities.contains(res)) {
//	    		   missingResponsibilites.add(res);
//	    	   }
//	       }
//	   
//		return missingResponsibilites;
//	}
//	
	@Override
	public List<ResponsibilitiesTypes> responsibilityDropDown(Integer empId) {
	    MasterEmployeeDetails employee = this.masterEmployeeDetailsRepository.findById(empId)
	            .orElseThrow(() -> new SupervisorAlreadyExistException("Employee does not exist"));

	    Set<ResponsibilitiesTypes> employeeHasResponsibilities = this.employeeResponsibilityRepo.findByMasterEmployeeDetails(employee)
	            .stream()
	            .map(EmployeeResponsibilities::getResponsibilitiesTypes)
	            .collect(Collectors.toSet());

	    List<ResponsibilitiesTypes> missingResponsibilities = Arrays.stream(ResponsibilitiesTypes.values())
	            .filter(res -> !employeeHasResponsibilities.contains(res))
	            .collect(Collectors.toList());

	    return missingResponsibilities;
	}


	
	@Override
	public Set<EmployeesDropDownResponse> getEmployeesDropDownByResponsibilityWithSearch(String keyword,ResponsibilitiesTypes types) {
	    String toLowerKeyword = keyword.toLowerCase();
	    
	    
	    List<EmployeeResponsibilities> responsibilities = this.employeeResponsibilityRepo.findByResponsibilitiesTypes(types);
	   
	    return responsibilities.stream()
	            .map(EmployeeResponsibilities::getMasterEmployeeDetails)
	            .filter(m -> !m.getStatus().equals(EmployeeStatus.EXIT) &&
	                    !m.getStatus().equals(EmployeeStatus.ABSCONDED) &&
	                    !m.getStatus().equals(EmployeeStatus.TERMINATED) &&
	                    (m.getFirstName().toLowerCase().contains(toLowerKeyword) ||
	                            m.getLastName().toLowerCase().contains(toLowerKeyword) ||
	                            m.getLancesoft().toLowerCase().contains(toLowerKeyword)))
	            .map(m -> {
	            	 EmployeesDropDownResponse dropDown = new EmployeesDropDownResponse();
	                dropDown.setEmpId(m.getEmpId());
	                dropDown.setLancesoftId(m.getLancesoft());
	                dropDown.setName(StringUtils.capitalize(m.getFirstName()) + " " + StringUtils.capitalize(m.getLastName()));
	                return dropDown;
	            })
	            .collect(Collectors.toSet());
	}

	
	

}
