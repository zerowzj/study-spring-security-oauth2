package study.springsecurity.oauth2.auth.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import study.springsecurity.oauth2.auth.server.auth.token.CustomTokenStore;

import java.util.concurrent.TimeUnit;

@Configuration
public class AuthorizationServerCfg extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CustomTokenStore tokenStore;

    /**
     * 配置授权服务器的安全，意味着实际上是/oauth/token端点
     * /oauth/authorize端点也应该是安全的
     * 默认的设置覆盖到了绝大多数需求，所以一般情况下你不需要做任何事情
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients() //
                .tokenKeyAccess("permitAll()") //
                .checkTokenAccess("isAuthenticated()") //
                .passwordEncoder(NoOpPasswordEncoder.getInstance()) //密码编码器
        ;
    }

    /**
     * 配置ClientDetailsService
     * 注意，除非你在configure(AuthorizationServerEndpointsConfigurer endpoints)中指定了一个AuthenticationManager，否则密码授权方式不可用
     * 至少配置一个client，否则服务器将不会启动
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //clients.withClientDetails(clientDetailsService);
        clients.inMemory()
                .withClient("client_id")
                .secret("secret") //密钥
                .authorizedGrantTypes("authorization_code", "client_credentials", "password", "refresh_token") //认证类型
                .scopes("all")
        //.authorities("oauth2")
        //.redirectUris("") //
        //.autoApprove("") //
        //.autoApprove(false)
        //.accessTokenValiditySeconds(60 * 1000) //
        //.refreshTokenValiditySeconds(60 * 1000) //
        ;
    }


    /**
     * 该方法是用来配置Authorization Server endpoints的一些非安全特性的，比如token存储、token自定义、授权类型等等的
     * 默认情况下，你不需要做任何事情，除非你需要密码授权，那么在这种情况下你需要提供一个AuthenticationManager
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //
//        DefaultTokenServices tokenServices = new DefaultTokenServices();
//        tokenServices.setTokenStore(endpoints.getTokenStore());
//        tokenServices.setSupportRefreshToken(false);
//        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
//        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
//        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.MINUTES.toSeconds(10));

        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST) //允许GET、POST请求获取token，即访问端点：oauth/token
                .authenticationManager(authenticationManager) //端点认证管理器
                //.userDetailsService(userDetailsService) //
                .tokenStore(new InMemoryTokenStore()) //token存储
        //.tokenServices(tokenServices)
        //.userApprovalHandler(null)
        //.authorizationCodeServices(null)
        ;
    }
}
