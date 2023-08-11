package ls.lesm.restcontroller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.Designations;
import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.ReleaseEmpDetailsRepository;
import ls.lesm.service.impl.AbscondEmpServiceImpl;
import ls.lesm.service.impl.AdminServiceImpl;

@RestController
@CrossOrigin("*")
public class AbscondEmpController {



    @Autowired
    MasterEmployeeDetailsRepository detailsRepository;


    @Autowired
    AbscondEmpServiceImpl abscondEmpServiceImpl;

 

    @Autowired
    ReleaseEmpDetailsRepository releaseEmpDetailsRepository;





  //1.select all designations
    @GetMapping("/get-all-designations")
    public ResponseEntity<List<Designations>> getAllDesgList() {
        List<Designations> Designations = abscondEmpServiceImpl.GetAllDesignation();

 

        List<Designations> filter = new ArrayList<Designations>();

 

        Pattern p = Pattern.compile("super admin", Pattern.CASE_INSENSITIVE);

        for (Designations de : Designations) {

 

            Matcher m = p.matcher(de.getDesgNames());


 

            if (m.matches()) {
                continue;

 

            }
            filter.add(de);


        }
 

        return new ResponseEntity<List<Designations>>(filter, org.springframework.http.HttpStatus.OK);
    }



        //2.select on emp by desg
    @GetMapping("/getByDesignationId/{des_id}")
    public ResponseEntity<List<MasterEmployeeDetails>> getEmployeesByDessignation(@PathVariable int des_id) {

 

        List<MasterEmployeeDetails> employeeDetails =  abscondEmpServiceImpl.GetEmp(des_id);

 

        return new ResponseEntity<List<MasterEmployeeDetails>>(employeeDetails, org.springframework.http.HttpStatus.OK);

 

    }



    //3. abscond emp
    @GetMapping("/AbscondEmp/{releaseDate}/{lancesoftId}")
    public synchronized ResponseEntity<?> abscondempDetails(@PathVariable String releaseDate,@PathVariable String lancesoftId,Principal principal) {
         abscondEmpServiceImpl.AbscondEmp(LocalDate.parse(releaseDate), lancesoftId, principal);
        return new ResponseEntity<>("Absconded Successfully", org.springframework.http.HttpStatus.CREATED);

 

    }




    //4.terminate emp
    @GetMapping("/TerminateEmp/{releaseDate}/{lancesoftId}")
    public synchronized ResponseEntity<?> terminateEmpDetails(@PathVariable String releaseDate,@PathVariable String lancesoftId,Principal principal) {
         abscondEmpServiceImpl.terminateEmp(LocalDate.parse(releaseDate), lancesoftId, principal);
        return new ResponseEntity<>(" Terminated Successfully", org.springframework.http.HttpStatus.CREATED);

 

    }



    @GetMapping("/ReleaseEmp/{releaseDate}/{lancesoftId}")
    public synchronized ResponseEntity<?> releaseEmpDetails(@PathVariable String releaseDate, @PathVariable String lancesoftId,Principal principal) {
    	
    	abscondEmpServiceImpl.releaseEmp( LocalDate.parse(releaseDate), lancesoftId, principal);
        return new ResponseEntity<>(" released Successfully", org.springframework.http.HttpStatus.CREATED);

 

    }
}
