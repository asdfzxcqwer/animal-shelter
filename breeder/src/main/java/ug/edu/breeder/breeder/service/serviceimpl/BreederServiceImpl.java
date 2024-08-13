package ug.edu.breeder.breeder.service.serviceimpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ug.edu.breeder.breeder.exception.BreederException;
import ug.edu.breeder.breeder.exception.BreederExceptionMessage;
import ug.edu.breeder.breeder.persistance.Breeder;
import ug.edu.breeder.breeder.persistance.BreederRepository;
import ug.edu.breeder.breeder.service.BreederService;
import ug.edu.breeder.security.service.SecurityService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public
class BreederServiceImpl implements BreederService {
    private BCryptPasswordEncoder passwordEncoder;
    private final BreederRepository breederRepository;

    private final BreederMapper breederMapper;
    private final SecurityService securityService;

    public BreederServiceImpl(BCryptPasswordEncoder passwordEncoder, BreederRepository breederRepository, SecurityService securityService) {
        this.passwordEncoder = passwordEncoder;
        this.securityService = securityService;
        this.breederMapper = new BreederMapper();
        this.breederRepository = breederRepository;
    }

    @Override
    public BreederOutput loginBreeder(BreederLoginInput breederLoginInput) {
        Breeder breeder = checkBreederInput(breederLoginInput);
        securityService.autoLogin(breederLoginInput.email(), breederLoginInput.password());
        return breederMapper.mapToBreederOutput(breeder);
    }

    private Breeder checkBreederInput(BreederLoginInput breederLoginInput) {
        List<Breeder> breeder = breederRepository.findByEmail(breederLoginInput.email());
        if (!breeder.isEmpty()) {
            if (breeder.get(0).getEmail().equals(breederLoginInput.email()) &&
                    breeder.get(0).getPassword().equals(breederLoginInput.password())) {
            }
        } else {
            throw new BreederException(BreederExceptionMessage.PASSWORD_OR_EMAIL_DO_NOT_MATCH.getMessage());
        }
        return breeder.get(0);
    }

    @Override
    public BreederOutput registerBreeder(BreederRegistrationInput breederRegistrationInput) {
        validateBreederRegistration(breederRegistrationInput);
        Breeder breeder = breederMapper.mapToBreederFromRegistration(breederRegistrationInput);
        breeder.setPassword(passwordEncoder.encode(breederRegistrationInput.password()));
        breederRepository.save(breeder);
        securityService.autoLogin(breederRegistrationInput.email(), breederRegistrationInput.password());
        return breederMapper.mapToBreederOutput(breeder);
    }

    private void validateBreederRegistration(BreederRegistrationInput breederRegistrationInput) {
        if (!patternMatchesEmail(breederRegistrationInput.email())) {
            throw new BreederException(BreederExceptionMessage.EMAIL_INVALID.getMessage());
        }
        if (!breederRepository.findByEmail(breederRegistrationInput.email()).isEmpty()) {
            throw new BreederException(BreederExceptionMessage.EMAIL_ALREADY_REGISTERED.getMessage());
        }
        if (!isValidPassword(breederRegistrationInput.password())) {
            throw new BreederException(BreederExceptionMessage.PASSWORD_INVALID.getMessage());
        }
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

    @Override
    public void deregisterBreeder(Long id) {
        boolean exists = breederRepository.existsById(id);
        if(!exists){
            throw new BreederException(BreederExceptionMessage.BREEDER_DOES_NOT_EXIST.getMessage());
        }
        breederRepository.deleteById(id);
    }
}
