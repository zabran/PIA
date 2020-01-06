package info.svetlik.pia.pia;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import info.svetlik.pia.dao.RoleRepository;
import info.svetlik.pia.domain.Role;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class RoleRepoTest {

	@Autowired
	private RoleRepository roleRepo;

	@Test
	@Transactional
	public void testUserManipulation() {
		log.info("Testing user manipulation.");
		Assertions.assertEquals(4, roleRepo.count());
		log.info("Looking for admin.");
		Role role = roleRepo.findByCode("ADMIN");
		Assertions.assertNotNull(role);
		log.info("Found ADMIN.");
		Assertions.assertEquals(1, role.getUsers().size());
		log.info("ADMIN is one, removing.");
		role.getUsers().remove(0);
		roleRepo.save(role);
		role = roleRepo.findByCode("ADMIN");
		Assertions.assertEquals(0, role.getUsers().size());
		log.info("Admin has no role now, OK.");
		
		roleRepo.save(new Role("test","test"));
		Assertions.assertEquals(5, roleRepo.count());
		role = roleRepo.findByCode("test");
		Assertions.assertNotNull(role);
		log.info("Added and found test.");
		roleRepo.delete(role);
		Assertions.assertEquals(4, roleRepo.count());
		log.info("Role test is no more.");
	}


}
