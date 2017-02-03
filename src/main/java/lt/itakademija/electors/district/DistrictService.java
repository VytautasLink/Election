package lt.itakademija.electors.district;

import lt.itakademija.exceptions.DistrictCloneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Pavel on 2017-01-14.
 */
@Service
public class DistrictService {

    @Autowired
    DistrictRepository repository;

    public List<DistrictReport> getDistrictsList() {
        List<DistrictEntity> list = repository.findAll();
        return mappingToReport(list);
    }

    @Transactional
    public DistrictEntity save(DistrictEntity apylinke){
    	List<DistrictEntity> findByNameAndAdress = repository.findByNameAndAdress(apylinke);
		if (findByNameAndAdress.size() == 0) {
            return repository.save(apylinke);
        }
		else {
			if(findByNameAndAdress.get(0).getId()== apylinke.getId()){
				return repository.save(apylinke);
			}
		}
        throw new DistrictCloneException("This district is already registered");
    }

    @Transactional
    public boolean delete(Long id) {
        repository.delete(id);
        return true;
    }

    public List getDistrictsWithNullRepresentativesList() {
        List<DistrictEntity> list = repository.findAll()
                .stream()
                .filter(d -> d.getRepresentative() == null)
                .collect(Collectors.toList());
        return mappingToReport(list);
    }

	public DistrictReport getDistrictById(Long id) {
		DistrictEntity district = repository.findById(id);
		DistrictReport report = new DistrictReport(district);
		return report;
	}

    private List<DistrictReport> mappingToReport(List<DistrictEntity> list){
        return list.stream()
                .map(ent -> new DistrictReport(ent))
                .collect(Collectors.toList());
    }

	

}
