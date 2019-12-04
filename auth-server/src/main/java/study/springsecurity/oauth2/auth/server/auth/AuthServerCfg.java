package study.springsecurity.oauth2.auth.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import study.springsecurity.oauth2.auth.server.auth.token.CustomTokenStore;

import java.util.concurrent.TimeUnit;

@Configuration
public class AuthServerCfg extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private CustomTokenStore tokenStore;

    /**
     * Client配置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.withClientDetails(clientDetailsService);
        clients.inMemory()
                .withClient("client_id")
                .secret("secret") //密钥
                .authorizedGrantTypes("authorization_code", "client_credentials", "password", "refresh_token") //认证类型
                .scopes("all")
//                .authorities("oauth2")
        ;
    }

    /**
     * 认证服务安全配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()") //
                .checkTokenAccess("isAuthenticated()") //
                .allowFormAuthenticationForClients() //
                .passwordEncoder(NoOpPasswordEncoder.getInstance()) //密码编码器
        ;
    }

    /**
     * 认证服务端点配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(false);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(10));

        endpoints.authenticationManager(authenticationManager) //端点认证管理器
                //.userDetailsService(userDetailsService) //
                //.tokenStore(tokenStore) //token存储
                //.tokenServices(tokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST) //允许GET、POST请求获取token，即访问端点：oauth/token
        ;
    }
}
