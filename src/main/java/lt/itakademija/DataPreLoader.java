package lt.itakademija;

import lt.itakademija.electors.candidate.CandidateEntity;
import lt.itakademija.electors.candidate.CandidateRepository;
import lt.itakademija.electors.candidate.CandidateService;
import lt.itakademija.electors.county.CountyEntity;
import lt.itakademija.electors.county.CountyRepository;
import lt.itakademija.electors.county.CountyService;
import lt.itakademija.electors.district.DistrictEntity;
import lt.itakademija.electors.district.DistrictRepository;
import lt.itakademija.electors.district.DistrictService;
import lt.itakademija.electors.party.PartyEntity;
import lt.itakademija.electors.party.PartyRepository;
import lt.itakademija.electors.party.PartyService;
import lt.itakademija.electors.representative.DistrictRepresentativeEntity;
import lt.itakademija.electors.representative.DistrictRepresentativeService;
import lt.itakademija.electors.results.multi.ResultMultiEntity;
import lt.itakademija.electors.results.multi.ResultMultiService;
import lt.itakademija.electors.results.multi.rating.RatingEntity;
import lt.itakademija.electors.results.single.ResultSingleEntity;
import lt.itakademija.electors.results.single.ResultSingleService;
import lt.itakademija.users.UserRoles;
import lt.itakademija.users.UsersEntity;
import lt.itakademija.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pavel on 2017-02-07.
 */
@Service
public class DataPreLoader {

    @Autowired
    CandidateService service;
    @Autowired
    CountyService countyService;
    @Autowired
    DistrictService districtService;
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    DistrictRepresentativeService districtRepresentativeService;
    @Autowired
    PartyService partyService;
    @Autowired
    ResultSingleService resultSingleService;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    CandidateService candidateService;
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    ResultMultiService resultMultiService;
    @Autowired
    UsersRepository usersRepository;

    public void createCounties() {
        System.out.println("Starting createCounties");
        String counties = "Naujamiesčio\n" +
                "Senamiessčio\n" +
                "Antakalnio\n" +
                "Žirmūnų\n" +
                "Fabijoniškių\n" +
                "Šeškinės\n" +
                "Justiniškių\n" +
                "Karoliniškių\n" +
                "Lazdynų\n" +
                "Naujosios Vilnios\n" +
                "Panerių\n" +
                "Verkių\n" +
                "Centro–Žaliakalnio\n" +
                "Šilainių\n" +
                "Kalniečių\n" +
                "Dainavos\n" +
                "Petrašiūnų\n" +
                "Panemunės\n" +
                "Aleksoto–Vilijampolės\n" +
                "Baltijos\n" +
                "Marių\n" +
                "Pajūrio\n" +
                "Danės\n" +
                "Saulės\n" +
                "Aušros\n" +
                "Nevėžio\n" +
                "Vakarinė\n" +
                "Aukštaitijos\n" +
                "Marijampolės\n" +
                "Alytaus\n" +
                "Gargždų\n" +
                "Šilutės\n" +
                "Pietų Žemaitijos\n" +
                "Tauragės\n" +
                "Plungės\n" +
                "Kretingos–Palangos\n" +
                "Kuršo\n" +
                "Mažeikių\n" +
                "Akmenės–Mažeikių\n" +
                "Telšių\n" +
                "Kelmės–Šiaulių\n" +
                "Raseinių–Kėdainių\n" +
                "Kėdainių\n" +
                "Radviliškio\n" +
                "Kuršėnų–Dainų\n" +
                "Žiemgalos\n" +
                "Pasvalio–Pakruojo\n" +
                "Biržų–Kupiškio\n" +
                "Anykščių–Panevėžio\n" +
                "Sėlos\n" +
                "Utenos\n" +
                "Visagino–Zarasų\n" +
                "Nalšios\n" +
                "Molėtų–Širvintų\n" +
                "Nemenčinės\n" +
                "Šalčininkų–Vilniaus\n" +
                "Medininkų\n" +
                "Trakų–Vievio\n" +
                "Kaišiadorių–Elektrėnų\n" +
                "Jonavos\n" +
                "Ukmergės\n" +
                "Jurbarko–Pagėgių\n" +
                "Sūduvos\n" +
                "Zanavykų\n" +
                "Raudondvario\n" +
                "Garliavos\n" +
                "Prienų–Birštono\n" +
                "Vilkaviškio\n" +
                "Dzūkijos\n" +
                "Varėnos–Trakų\n" +
                "Lazdijų–Druskininkų";
        final String[] countiesArr = counties.split("\n");
        for (String cou : countiesArr) {
            CountyEntity countyEntity = new CountyEntity();
            countyEntity.setName(cou);
            countyService.save(countyEntity);
        }
    }

    public void createRepresentatives() {
        System.out.println("Starting createRepresentatives");
        try {
            FileInputStream fis = new FileInputStream(new File("test-csv/big/names.txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            final BufferedReader bufferedReader = new BufferedReader(isr);
            final List<String> names = bufferedReader.lines().collect(Collectors.toList());
            final List<DistrictEntity> districtRepositoryAll = districtRepository.findAll();
            for (int i = 0; i < districtRepositoryAll.size(); i++) {
                if(districtRepositoryAll.get(i).getId() != 5){
                String name = names.get(new Random().nextInt(998) + 1) + "as";
                String surname = names.get(new Random().nextInt(998) + 1) + "evičius";
                saveRep(districtRepositoryAll.get(i), name, surname);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private DistrictRepresentativeEntity saveRep(DistrictEntity des, String name, String surname) {
        DistrictRepresentativeEntity rep = new DistrictRepresentativeEntity();
        rep.setName(name);
        rep.setSurname(surname);
        rep.setDistrict(des);
        districtRepresentativeService.save(rep);
        return rep;
    }

    private DistrictEntity saveDistrict(CountyEntity ces, String name, String adress) {
        DistrictEntity de = new DistrictEntity();
        de.setName(name);
        de.setCounty(ces);
        de.setAdress(adress);
        de.setNumberOfElectors(5000L);
        return districtService.save(de);
    }

    public void loadParties() {
        System.out.println("Starting loadParties");
        final MultipartFile file1 = parseToMultiPart("test-csv/big/big-party-100.csv");
        partyService.save("Statybininkų Partija", 1, file1);

        final MultipartFile file2 = parseToMultiPart("test-csv/big/p50-1.csv");
        partyService.save("Teisininkų Partija", 2, file2);

        final MultipartFile file3 = parseToMultiPart("test-csv/big/p50-2.csv");
        partyService.save("Batono Partija", 3, file3);

        final MultipartFile file4 = parseToMultiPart("test-csv/big/p50-3.csv");
        partyService.save("IT Partija", 4, file4);

        final MultipartFile file5 = parseToMultiPart("test-csv/big/p50-4.csv");
        partyService.save("Monopolistai", 5, file5);

        final MultipartFile file6 = parseToMultiPart("test-csv/big/p50-5.csv");
        partyService.save("Kedainių", 6, file6);

        final MultipartFile file7 = parseToMultiPart("test-csv/big/p50-6.csv");
        partyService.save("Harley Davidson", 7, file7);

        final MultipartFile file8 = parseToMultiPart("test-csv/big/p50-7.csv");
        partyService.save("Kolūkininkai", 8, file8);

        final MultipartFile file9 = parseToMultiPart("test-csv/big/p50-8.csv");
        partyService.save("Šventųjų partija", 9, file9);

        final MultipartFile file10 = parseToMultiPart("test-csv/big/p50-9.csv");
        partyService.save("Laisvės Partija", 10, file10);


    }

    private MultipartFile parseToMultiPart(String pathToFile) {
        Path path = Paths.get(pathToFile);
        String name = path.getFileName().toFile().getName();
        String originalFileName = path.getFileName().toFile().getName();
        String contentType = "multipart/form-data";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
        }
        return new MockMultipartFile(name, originalFileName, contentType, content);
    }

    public void createDistricts() {
        System.out.println("Starting createDistricts");
        try {
            FileInputStream fis = new FileInputStream(new File("test-csv/big/districts.txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            final BufferedReader bufferedReader = new BufferedReader(isr);
            final List<String> districtsFromFile = bufferedReader.lines().collect(Collectors.toList());
            for (int i = 0; i < districtsFromFile.size(); i++) {
                DistrictEntity districtEntity = new DistrictEntity();
                List<CountyEntity> allCounties = countyRepository.findAll();

                Long index = (long) ((i / 28.22));
                if (index >= 71) {
                    index = ((long) new Random().nextInt(70));
                }
                districtEntity.setName(districtsFromFile.get(i));
                districtEntity.setCounty(allCounties.get(index.intValue()));
                districtEntity.setNumberOfElectors(new Random().nextInt(5000) + 1000L);
                districtEntity.setAdress("Adresas g. 1");
                districtService.save(districtEntity);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void votesSingle() {
        System.out.println("Starting votes single");
        List<CandidateEntity> candidatesList = candidateRepository.getCandidatesList();
        Collections.shuffle(candidatesList);
        List<DistrictEntity> all = districtRepository.findAll();
        List<CountyEntity> countyAll = countyRepository.findAll();
        countyAll.forEach(c -> {
            List<CandidateEntity> cList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                candidatesList.get(i).setCounty(c);
                candidateService.save(candidatesList.get(i));
                cList.add(candidatesList.get(i));
            }
            candidatesList.removeAll(cList);
        });

        all.stream().forEach(dis -> {
            if (dis.getId() != 5L) {
                List<ResultSingleEntity> resultSingleEntities = new ArrayList<>();
                CandidateEntity spc = new CandidateEntity();
                spc.setId(-1991L);
                ResultSingleEntity spoiled = new ResultSingleEntity();
                spoiled.setCandidate(spc);
                spoiled.setVotes(10L);
                spoiled.setDistrict(dis);
                resultSingleEntities.add(spoiled);
                for (int i = 0; i < 10; i++) {
                    CandidateEntity candidateEntity = candidateRepository.findByCounty(dis.getCounty()).get(i);
                    ResultSingleEntity res = new ResultSingleEntity();
                    res.setDistrict(dis);
                    res.setCandidate(candidateEntity);
                    Integer round = Math.abs(Math.round(new Random().nextInt(500) * new Random().nextFloat() * new Random().nextFloat()));
                    res.setVotes(round.longValue());
                    res.setApproved(true);
                    resultSingleEntities.add(res);
                    res.setDatePublished(new Date());
                }
                resultSingleService.save(resultSingleEntities);
            }
        });
    }

    public void votesMulti() {
        System.out.println("Starting votes multi");
        List<DistrictEntity> districtsAll = districtRepository.findAll();
        List<PartyEntity> partyAll = partyRepository.findAll();
        districtsAll.stream().forEach(dis -> {
            if (dis.getId() != 5L) {
                List<Integer> singleResults = dis.getResultSingleEntity().stream().mapToInt(r -> r.getVotes().intValue()).boxed().collect(Collectors.toList());
                List<ResultMultiEntity> votes = new ArrayList<>();
                //Spoiled ballots
                ResultMultiEntity spoiled = new ResultMultiEntity();
                spoiled.setVotes(dis.getSpoiledSingle().longValue());
                PartyEntity sp = new PartyEntity();
                sp.setId(-1991L);
                spoiled.setParty(sp);
                spoiled.setDistrict(dis);
                votes.add(spoiled);

                partyAll.stream().forEach(par -> {
                    List<RatingEntity> ratings = new ArrayList<>();
                    Integer partyVotes = singleResults.get(partyAll.indexOf(par));
                    //ratings loop
                    for (int i = new Random().nextInt(20); i < (20 + new Random().nextInt(30)); i++) {
                        RatingEntity rating = new RatingEntity();
                        CandidateEntity cand = par.getMembers().get(i);
                        rating.setCandidate(cand);
                        rating.setPoints(new Random().nextInt(partyVotes.intValue() + 1));
                        ratings.add(rating);
                    }
                    votes.add(getResMulti(dis, par, partyVotes, ratings));
                });
                resultMultiService.save(votes);
            }
        });

    }

    private ResultMultiEntity getResMulti(DistrictEntity dis, PartyEntity par, long votes, List<RatingEntity> ratings) {
        ResultMultiEntity res = new ResultMultiEntity();
        res.setApproved(true);
        res.setDatePublished(new Date());
        res.setDistrict(dis);
        res.setParty(par);
        res.setVotes(votes);
        res.setRating(ratings);
        return res;
    }

    public void createNonPartyCandidates() {
        System.out.println("Starting createNonPartyCandidates");
        try {
            FileInputStream fis = new FileInputStream(new File("test-csv/big/names.txt"));
            InputStreamReader isr = new InputStreamReader(fis);
            final BufferedReader bufferedReader = new BufferedReader(isr);
            final List<String> names = bufferedReader.lines().collect(Collectors.toList());
            for (int i = 0; i < 200; i++) {
                String name = names.get(new Random().nextInt(998) + 1);
                String surname = names.get(new Random().nextInt(998) + 1) + "avičius";
                createCandidate(null, name, surname, "Single Mandate Cnadidate");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createCandidate(CountyEntity ces, String name, String surname, String desc) {
        CandidateEntity can = new CandidateEntity();
        can.setBirthDate(new Date());
        can.setName(name);
        can.setSurname(surname);
        can.setDescription(desc);
        can.setCounty(ces);
        can.setMultiList(false);
        service.save(can);
    }

    @Transactional
    public void createAdmin() {
        UsersEntity admin = new UsersEntity();
        admin.setPassword("admin");
        admin.setUsername("admin");
        UserRoles userRoles = new UserRoles();
        userRoles.setName("ADMIN");
        Set<UserRoles> r = new HashSet<>();
        r.add(userRoles);
        admin.setRoles(r);
        usersRepository.save(admin);
    }
}
