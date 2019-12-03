package study.springsecurity.oauth2.auth.server.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityCfg extends WebSecurityConfigurerAdapter {

    /**
     * （★）HTTP请求安全
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
        ;
    }

    /**
     * （★）WEB安全
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
