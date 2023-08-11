package ls.lesm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.DesignationsRepository;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.Sub_ProfitRepository;

@Service
public class DefaultCalculationsServiceImpl {
	@Autowired
	BusinessCalculation businessCalculation;

	@Autowired
	LeadCalculation leadCalculation;

	@Autowired
	ManagerCalculation managerCalculation;

	@Autowired
	GeneralManagerCalculation generalManagerCalculation;

	@Autowired
	CountryHeadCalculation countryHeadCalculation;

	@Autowired
	ManagingDirectorCalculation managingDirectorCalculation;

	@Autowired
	MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;

	@Autowired
	Sub_ProfitRepository sub_ProfitRepository;

	@Autowired
	VicePresidentCalculation vicePresidentCalculation;

	@Autowired
	DesignationsRepository designationsRepository;

	@Autowired
	CalculationsUptoDate calculationsUptoDate;
	
	 @Scheduled(cron = "0 0 6 * * *")

	public void defaultCalculationCall() throws Exception

	{
		try {

			List<Designations> listOfDesignations = designationsRepository.findAll();

			for (Designations desg : listOfDesignations) {

				List<MasterEmployeeDetails> employee = this.masterEmployeeDetailsRepository

						.findBydesignations_Id(desg.getDesgId());

				if (desg.getDesgNames().equals("MD")) {
					for (MasterEmployeeDetails employesUnderMe : employee) {

						managingDirectorCalculation.managingDirectorCal(employesUnderMe.getEmpId(), null, null);
					}
					continue;
				}

				if (desg.getDesgNames().equals("CH")) {
					for (MasterEmployeeDetails employesUnderMe : employee) {
						countryHeadCalculation.countryHeadCal(employesUnderMe.getEmpId(), null, null);

					}
					continue;
				}

				if (desg.getDesgNames().equals("GM")) {
					for (MasterEmployeeDetails employesUnderMe : employee) {

						generalManagerCalculation.generalManagercal(employesUnderMe.getEmpId(), null, null);
					}
					continue;
				}

				if (desg.getDesgNames().toUpperCase().equals("MANAGER")) {
					for (MasterEmployeeDetails employesUnderMe : employee) {
						managerCalculation.manager_cal(employesUnderMe.getEmpId(), null, null);

					}
					continue;
				}

				if (desg.getDesgNames().toUpperCase().equals("LEAD")) {
					for (MasterEmployeeDetails employesUnderMe : employee) {
						leadCalculation.lead_cal(employesUnderMe.getEmpId(), null, null);

					}
					continue;
				}

			}

		} catch (Exception e) {
			throw new Exception("Can't able to perform default calculations due to" + e);
		}

	}

}
