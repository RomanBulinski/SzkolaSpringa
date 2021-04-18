package rombuulean.buuleanBook.users.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rombuulean.buuleanBook.user.db.UserEntityRepository;
import rombuulean.buuleanBook.user.domain.UserEntity;
import rombuulean.buuleanBook.users.application.port.UserRegistrationUseCase;

@Service
@AllArgsConstructor
public class UserService implements UserRegistrationUseCase {

    private final UserEntityRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public RegisterResponse register(String username, String password) {
        if(repository.findByUsernameIgnoreCase(username).isPresent()){
            return  RegisterResponse.failure("Account already exist");
        }
        UserEntity entity = new UserEntity(username, encoder.encode(password));
        return RegisterResponse.success(repository.save(entity));
    }
}
