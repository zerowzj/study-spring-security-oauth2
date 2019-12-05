package study.springsecurity.oauth2.auth.server.auth.details;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("loadUserByUsername[{}]", username);
//        if(!Strings.isNullOrEmpty(username)){
//            throw new UsernameNotFoundException("用户名不能为空");
//        }
        CustomUserDetails userDetails = new CustomUserDetails("wzj", "123");
        return userDetails;
    }
}
