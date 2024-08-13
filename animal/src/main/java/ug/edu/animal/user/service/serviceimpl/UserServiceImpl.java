package ug.edu.animal.user.service.serviceimpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ug.edu.animal.security.service.SecurityService;
import ug.edu.animal.user.exception.UserException;
import ug.edu.animal.user.exception.UserExceptionMessage;
import ug.edu.animal.user.persistance.User;
import ug.edu.animal.user.persistance.UserRepository;
import ug.edu.animal.user.service.UserService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final SecurityService securityService;
    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SecurityService securityService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserOutput loginUser(UserLoginInput userLoginInput) {
        User user = checkUserInput(userLoginInput);
        securityService.autoLogin(userLoginInput.email(), userLoginInput.password());
        return userMapper.mapToUserOutput(user);
    }



    @Override
    public UserOutput registerUser(UserRegistrationInput userRegistrationInput) {
        validateUserRegistration(userRegistrationInput);
        User user = userMapper.mapToUserFromRegistration(userRegistrationInput);
        user.setPassword(passwordEncoder.encode(userRegistrationInput.password()));
        userRepository.save(user);
        securityService.autoLogin(userRegistrationInput.email(), userRegistrationInput.password());
        return userMapper.mapToUserOutput(user);
    }

    private void validateUserRegistration(UserRegistrationInput userRegistrationInput) {
        if (!patternMatchesEmail(userRegistrationInput.email())) {
            throw new UserException(UserExceptionMessage.EMAIL_INVALID.getMessage());
        }
        if (!userRepository.findByEmail(userRegistrationInput.email()).isEmpty()) {
            throw new UserException(UserExceptionMessage.EMAIL_ALREADY_REGISTERED.getMessage());
        }
        if (!isValidPassword(userRegistrationInput.password())) {
            throw new UserException(UserExceptionMessage.PASSWORD_INVALID.getMessage());
        }
    }

    private User checkUserInput(UserLoginInput userLoginInput) {
        List<User> user = userRepository.findByEmail(userLoginInput.email());
        if (!user.isEmpty()) {
            if (user.get(0).getEmail().equals(userLoginInput.email()) &&
                    user.get(0).getPassword().equals(userLoginInput.password())) {
            }
        } else {
            throw new UserException(UserExceptionMessage.PASSWORD_OR_EMAIL_DO_NOT_MATCH.getMessage());
        }
        return user.get(0);
    }
    private static boolean patternMatchesEmail(String emailAddress) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(emailAddress)
                .matches();
    }

    public boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*?\\d)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[^\\da-zA-Z]).*$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
