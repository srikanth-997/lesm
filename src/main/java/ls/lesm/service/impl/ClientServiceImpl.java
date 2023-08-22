package ls.lesm.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ls.lesm.exception.DateMissMatchException;
import ls.lesm.exception.SupervisorAlreadyExistException;
import ls.lesm.model.EmployeesAtClientsDetails;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.payload.request.EmployeeAtClientRequest;
import ls.lesm.repository.AddressRepositoy;
import ls.lesm.repository.AddressTypeRepository;
import ls.lesm.repository.ClientsRepository;
import ls.lesm.repository.DepartmentsRepository;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.EmployeeResponsibilitiesRepository;
import ls.lesm.repository.EmployeeTypeRepository;
import ls.lesm.repository.EmployeesAtClientsDetailsRepository;
import ls.lesm.repository.HistoryRepository;
import ls.lesm.repository.InternalExpensesRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.repository.SalaryRepository;
import ls.lesm.repository.SecondaryManagerRepository;
import ls.lesm.repository.SubDepartmentsRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
	
	
	
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
	private SecondaryManagerRepository secondaryManagerRepo;
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private ReleaseEmpDetailsRepository releaseEmpDetailsRepo;
	
	@Autowired
	private EmployeeResponsibilitiesRepository responsibilityRepo;
	

	
	
	@Override
	public EmployeeAtClientRequest addClientDetailsToEmployee(EmployeeAtClientRequest req, Principal principal) {
		
		EmployeesAtClientsDetails detail=new EmployeesAtClientsDetails();
	

			detail.setPONumber(req.getPONumber());
		
		
			
		      setClientDetails(req, detail, principal);
	
		
		return req;
	}
	
	
	@Override
	public void editClientDetailsForEmployee(Integer clientDetailId, EmployeeAtClientRequest req,
			Principal principal) {
		
		EmployeesAtClientsDetails clientDetail= this.employeesAtClientsDetailsRepository.findById(clientDetailId).orElseThrow(()-> new SupervisorAlreadyExistException("Client Details Not Found"));
	

        	clientDetail.setPONumber(req.getPONumber());
    
		
			setClientDetails(req, clientDetail, principal);
	
		
	}
	
	
	private EmployeeAtClientRequest setClientDetails(EmployeeAtClientRequest req, EmployeesAtClientsDetails detail,Principal principal) {
		
		detail.setCreatedAt(LocalDate.now());
		if(principal!=null)
		detail.setCreatedBy(principal.getName());
		MasterEmployeeDetails employee= this.masterEmployeeDetailsRepository.findById(req.getEmpId()).orElseThrow(()-> new SupervisorAlreadyExistException("Employee does not exist"));
		
		
		
		//set employee id
		detail.setMasterEmployeeDetails(employee);
		
		
		//set client id
		this.clientsRepository.findById(req.getClients()).map(cId -> {
			detail.setClients(cId);
			return cId;
		});
		
		this.clientsRepository.findById(req.getSubContractor()).map(cId -> {
			detail.setSubContractor(cId);
			return cId;
		});
		
		
		Double totaltax=0d;
		
		
		
		
		if(req.getCGST()!=null) {
			totaltax+=req.getCGST();
			detail.setCGST(req.getCGST());
		}else
			detail.setCGST(0d);
		
		if(req.getSGST()!=null) {
			totaltax+=req.getSGST();
			detail.setSGST(req.getSGST());
		}else
			detail.setSGST(0d);
		
		if(req.getIGST()!=null) {
			totaltax+=req.getIGST();
			detail.setIGST(req.getIGST());
		}else
			detail.setIGST(0d);
		
		detail.setTotalTax(totaltax);
		detail.setClientEmail(req.getClientEmail());
		detail.setClientLocation(req.getClientLocation());
		detail.setClientManagerName(req.getClientManagerName());
		detail.setClientSalary(req.getClientSalary());
		detail.setDesgAtClient(req.getDesgAtClient());
		detail.setPOValue(req.getPOValue());
		detail.setSkillSet(req.getSkillSet());
		detail.setWorkMode(req.getWorkMode());
		detail.setClientLastWorkingDate(req.getClientLastWorkingDate());
		detail.setLancesoftLastWorkingDate(req.getLancesoftLastWorkingDate());
		
		if(req.getTowerHead()!=null)
		detail.setTowerHead(this.masterEmployeeDetailsRepository.findById(req.getTowerHead()).orElseThrow(()->new SupervisorAlreadyExistException("Tower Head Employee Does Not Exist")));
		
		if(req.getTowerLead()!=null)
		detail.setTowerLead(this.masterEmployeeDetailsRepository.findById(req.getTowerLead()).orElseThrow(()->new SupervisorAlreadyExistException("Tower Lead Employee Does Not Exist")));
		
		if(req.getRecruiter()!=null)
		detail.setRecruiter(this.masterEmployeeDetailsRepository.findById(req.getRecruiter()).orElseThrow(()->new SupervisorAlreadyExistException("Recruiter Employee Does Not Exist")));
		
		
		if (req.getPOSdate() != null) {
			LocalDate pos = req.getPOSdate();

			if (req.getPOEdate() != null && pos.isAfter(req.getPOEdate()))
				throw new DateMissMatchException("PO Start Date can't be before PO End Date, modify PO Start Date or PO End Date");
//			
			if (pos.isBefore(employee.getJoiningDate()))
				throw new DateMissMatchException("PO Start Date can't be before employee Joining Date, this is Employee Joining Date: "+employee.getJoiningDate());
//			
//			if (req.getPODate()!=null && pos.isBefore(req.getPODate()))
//				throw new DateMissMatchException("PO Start Date can't be before PO Date, modify PO Start Date or PO Date");
//			
//			if (req.getOfferReleaseDate() != null && pos.isBefore(req.getOfferReleaseDate()))
//				throw new DateMissMatchException("PO Start Date can't be before Offer Release Date, modify PO Start Date or Offer Release Date");
//			
//			if (req.getLancesoftLastWorkingDate() != null && pos.isAfter(req.getLancesoftLastWorkingDate()))
//				throw new DateMissMatchException("PO Start Date can't be after Lancesoft Last Working Date, modify PO Start Date or Lancesoft Last Working Date");
//			
//			if (req.getClientLastWorkingDate() != null && pos.isAfter(req.getClientLastWorkingDate()))
//				throw new DateMissMatchException("PO Start Date can't be after Client Last Working Date, modify PO Start Date or Client Last Working Date");
//			
//			if (req.getClientJoiningDate() != null && pos.isBefore(req.getClientJoiningDate()))
//				throw new DateMissMatchException("PO Start Date can't be before Client Joining Date, modify PO Start Date or Client Joining Date");

			detail.setPOSdate(pos);
		}
		
		if(req.getPOEdate()!=null) {
			LocalDate poe=req.getPOEdate();
			if(poe.isBefore(employee.getJoiningDate()))
				throw new DateMissMatchException("PO End Date can't be before employee Joining Date, this is Employee Joing Date: "+employee.getJoiningDate());
			
//			if(req.getClientJoiningDate()!=null && poe.isBefore(req.getClientJoiningDate()))
//				throw new DateMissMatchException("PO End Date can't be before Client Joining Date, modify PO End Date or Client Joining Date");
//			
//			if(req.getPOSdate()!=null && poe.isBefore(req.getPOSdate()))
//				throw new DateMissMatchException("PO End Date can't be before PO Start Date, modify PO End Date or PO Start Date");
//			
//			if(req.getPODate()!=null && poe.isBefore(req.getPODate()))
//				throw new DateMissMatchException("PO End Date can't be before PO Date, modify PO End Date or PO Date");
//			
//			if(req.getOfferReleaseDate()!=null && poe.isBefore(req.getOfferReleaseDate()))
//				throw new DateMissMatchException("PO End Date can't be before Offer Releas Date, modify PO End Date or Offer Release Date");
			
//			if(req.getClientLastWorkingDate()!=null && poe.isAfter(req.getClientLastWorkingDate()))
//				throw new DateMissMatchException("PO End Date can't be after Client Last Working Date");
			
//			if(req.getLancesoftLastWorkingDate()!=null && poe.isAfter(req.getLancesoftLastWorkingDate()))
//				throw new DateMissMatchException("PO End Date can't be after lancesoft Last Working Date");
			
			detail.setPOEdate(poe);
		}
		
		if(req.getClientJoiningDate()!=null) {
			LocalDate cjd=req.getClientJoiningDate();
			if(cjd.isBefore(employee.getJoiningDate()))
				throw new DateMissMatchException("Client Joining Date can't be before employee Joinging Date, this is Employee Joining Date: "+employee.getJoiningDate());
//			
//			if(req.getPODate()!=null && cjd.isBefore(req.getPODate()))
//				throw new DateMissMatchException("Client Joining Date can't be before PO Date, modify Client Joining Date or PO Date");
//			
//			if(req.getOfferReleaseDate()!=null && cjd.isBefore(req.getOfferReleaseDate()))
//				throw new DateMissMatchException("Client Joining Date can't be before Offer Releas Date, modify Client Joining Date or Offer Release Date");
//			
//			if(req.getClientLastWorkingDate()!=null && cjd.isAfter(req.getClientLastWorkingDate()))
//				throw new DateMissMatchException("Client Joining Date can't be after Client Last Working Date, modify Client Joining Date or Client Last Working Date");
//			
//			if(req.getLancesoftLastWorkingDate()!=null&& cjd.isAfter(req.getLancesoftLastWorkingDate()))
//				throw new DateMissMatchException("Client Joining Date can't be after Lancesoft Last Working Date, modify Client Joining Date or Lancesoft Last Working Date");
			
			detail.setClientJoiningDate(cjd);
			
		}
		if(req.getPODate()!=null) {
			LocalDate pod=req.getPODate();
//			if(pod.isBefore(employee.getJoiningDate()))
//				throw new DateMissMatchException("PO Date can't be before employee Joinging Date, this is Employee Joining Date: "+employee.getJoiningDate());
//			
//			if(req.getOfferReleaseDate()!=null && pod.isBefore(req.getOfferReleaseDate()))
//				throw new DateMissMatchException("PO Date can't be before Offer Releas Date");
			
//			if(req.getClientLastWorkingDate()!=null && pod.isAfter(req.getClientLastWorkingDate()))
//				throw new DateMissMatchException("PO Date can't be after Client Last Working Date, modify PO Date or Client Last Working Date");
//			
//			if(req.getLancesoftLastWorkingDate()!=null && pod.isAfter(req.getLancesoftLastWorkingDate()))
//				throw new DateMissMatchException("PO Date can't be after Lancesoft Last Working Date, modify PO Date or Lancesoft Last Working Date");
			
			detail.setPODate(pod);
		}
		if(req.getOfferReleaseDate()!=null) {
			LocalDate ord=req.getOfferReleaseDate();
//			if(req.getClientLastWorkingDate()!=null&& ord.isAfter(req.getClientLastWorkingDate()))
//				throw new DateMissMatchException("Offer Releas Date can't be after Client Last Working Date, modify Offer Release Date or Client Last Workign Date ");
//			
//			if(req.getLancesoftLastWorkingDate()!=null && ord.isAfter(req.getLancesoftLastWorkingDate()))
//				throw new DateMissMatchException("Offer Releas Date can't be after Lancesoft Last Working Date, modify Offer Release Date or Lansoft Last Working Date");
			
			detail.setOfferReleaseDate(ord);
		}
		
		
		EmployeesAtClientsDetails savedClientDetail = this.employeesAtClientsDetailsRepository.save(detail);
		
		return req;
		
	}


	@Override
	public EmployeeAtClientRequest editClientDetailRequest(Integer clientDetailId) {
	
		
	 EmployeesAtClientsDetails clientDetail=this.employeesAtClientsDetailsRepository.findById(clientDetailId).orElseThrow(()-> new SupervisorAlreadyExistException("Client Detail Not found"));
	 EmployeeAtClientRequest req=new EmployeeAtClientRequest();
	 req.setCGST(clientDetail.getCGST());
	 req.setSubContractor(clientDetail.getSubContractor().getClientsId());
	 req.setClientEmail(clientDetail.getClientEmail());
	 req.setClientJoiningDate(clientDetail.getClientJoiningDate());
	 req.setClientLastWorkingDate(clientDetail.getClientLastWorkingDate());
	 req.setClientLocation(clientDetail.getClientLocation());
	 req.setClientManagerName(clientDetail.getClientManagerName());
	 req.setClients(clientDetail.getClients().getClientsId());
	 req.setClientSalary(clientDetail.getClientSalary());
	 req.setDesgAtClient(clientDetail.getDesgAtClient());
	 req.setEmpId(clientDetail.getMasterEmployeeDetails().getEmpId());
	 req.setIGST(clientDetail.getIGST());
	 req.setLancesoftLastWorkingDate(clientDetail.getLancesoftLastWorkingDate());
	 req.setOfferReleaseDate(clientDetail.getOfferReleaseDate());
	 req.setPODate(clientDetail.getPODate());
	 req.setPOEdate(clientDetail.getPOEdate());
	 req.setPONumber(clientDetail.getPONumber());
	 req.setPOSdate(clientDetail.getPOSdate());
	 req.setPOValue(clientDetail.getPOValue());
	 req.setRecruiter(clientDetail.getRecruiter().getEmpId());
	 req.setSGST(clientDetail.getSGST());
	 req.setSkillSet(clientDetail.getSkillSet());
	 req.setTowerHead(clientDetail.getTowerHead().getEmpId());
	 req.setTowerLead(clientDetail.getTowerLead().getEmpId());
	 req.setWorkMode(clientDetail.getWorkMode());
	 
		return req;
	}


}
