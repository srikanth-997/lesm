package ls.lesm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.User;
import ls.lesm.payload.request.SignUpRequest;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);

	public void deleteByUsername(String username);

	public User getUserByEmail(String email);

	public User findByEmail(String email);

	public User findByPhoneNo(String phoneNo);
	
	//public List<User> findByRoleName(String string);
	
	public Page<User> findByRoleName(String string, Pageable pageable);

	public User findByUserId(String loggedInUser);

	//public SignUpRequest findByUsername(String username);
	//@Query("SELECT user FROM User user LEFT JOIN user.roles role WHERE role.id = ?1")
    //List<User> findUserByRole(int role);
}
