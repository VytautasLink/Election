package lt.itakademija;

/**
 * Created by Pavel on 2017-03-03.
 */
import lt.itakademija.users.UserDetailsServiceCustom;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceCustom service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, apiAdmin()).hasAnyAuthority("ROLE_ADMIN","ADMIN")
                .antMatchers(HttpMethod.DELETE, apiAdmin()).hasAnyAuthority("ROLE_ADMIN","ADMIN")
                .antMatchers(HttpMethod.PUT, apiAdmin()).hasAnyAuthority("ROLE_ADMIN","ADMIN")
                .antMatchers(HttpMethod.PATCH, apiAdmin()).hasAnyAuthority("ROLE_ADMIN","ADMIN")
                .antMatchers(HttpMethod.POST, apiRepresentative()).hasAnyAuthority("REPRESENTATIVE")
                .and()
                .formLogin()
                .loginPage("/login").successForwardUrl("/login/check").failureForwardUrl("/bad-credentials")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .logout()
                .permitAll();

        http.headers().frameOptions().disable();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
    }

    private String[] apiAdmin(){
        return new String[] {"/candidate/**","/county/**","/district/**","/party/**","/representative",
                "/result/**"};
    }

    private String[] apiRepresentative(){
        return new String[] {"/result-multi","/result-single"};
    }
}