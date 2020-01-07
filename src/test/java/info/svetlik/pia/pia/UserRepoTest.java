package info.svetlik.pia.pia;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import info.svetlik.pia.dao.RoleRepository;
import info.svetlik.pia.dao.UserRepository;
import info.svetlik.pia.domain.Role;
import info.svetlik.pia.domain.User;
import info.svetlik.pia.service.UserManager;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UserRepoTest {

	@Autowired
	private UserRepository userRepo;
	
	
	@Autowired
	private UserManager userMana;
	
	
	
	/*---------green scenerio-------------*/

	@Test
	@Transactional
	public void testUserManipulation() {
		log.info("Testing user manipulation.");
		Assertions.assertEquals(1, userRepo.count());
		log.info("Looking for admin.");
		User user = userRepo.findByUsername("admin");
		Assertions.assertNotNull(user);
		log.info("Found admin.");
		Assertions.assertEquals(1, user.getRoles().size());
		log.info("Admin has one role, removing.");
		user.getRoles().remove(0);
		userRepo.save(user);
		user = userRepo.findByUsername("admin");
		Assertions.assertEquals(0, user.getRoles().size());
		log.info("Admin has no role now, OK.");
	}
	
	
	@Test
	public void testSession() {
		log.info("Testing user manipulation.");
		Assertions.assertEquals(1, userRepo.count());
		log.info("Looking for admin.");
		User user = userRepo.findByUsername("admin");
		Assertions.assertNotNull(user);
		log.info("Found admin.");
		//Assertions.assertThrows(LazyInitializationException.class, user::getRoles);
	}
	
	public void addTestUser() {
		User user = new User();
		user.setEmail("mail");
		user.setPassword("heslo");
		user.setUsername("user");
		userMana.addUser(user);
	}
	
	@Test
	public void addUser() {
		addTestUser();
		
		
		User user2 = userMana.getUser("user");
		log.info("Testing user e-mail.");
		Assertions.assertEquals(user2.getEmail(), "mail");
		log.info("Testing user password.");
		Assertions.assertTrue(userMana.passwordMatches(user2, "heslo"));
		log.info("Testing username.");
		Assertions.assertEquals(user2.getUsername(), "user");
		//Lazy
		/*log.info("Testing user role count.");
		Assertions.assertEquals(1, user2.getRoles().size());
		log.info("Testing user role name.");
		Assertions.assertEquals("user", user2.getRoles().get(0).getName());
		log.info("Testing user role code.");
		Assertions.assertEquals("USER", user2.getRoles().get(0).getCode());*/
		log.info("Testing user counts.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		userRepo.deleteById(user2.getId());
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	@Test
	public void editUser() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("user2");
		user2.setEmail("email");
		user2.setPassword("new");
		//user2.setRoles(userRepo.findByUsername("admin").getRoles());
		userMana.editUser(user2);
		
		User user3 = userMana.getUser(id);
		log.info("Testing user e-mail.");
		Assertions.assertEquals(user3.getEmail(), "email");
		log.info("Testing user password.");
		Assertions.assertTrue(userMana.passwordMatches(user3, "new"));
		log.info("Testing username.");
		Assertions.assertEquals(user3.getUsername(), "user2");
		//LAZY
		/*log.info("Testing user role count.");
		Assertions.assertEquals(1, user3.getRoles().size());
		log.info("Testing user role name.");
		Assertions.assertEquals("admin", user3.getRoles().get(0).getName());
		log.info("Testing user role code.");
		Assertions.assertEquals("ADMIN", user3.getRoles().get(0).getCode());*/
		log.info("Testing old user.");
		Assertions.assertNull(userMana.getUser("user"));
		log.info("Testing user count.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		userRepo.deleteById(id);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	@Test
	public void changePassword() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("user");
		user2.setEmail("mail");
		user2.setPassword("new");
		userMana.changePassword(user2, "heslo");
		
		User user3 = userMana.getUser(id);
		log.info("Testing user e-mail.");
		Assertions.assertEquals(user3.getEmail(), "mail");
		log.info("Testing user password.");
		Assertions.assertTrue(userMana.passwordMatches(user3, "new"));
		log.info("Testing username.");
		Assertions.assertEquals(user3.getUsername(), "user");
		//LAZY
		/*log.info("Testing user role count.");
		Assertions.assertEquals(1, user3.getRoles().size());
		log.info("Testing user role name.");
		Assertions.assertEquals("user", user3.getRoles().get(0).getName());
		log.info("Testing user role code.");
		Assertions.assertEquals("USER", user3.getRoles().get(0).getCode());*/
		log.info("Testing user count.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		User user = userRepo.findByUsername("user");
		userRepo.delete(user);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	/*------------negative tests---------------*/
	
	/*--------addUser----------*/
	@Test
	public void addUserUserExists() {
		addTestUser();

		log.info("Testing user exists error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> addTestUser(), "User already exists!");
		log.info("Testing user counts.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		User user = userRepo.findByUsername("user");
		userRepo.delete(user);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	@Test
	public void addUserUsernameEmpty() {
		User user = new User();
		user.setUsername("");
		user.setEmail("mail");
		user.setPassword("heslo");
		
		log.info("Testing username empty error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> userMana.addUser(user), "Username cannot be empty!");
	}
	
	@Test
	public void addUserPasswordEmpty() {
		User user = new User();
		user.setEmail("mail");
		user.setUsername("user");
		user.setPassword("");
		
		log.info("Testing password empty error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> userMana.addUser(user), "Password cannot be empty!");
	}
	
	/*--------editUser----------*/

	@Test
	public void editUserUserExists() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("admin");
		
		
		log.info("Testing user exists error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> userMana.editUser(user2), "There is already user with same username!");
		log.info("Testing user counts.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		userRepo.deleteById(id);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	@Test
	public void editUserUsernameEmpty() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("");
		
		
		log.info("Testing username empty error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> userMana.editUser(user2), "Username cannot be empty!");
		log.info("Testing user counts.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		userRepo.deleteById(id);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	@Test
	public void editUserPasswordEmpty() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("user");
		user2.setEmail("mail");
		user2.setPassword("");
		//user2.setRoles(userRepo.findByUsername("admin").getRoles());
		userMana.editUser(user2);
		
		User user3 = userMana.getUser(id);
		log.info("Testing user e-mail.");
		Assertions.assertEquals(user3.getEmail(), "mail");
		log.info("Testing user password.");
		Assertions.assertTrue(userMana.passwordMatches(user3, "heslo"));
		log.info("Testing username.");
		Assertions.assertEquals(user3.getUsername(), "user");
		//lazy
		/*log.info("Testing user role count.");
		Assertions.assertEquals(1, user3.getRoles().size());
		log.info("Testing user role name.");
		Assertions.assertEquals("admin", user3.getRoles().get(0).getName());
		log.info("Testing user role code.");
		Assertions.assertEquals("ADMIN", user3.getRoles().get(0).getCode());*/
		log.info("Testing user count.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		userRepo.deleteById(id);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	/*-----changePassword------*/
	
	@Test
	public void changePasswordWrongPassword() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("user");
		user2.setEmail("mail");
		user2.setPassword("new");
		
		log.info("Testing wrong password error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> userMana.changePassword(user2, ""), "Wrong old password!");
		
		
		log.info("Testing user count.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		User user = userRepo.findByUsername("user");
		userRepo.delete(user);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
	
	@Test
	public void changePasswordEmptyPassword() {
		addTestUser();
		
		User old = userMana.getUser("user");
		Long id = old.getId();
		User user2 = new User();
		user2.setId(id);
		user2.setUsername("user");
		user2.setEmail("mail");
		user2.setPassword("");
		
		log.info("Testing empty password error.");
		Assertions.assertThrows(IllegalArgumentException.class,() -> userMana.changePassword(user2, "heslo"), "Password cannot be empty!");
		
		
		log.info("Testing user count.");
		Assertions.assertEquals(2,userMana.getUsers().size());
		User user = userRepo.findByUsername("user");
		userRepo.delete(user);
		Assertions.assertEquals(1,userMana.getUsers().size());
	}
}
