package ls.lesm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.AddressType;

public interface AddressTypeRepository extends JpaRepository<AddressType, Integer> {

	AddressType findByAddType(String addType);


}
