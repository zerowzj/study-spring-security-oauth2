package study.springsecurity.oauth2.auth.server.support;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@SpringBootApplication(scanBasePackages = "study.springsecurity.oauth2.auth.server")
public class SpringBootCfg {
}
