package ug.edu.animal.user.service;

import ug.edu.animal.user.service.serviceimpl.UserLoginInput;
import ug.edu.animal.user.service.serviceimpl.UserOutput;
import ug.edu.animal.user.service.serviceimpl.UserRegistrationInput;

public interface UserService {

    UserOutput loginUser(UserLoginInput userLoginInput);

    UserOutput registerUser(UserRegistrationInput userRegistrationInput);

}
