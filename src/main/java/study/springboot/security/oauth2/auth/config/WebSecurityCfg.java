package study.springboot.security.oauth2.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import study.springboot.security.oauth2.auth.filter.WatchDogFilter;

@Configuration
public class WebSecurityCfg extends WebSecurityConfigurerAdapter {

    @Autowired
    private WatchDogFilter watchDogFilter;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler loginSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler loginFailureHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * （★）HTTP请求安全
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //（▲）过滤器
        http.addFilterBefore(watchDogFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        //（▲）认证
        //Basic登录
//        http.httpBasic();
        //表单登录
        http.formLogin() //需要登录时，转到的登录页面
                .loginPage("/login.html") //登录跳转页面controller或页面
                .loginProcessingUrl("/doLogin") //登录表单提交地址
                .defaultSuccessUrl("/main.html", true) //默认登录成功url
                //.successForwardUrl("/") //登录成功跳转url
                //.successHandler(loginSuccessHandler) //登录成功处理器
                .failureUrl("/login.html?login_failure") //登录失败url，前端可通过url中是否有error来提供友好的用户登入提示
                //.failureForwardUrl() //登录失败跳转url
                //.failureHandler(loginFailureHandler) //登录失败处理器
                .usernameParameter("loginName")
                .passwordParameter("loginPwd")
                .permitAll()
        ;
        //（▲）授权
        http.authorizeRequests() //请求授权
                //.accessDecisionManager() //
//                .withObjectPostProcessor() //
//                .antMatchers("/view/**")
//                .permitAll() //不需要权限认证
                .anyRequest()  //任何请求
                .authenticated() //需要身份认证
        ;
        //（▲）
        http.exceptionHandling()
                .accessDeniedPage("/403") //无权页面
        //.accessDeniedHandler(accessDeniedHandler) //无权处理器
        //.authenticationEntryPoint() //认证入口
        ;
        //（▲）注销
        http.logout()
                .logoutUrl("/doLogout")  //注销url
                .logoutSuccessUrl("/login.html?logout_success") //注销成功url
        //.logoutSuccessHandler(logoutSuccessHandler) //注销成功处理器
        ;
        //（▲）会话
        http.sessionManagement()
                .invalidSessionUrl("/login.html?session_invalid") //
//                .invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false) //当达到最大值时，是否保留已经登录的用户
        //.sessionRegistry()
//                .expiredUrl("")
//                .expiredSessionStrategy(expiredSessionStrategy) //当达到最大值时，旧用户被踢出后的操作
        ;
        //（▲）其他
        http.csrf().disable() //关闭跨站请求防护
        ;
    }

    /**
     * （★）WEB安全
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**");
    }

    /**
     * （★）身份验证管理器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
