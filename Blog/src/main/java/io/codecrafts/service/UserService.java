package io.codecrafts.service;

import io.codecrafts.model.User;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface UserService {
	User findUserById(UUID id);
	List<User> getAll();
	List<User> findAllInRange(int page, int numItems);
	User findUserByEmail(String email);
	User saveUser(User user);
	void updateUser(User user);
	void deleteUser(UUID id);
	List<User> searchByKeyword(String keyword, Pageable pageable);
}
