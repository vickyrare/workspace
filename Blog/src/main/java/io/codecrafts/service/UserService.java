package io.codecrafts.service;

import io.codecrafts.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
	public User findUserById(UUID id);
	public List<User> getAll();
	public List<User> findAllInRange(int page, int numItems);
	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void updateUser(User user);
	public void deleteUser(UUID id);
}
