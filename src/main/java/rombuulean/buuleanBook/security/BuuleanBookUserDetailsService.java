package rombuulean.buuleanBook.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import rombuulean.buuleanBook.user.db.UserEntityRepository;

@AllArgsConstructor
public class BuuleanBookUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsernameIgnoreCase(username)
                .map( x -> new UserEntityDetails(x))
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }
}
