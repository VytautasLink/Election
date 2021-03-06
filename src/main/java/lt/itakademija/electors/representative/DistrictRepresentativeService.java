package lt.itakademija.electors.representative;

import lt.itakademija.electors.GeneralConditions;
import lt.itakademija.users.Md5;
import lt.itakademija.users.UserRoles;
import lt.itakademija.users.UsersEntity;
import lt.itakademija.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by Pavel on 2017-01-16.
 */
@Service
public class DistrictRepresentativeService {

    @Autowired
    DistrictRepresentativeRepository repository;

    @Autowired
    UsersService usersService;

    public List<DistrictRepresentativeReport> getAllRepresentatives() {
        List<DistrictRepresentativeReport> list = repository.findAll().stream()
                .map(ent -> {
                    DistrictRepresentativeReport rep = new DistrictRepresentativeReport();
                    rep.setId(ent.getId());
                    if (ent.getDistrict() == null) {
                        rep.setDistrictId(null);
                        rep.setDistrictName(null);
                    }else{
                        rep.setDistrictId(ent.getDistrict().getId());
                        rep.setDistrictName(ent.getDistrict().getName());
                    }
                    rep.setName(ent.getName());
                    rep.setSurname(ent.getSurname());
                    return rep;
                })
                .collect(Collectors.toList());
        return list;
    }

    @Transactional
    public DistrictRepresentativeCredentialsReport save(DistrictRepresentativeEntity representative) {
        int randomNum = ThreadLocalRandom.current().nextInt(100, 999 + 1);
        String username = representative.getName().toLowerCase() + representative.getSurname().toLowerCase()+randomNum;

        String password = Md5.generate(username).substring(0,8);
        UsersEntity user = new UsersEntity();
        user.setPassword(password);

        System.out.println("Username: " + username + " ,Password: " + password);
        user.setUsername(username);
        Set<UserRoles> roles = new HashSet<>();
        UserRoles role = new UserRoles();
        role.setName("REPRESENTATIVE");
        roles.add(role);
        user.setRoles(roles);
        user.setDistrictId(representative.getDistrict().getId().intValue());
        usersService.saveUser(user);
        repository.save(representative);
        return new DistrictRepresentativeCredentialsReport(username, password);
    }

    public Long findByName(String name) {
        return repository.getIdByName(name).longValue();
    }
}
