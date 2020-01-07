package info.svetlik.pia.service;

import java.util.List;

import info.svetlik.pia.domain.User;

public interface UserManager {

	List<User> getUsers();

	void addUser(String username, String password);
	
	void changePassword(String username, String password);
	
	void addUser(User user);
	
	void editUser(User user);
	
	void removeUser(User user);

	User getUser(Long id);

	void changePassword(User user, String passwordOld);

	User getUser(String name);

	public boolean passwordMatches(User user, String password);
}
