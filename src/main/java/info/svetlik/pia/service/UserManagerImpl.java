package info.svetlik.pia.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import info.svetlik.pia.dao.RoleRepository;
import info.svetlik.pia.dao.UserRepository;
import info.svetlik.pia.domain.Role;
import info.svetlik.pia.domain.User;
import info.svetlik.pia.model.WebCredentials;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserManagerImpl implements UserManager, UserDetailsService {

	private static final String DEFAULT_USER = "admin";
	private static final String DEFAULT_PASSWORD = "default";

	private final PasswordEncoder encoder;

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;

	@Autowired
	public UserManagerImpl(PasswordEncoder encoder, UserRepository userRepo, RoleRepository roleRepo) {
		this.encoder = encoder;
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
	}

	@Override
	public List<User> getUsers() {
		List<User> retVal = new LinkedList<>();
		for (User user : this.userRepo.findAll()) {
			retVal.add(user);
		}
		return Collections.unmodifiableList(retVal);
	}

	@Override
	public void addUser(String username, String password) {
		if (this.userRepo.findByUsername(username) != null) {
			throw new IllegalArgumentException("User already exists!");
		}

		String hashed = this.encoder.encode(password);
		User user = new User(username, hashed);
		this.userRepo.save(user);
	}
	
	

	@EventListener(classes = {
			ContextRefreshedEvent.class
	})
	@Order(2)
	@Transactional
	public void setup() {
		if (this.userRepo.count() == 0) {
			log.info("No user present, creating admin.");
			this.addUser(DEFAULT_USER, DEFAULT_PASSWORD);
			User user = this.userRepo.findByUsername(DEFAULT_USER);
			Role role = this.roleRepo.findByCode("ADMIN");
			user.getRoles().add(role);
			this.userRepo.save(user);
		}
		if(!this.userRepo.findByUsername(DEFAULT_USER).getPassword().equals(this.encoder.encode(DEFAULT_PASSWORD)))
			changePassword(DEFAULT_USER, DEFAULT_PASSWORD);
	}

	private String toSpringRole(Role role) {
		return "ROLE_" + role.getCode();
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username!");
		}

		WebCredentials creds = new WebCredentials(user.getUsername(), user.getPassword());

		user.getRoles()
		.stream()
		.map(this::toSpringRole)
		.forEach(creds::addRole);

		return creds;
	}

	@Override
	public void changePassword(String username, String password) {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid session!");
		}
		user.setPassword(this.encoder.encode(password));
		this.userRepo.save(user);
	}

	@Override
	public void addUser(User user) {
		if(user.getPassword().equals("")) {
			throw new IllegalArgumentException("Password cannot be empty!");
		}
		
		if (this.userRepo.findByUsername(user.getUsername()) != null) {
			throw new IllegalArgumentException("User already exists!");
		}
		
		checkUserOK(user);

		String hashed = this.encoder.encode(user.getPassword());
		user.setPassword(hashed);
		this.userRepo.save(user);
		
	}
	
	public void checkUserOK(User user) {
		if(user.getUsername().equals("")) {
			throw new IllegalArgumentException("Username cannot be empty!");
		}
		
		if(user.getRoles().isEmpty()) {
			log.info("No role found, adding USER.");
			List<Role> roles = new LinkedList<Role>();
			roles.add(this.roleRepo.findByCode("USER"));
			user.setRoles(roles);
		}
		else if(user.getRoles().contains(this.roleRepo.findByCode("USER"))&& user.getRoles().size()>1) {
			log.info("Found role USER along with other roles. Removing USER.");
			List<Role> roles = user.getRoles();
			roles.remove(this.roleRepo.findByCode("USER"));
			user.setRoles(roles);
		}
	}
	
	public void editUser(User user) {
		checkUserOK(user);
		
		User old = this.userRepo.findById(user.getId()).get();
		if (this.userRepo.findByUsername(user.getUsername()) != null && !old.getUsername().equals(user.getUsername())) {
			throw new IllegalArgumentException("There is already user with same username!");
		}
		
		if(!user.getPassword().equals(""))
		{
			String hashed = this.encoder.encode(user.getPassword());
			user.setPassword(hashed);
		}
		else user.setPassword(old.getPassword());
		this.userRepo.save(user);
	}
	
	public void removeUser(User user) {
		User old = this.userRepo.findById(user.getId()).get();
		this.userRepo.delete(old);
	}
	
	public User getUser(Long id) {
		return this.userRepo.findById(id).get();
	}

	@Override
	public void changePassword(User user, String passwordOld) {
		if(user.getPassword().equals(""))
		{
			throw new IllegalArgumentException("Password cannot be empty!");
		}
		User old = this.userRepo.findByUsername(user.getUsername());
		
		if(!encoder.matches(passwordOld, old.getPassword())) {
			throw new IllegalArgumentException("Wrong old password!");
		}
		
		String hashed = this.encoder.encode(user.getPassword());
		user = old;
		user.setPassword(hashed);
		this.userRepo.save(user);
	}

	@Override
	public User getUser(String name) {
		return this.userRepo.findByUsername(name);
	}

}
