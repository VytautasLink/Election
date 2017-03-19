package lt.itakademija.electors;

import lt.itakademija.Application;
import lt.itakademija.electors.candidate.CandidateControllerTest;
import lt.itakademija.users.UserDetailsServiceCustom;
import lt.itakademija.users.UsersEntity;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContextTestExcecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Pavel on 2017-03-15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {Application.class})
@ContextConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExcecutionListener.class})
@WithMockUser(username="admin",roles={"ADMIN","REPRESENTATIVE"})
public class SecurityTest{

    @Test
    public void connectTest(){
        Assert.assertThat("admin", CoreMatchers.is(SecurityContextHolder.getContext().getAuthentication().getName()));
        List<String> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(d -> d.getAuthority()).collect(Collectors.toList());
        roles.forEach(System.out::println);
        Assert.assertThat(roles.contains("ROLE_ADMIN"),CoreMatchers.is(true));
    }
}
