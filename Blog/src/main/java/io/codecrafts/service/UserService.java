package io.codecrafts.service;

import io.codecrafts.model.User;

public interface UserService {
	public User findUserByEmail(String email);
	public void saveUser(User user);
}
