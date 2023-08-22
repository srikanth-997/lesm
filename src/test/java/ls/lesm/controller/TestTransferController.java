package ls.lesm.controller;

import java.util.List;

import org.easymock.Mock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.restcontroller.TransferEmployee;
import ls.lesm.service.impl.TransferEmployeeService;

@ExtendWith(MockitoExtension.class)
public class TestTransferController {
	
	
	
	
	@InjectMocks
	private TransferEmployee transferEmployee ;//mockito create the proxy for the class and memory allocation is happend and inject for the test class
	@Mock
	private TransferEmployeeService transferEmployeeService;
	
	
	@Test
	@DisplayName("GettingEmployeeByID")
	void testgetEmployeesByDesignation(){
		
		Designations desg=new Designations();
		desg.setDesgId(1);
		ResponseEntity<List<MasterEmployeeDetails>> d=transferEmployee.getEmployeesByDesignation(desg.getDesgId());
		
	}
	
}
	

//}
