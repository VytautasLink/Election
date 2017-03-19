package lt.itakademija.electors.results;

import lt.itakademija.electors.GeneralConditions;
import lt.itakademija.electors.candidate.CandidateEntity;
import lt.itakademija.electors.candidate.CandidateReport;
import lt.itakademija.electors.county.CountyEntity;
import lt.itakademija.electors.county.CountyReport;
import lt.itakademija.electors.county.CountyRepository;
import lt.itakademija.electors.district.DistrictEntity;
import lt.itakademija.electors.district.DistrictRepository;
import lt.itakademija.electors.party.PartyEntity;
import lt.itakademija.electors.party.PartyReport;
import lt.itakademija.electors.party.PartyRepository;
import lt.itakademija.electors.results.multi.ResultMultiEntity;
import lt.itakademija.electors.results.multi.ResultMultiRepository;
import lt.itakademija.electors.results.multi.rating.RatingEntity;
import lt.itakademija.electors.results.multi.rating.RatingRepository;
import lt.itakademija.electors.results.reports.ResultCountyReport;
import lt.itakademija.electors.results.reports.ResultDistrictReport;
import lt.itakademija.electors.results.reports.ResultsGeneralReport;
import lt.itakademija.electors.results.reports.dto.CandidateIntDTO;
import lt.itakademija.electors.results.reports.dto.PartyIntDTO;
import lt.itakademija.electors.results.reports.dto.StringLongDTO;
import lt.itakademija.electors.results.single.ResultSingleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pavel on 2017-02-13.
 */
@Service
public class ResultsService {
    private ResultsGeneralReport generalReport;
    private List<ResultCountyReport> finishVotingCountyReports = new ArrayList<>();

    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    ResultMultiRepository resultMultiRepository;
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    RatingRepository ratingRepository;

    public ResultsGeneralReport getGeneralReport() {
        if (generalReport == null) {
            System.out.println("1");
            return formGeneralResults();
        }
        System.out.println("2");
        return generalReport;
    }

    public void setGeneralReport(ResultsGeneralReport generalReport) {
        this.generalReport = generalReport;
    }

    public void saveCountyResults(Long id) {
        final CountyEntity county = countyRepository.findById(id);
        int size = county.getDistricts().size();
        if (getNumDistrictsVotedMulti(county) == size && getNumDistrictsVotedSingle(county) == size) {
            ResultCountyReport countyResults = getCountyResults(id);
            finishVotingCountyReports.add(countyResults);
        }
    }

    public ResultCountyReport formOrGetCountyResults(Long id) {
        boolean hasReport = finishVotingCountyReports.stream().anyMatch(r -> r.getCounty().getId().equals(id));
        if (hasReport) {
            System.out.println("Found results for county " + id);
            return finishVotingCountyReports.stream().filter(r -> r.getCounty().getId().equals(id)).findFirst().get();
        } else {
            System.out.println("Generating results for county " + id);
            return getCountyResults(id);
        }
    }

    public ResultDistrictReport getDistrictResults(Long id) {
        final DistrictEntity district = districtRepository.findById(id);
        return new ResultDistrictReport(district);
    }

    private ResultCountyReport getCountyResults(final Long id) {
        final CountyEntity county = countyRepository.findById(id);
        ResultCountyReport report = new ResultCountyReport();

        int votersCount = county.getDistricts().stream().mapToInt(d -> d.getNumberOfElectors().intValue()).sum();
        report.setVotersCount(votersCount);

        int validCount = county.getDistricts()
                .stream()
                .filter(f -> f.getResultMultiEntity().size() != 0)
                .mapToInt(d -> d.getResultMultiEntity()
                        .stream()
                        .mapToInt(r -> r.getVotes().intValue())
                        .sum()
                        + d.getSpoiledMulti())
                .sum();
        report.setValidCount(validCount);

        int spoiledSingle = getSpoiledSingle(county);
        report.setSpoiledSingle(spoiledSingle);
        report.setSpoiledSingle(spoiledSingle);

        int spoiledMulti = getSpoiledMulti(county);
        report.setSpoiledMulti(spoiledMulti);

        Long districtsVotedSingle = getNumDistrictsVotedSingle(county);
        report.setDistrictsVotedSingle(districtsVotedSingle.intValue());

        Long districtsVotedMulti = getNumDistrictsVotedMulti(county);
        report.setDistrictsVotedMulti(districtsVotedMulti.intValue());

        report.setCounty(new CountyReport(county));
        report.setDistrictsCount(county.getDistricts().size());

        //single
        List<ResultSingleEntity> allSingleResults = getCountySingleResultList(county);
        Set<CandidateIntDTO> candidatesIntDtoSet = getCandidatesIntDtoSet(allSingleResults);
        List<CandidateIntDTO> candidatesResultList = getCandidatesResults(allSingleResults, candidatesIntDtoSet);
        report.setVotesByCandidate(candidatesResultList);
        if (!candidatesResultList.isEmpty()) {
            report.setSingleMandateWinner(candidatesResultList.get(0).getCandidate());
        }

        //multi
        List<ResultMultiEntity> countyMultiResults = getCountyMultiResults(county);
        List<RatingEntity> countyRatings = getCountyRatings(countyMultiResults);
        List<PartyIntDTO> partyIntDTOList = getPartyIntDtoList(countyMultiResults);
        List<PartyIntDTO> partiesVotesSummary = getPartiesVotesSummary(countyMultiResults, countyRatings, partyIntDTOList);

        for (PartyIntDTO partyIntDTO : partiesVotesSummary) {
            List<CandidateIntDTO> newRatings = new ArrayList<>();
            for (RatingEntity countyRating : countyRatings) {
                if (countyRating.getCandidate().getPartyDependencies().getId().equals(partyIntDTO.getPar().getId())) {
                    newRatings.add(new CandidateIntDTO(countyRating.getCandidate(), countyRating.getPoints()));
                }
            }
            Map<CandidateReport, List<CandidateIntDTO>> collect = newRatings
                    .stream()
                    .collect(Collectors.groupingBy(CandidateIntDTO::getCandidate));
            List<CandidateIntDTO> ratings = new ArrayList<>();
            collect.forEach((key, value) -> {
                ratings.add(new CandidateIntDTO(key, value
                        .stream()
                        .mapToInt(CandidateIntDTO::getVotes)
                        .sum()));
            });

            List<CandidateIntDTO> sortedRatings = ratings
                    .stream()
                    .sorted((ra1, ra2) -> ra2.getVotes().compareTo(ra1.getVotes()))
                    .collect(Collectors.toList());
            partyIntDTO.setRatings(sortedRatings);
        }
        report.setVotesByParty(partiesVotesSummary);

        List<PartyIntDTO> partyIntDTOS = sortMemberByRatingAndPartyNumber(report);
        report.setReorderedPartyMembersParties(partyIntDTOS);

        return report;
    }

    private int getSpoiledMulti(CountyEntity county) {
        return county.getDistricts()
                .stream()
                .filter(d -> d.getSpoiledMulti() != null)
                .filter(d -> d.getResultMultiEntity().get(0).isApproved())
                .mapToInt(d -> d.getSpoiledMulti())
                .sum();
    }

    private int getSpoiledSingle(CountyEntity county) {
        return county.getDistricts()
                .stream()
                .filter(d -> d.getSpoiledSingle() != null)
                .filter(d -> d.getResultSingleEntity().get(0).isApproved())
                .mapToInt(d -> d.getSpoiledSingle())
                .sum();
    }

    private List<PartyIntDTO> sortMemberByRatingAndPartyNumber(final ResultCountyReport report) {
        List<PartyIntDTO> list = new ArrayList<>();
        final List<PartyIntDTO> partiesVotesSummary = report.getVotesByParty();
        for (PartyIntDTO partyIntDTO : partiesVotesSummary) {
            List<CandidateReport> membersOriginal = partyIntDTO.getPar().getMembers()
                    .stream()
                    .sorted(Comparator.comparing(CandidateReport::getNumberInParty))
                    .collect(Collectors.toList());
            for (int i = 0; i < membersOriginal.size(); i++) {
                membersOriginal.get(i).setNumberInParty(i + 1001);
            }
            List<CandidateReport> membersWithRating = partyIntDTO.getRatings()
                    .stream()
                    .sorted((r1, r2) -> r2.getVotes().compareTo(r1.getVotes()))
                    .map(r -> r.getCandidate())
                    .collect(Collectors.toList());
            for (int i = 0; i < membersWithRating.size(); i++) {
                membersWithRating.get(i).setNumberInParty(i + 1);
            }
            ArrayList<CandidateReport> membersOriginalClone = new ArrayList<CandidateReport>(membersOriginal);


            membersOriginalClone.removeAll(membersWithRating);
            membersOriginalClone.addAll(membersWithRating);
            List<CandidateReport> membersSortedByNumber = membersOriginalClone
                    .stream()
                    .sorted(Comparator.comparing(CandidateReport::getNumberInParty))
                    .collect(Collectors.toList());
            for (int i = 0; i < membersSortedByNumber.size(); i++) {
                membersSortedByNumber.get(i).setNumberInParty(i + 1);
            }
            partyIntDTO.getPar().setMembers(membersSortedByNumber);
            list.add(partyIntDTO);
        }
        return list;
    }

    private List<PartyIntDTO> getPartyIntDtoList(List<ResultMultiEntity> countyMultiResults) {
        return countyMultiResults
                .stream()
                .map(r -> new PartyIntDTO(r.getParty(), 0, new ArrayList<RatingEntity>()))
                .distinct()
                .collect(Collectors.toList());
    }

    private List<PartyIntDTO> getPartiesVotesSummary(List<ResultMultiEntity> countyMultiResults, List<RatingEntity> countyRatings, List<PartyIntDTO> partyIntDTOList) {
        return partyIntDTOList
                .stream()
                .map(partyDto -> {
                    countyMultiResults
                            .stream()
                            .filter(result -> result.getParty().getId().equals(partyDto.getPar().getId()))
                            .forEach(result -> {
                                partyDto.setVotes(partyDto.getVotes() + result.getVotes().intValue());
                            });

                    return partyDto;
                })
                .sorted((r1, r2) -> r2.getVotes().compareTo(r1.getVotes()))
                .collect(Collectors.toList());
    }

    private List<RatingEntity> getCountyRatings(List<ResultMultiEntity> countyMultiResults) {
        return countyMultiResults
                .stream()
                .flatMap(r -> r.getRating().stream())
                .collect(Collectors.toList());
    }

    private List<ResultMultiEntity> getCountyMultiResults(CountyEntity county) {
        return county.getDistricts()
                .stream()
                .filter(d -> d.getSpoiledMulti() != null)
                .filter(d -> d.getResultMultiEntity().get(0).isApproved())
                .map(DistrictEntity::getResultMultiEntity)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<CandidateIntDTO> getCandidatesResults(List<ResultSingleEntity> allSingleResults, Set<CandidateIntDTO> candidatesIntDtoSet) {
        return candidatesIntDtoSet
                .stream()
                .map(cid -> {
                    allSingleResults
                            .stream()
                            .filter(r -> r.getCandidate().getId().equals(cid.getCandidate().getId()))
                            .forEach(r -> cid.setVotes((cid.getVotes() + r.getVotes().intValue())));
                    return cid;
                })
                .sorted((r1, r2) -> r2.getVotes().compareTo(r1.getVotes()))
                .collect(Collectors.toList());
    }

    private Set<CandidateIntDTO> getCandidatesIntDtoSet(List<ResultSingleEntity> allSingleResults) {
        return allSingleResults
                .stream()
                .map(r -> new CandidateIntDTO(r.getCandidate(), 0))
                .distinct()
                .collect(Collectors.toSet());
    }

    private List<ResultSingleEntity> getCountySingleResultList(CountyEntity county) {
        return county.getDistricts()
                .stream()
                .filter(d -> d.getSpoiledSingle() != null)
                .filter(d -> d.getResultSingleEntity().get(0).isApproved())
                .map(d -> d.getResultSingleEntity())
                .flatMap(r -> r.stream())
                .collect(Collectors.toList());
    }

    private long getNumDistrictsVotedMulti(CountyEntity county) {
        return county.getDistricts()
                .stream()
                .filter(d -> d.getSpoiledMulti() != null)
                .filter(d -> d.getResultMultiEntity().get(0).isApproved())
                .count();
    }

    private long getNumDistrictsVotedSingle(CountyEntity county) {
        return county.getDistricts()
                .stream()
                .filter(d -> d.getSpoiledSingle() != null)
                .filter(d -> d.getResultSingleEntity().get(0).isApproved())
                .count();
    }

    public ResultsGeneralReport formGeneralResults() {
        ResultsGeneralReport report = new ResultsGeneralReport();
        final int sumOfVotes = resultMultiRepository.findAll().stream().mapToInt(r -> r.getVotes().intValue()).sum();
        List<DistrictEntity> allDistricts = districtRepository.findAll();
        int spoiledMulti = allDistricts.stream().filter(d -> d.getSpoiledMulti() != null).mapToInt(DistrictEntity::getSpoiledMulti).sum();
        int spoiledSingle = allDistricts.stream().filter(d -> d.getSpoiledSingle() != null).mapToInt(DistrictEntity::getSpoiledSingle).sum();
        int numberOfVoters = allDistricts.stream().mapToInt(d -> d.getNumberOfElectors().intValue()).sum();
        List<CandidateIntDTO> singleWinners = getSingleWinners();
        List<PartyIntDTO> votesInMulti = countPartiesVotesRatings(singleWinners);
        report.setVotesInMulti(votesInMulti);
        report.setSingleWinners(singleWinners);
        report.setVotesCount(sumOfVotes);
        report.setSpoiledMulti(spoiledMulti);
        report.setSpoiledSingle(spoiledSingle);
        report.setVotersCount(numberOfVoters);

        List<PartyIntDTO> partiesOverMinimumLine = getPartiesOverMinimumLine(report, sumOfVotes);
        report.setPartiesOverMinimumLine(partiesOverMinimumLine);

        int sumPartyVotesOverLine = partiesOverMinimumLine.stream().mapToInt(PartyIntDTO::getVotes).sum();
        List<PartyIntDTO> mandatesPerParty = getMandatesPerParty(partiesOverMinimumLine, sumPartyVotesOverLine);
        report.setMandatesPerPartyInMulti(mandatesPerParty);

        List<CandidateReport> multiWinnersList = mandatesPerParty.stream().map(m -> votesInMulti.stream()
                .filter(v -> v.getPar().getId() == m.getPar().getId())
                .findFirst()
                .map(v -> {
                    List<CandidateReport> members = v.getPar().getMembers();
                    List<CandidateReport> winnersList = singleWinners.stream().map(dto -> dto.getCandidate()).collect(Collectors.toList());
                    List<CandidateReport> finalMemnbers = members.stream()
                            .filter(member -> {
                                List<Long> winnersIds = singleWinners.stream().map(w -> w.getCandidate().getId()).collect(Collectors.toList());
                                return !winnersIds.contains(member.getId());
                            })
                            .collect(Collectors.toList());
                    return finalMemnbers;
                })
                .map(v -> v.subList(0, m.getVotes()))
                .get()).flatMap(Collection::stream).collect(Collectors.toList());
        report.setMultiWinners(multiWinnersList);

        int districtCount = allDistricts.size();
        report.setDistrictsCount(districtCount);

        Long districtVoted = allDistricts.stream()
                .filter(d -> !d.getResultMultiEntity().isEmpty() && !d.getResultSingleEntity().isEmpty())
                .filter(d-> d.getResultMultiEntity().get(0).isApproved() && d.getResultSingleEntity().get(0).isApproved())
                .count();
        report.setDistrictsVoted(districtVoted.intValue());

        List<StringLongDTO> multiMandatesPerParty = report.getMandatesPerPartyInMulti()
                .stream()
                .map(p -> new StringLongDTO(p.getPar().getName(), p.getVotes().longValue()))
                .collect(Collectors.toList());
        List<StringLongDTO> singleMandatesPerParty = report.getSingleWinners()
                .stream()
                .map(c -> {
                    if (c.getCandidate().getPartijosPavadinimas() == null) {
                        return new StringLongDTO("Nepartiniai", 1L);
                    } else {
                        return new StringLongDTO(c.getCandidate().getPartijosPavadinimas(), 1L);
                    }
                })
                .collect(Collectors.toList());
        multiMandatesPerParty.addAll(singleMandatesPerParty);
        Map<String, Long> collect = multiMandatesPerParty
                .stream()
                .collect(Collectors.groupingBy(StringLongDTO::getName,
                        Collectors.summingLong(StringLongDTO::getNumber)));
        List<StringLongDTO> listOfMandatesPerParty = new ArrayList<>();
        collect.forEach((s, l) -> listOfMandatesPerParty.add(new StringLongDTO(s, l)));

        report.setMandatesPerPartyGeneralLive(listOfMandatesPerParty.stream().sorted((s1, s2) -> s2.getNumber().compareTo(s1.getNumber())).collect(Collectors.toList()));

        setGeneralReport(report);
        return report;
    }

    private List<PartyIntDTO> getMandatesPerParty(List<PartyIntDTO> partiesOverMinimumLine, int sumPartyVotesOverLine) {
        Integer count = 0;
        List<PartyIntDTO> mandatesPerParty = new ArrayList<>();
        for (int i = 0; i < partiesOverMinimumLine.size(); i++) {
            PartyReport par = new PartyReport(partiesOverMinimumLine.get(i).getPar().getName(), partiesOverMinimumLine.get(i).getPar().getPartyNumber(), partiesOverMinimumLine.get(i).getPar().getId());
            if (i != partiesOverMinimumLine.size() - 1) {
                Float percent = 1.0F * partiesOverMinimumLine.get(i).getVotes() / sumPartyVotesOverLine;
                partiesOverMinimumLine.get(i).setVotes(GeneralConditions.countMandates(percent));
                mandatesPerParty.add(new PartyIntDTO(par, GeneralConditions.countMandates(percent)));
                count = count + GeneralConditions.countMandates(percent);
            } else {
                mandatesPerParty.add(new PartyIntDTO(par, GeneralConditions.getMultiGetMandates() - count));
            }
        }
        return mandatesPerParty;
    }

    private List<PartyIntDTO> getPartiesOverMinimumLine(ResultsGeneralReport report, int sumOfVotes) {
        return report.getVotesInMulti()
                .stream()
                .filter(o -> (1.0 * o.getVotes() / sumOfVotes) * 100 >= GeneralConditions.getMinimumPercentInMulti())
                .map(o -> {
                    PartyIntDTO partyIntDTO = new PartyIntDTO(new PartyReport(o.getPar().getName(), o.getPar().getPartyNumber(), o.getPar().getId()), o.getVotes());
                    return partyIntDTO;
                })
                .collect(Collectors.toList());
    }

    private List<CandidateIntDTO> getSingleWinners() {
        return countyRepository.findAll()
                .stream()
                .filter(c -> !c.getCandidates().isEmpty())
                .map(county -> {
                    List<ResultSingleEntity> votesForCandidates = county.getCandidates()
                            .stream()
                            .map(CandidateEntity::getResults)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList());
                    Map<CandidateEntity, List<ResultSingleEntity>> candidateToVotes = votesForCandidates
                            .stream()
                            .collect(Collectors.groupingBy(ResultSingleEntity::getCandidate));
                    List<CandidateIntDTO> list = new ArrayList<>();
                    candidateToVotes.forEach((candidateEntity, resultSingleEntities) -> {
                        Long sum = resultSingleEntities.stream().mapToLong(ResultSingleEntity::getVotes).sum();
                        list.add(new CandidateIntDTO(candidateEntity, sum.intValue()));
                    });
                    List<CandidateIntDTO> sortedList = list
                            .stream()
                            .sorted((d1, d2) -> d2.getVotes().compareTo(d1.getVotes()))
                            .collect(Collectors.toList());
                    return sortedList.iterator().next();
                }).collect(Collectors.toList());
    }

    private List<PartyIntDTO> countPartiesVotesRatings(List<CandidateIntDTO> singleWinners) {
        List<PartyEntity> allParties = partyRepository.findAll();
        return allParties
                .stream()
                .map(p -> {
                    List<ResultMultiEntity> byParty = resultMultiRepository.findByParty(p);
                    Integer sum = byParty
                            .stream()
                            .mapToInt(v -> v.getVotes().intValue()).sum();
                    List<RatingEntity> partyRatingsAll = ratingRepository.findByPartyCandidate(p);
                    List<RatingEntity> partyRatings = new ArrayList<>();
                    partyRatingsAll
                            .stream()
                            .collect(Collectors.groupingBy(RatingEntity::getCandidate, Collectors.summingInt(RatingEntity::getPoints)))
                            .forEach(((candidate, integer) -> {
                                RatingEntity rating = new RatingEntity();
                                rating.setCandidate(candidate);
                                rating.setPoints(integer);
                                if (integer > candidate.getPartyDependencies().getMembers().stream().filter(CandidateEntity::isMultiList).count()) {
                                    partyRatings.add(rating);
                                }
                            }));
                    List<RatingEntity> sortedRatings = partyRatings.stream().sorted((r1, r2) -> r2.getPoints().compareTo(r1.getPoints())).collect(Collectors.toList());
                    List<CandidateEntity> candidatesOrderFromRatingsOriginal = sortedRatings.stream().map(RatingEntity::getCandidate).collect(Collectors.toList());
                    ArrayList<CandidateEntity> candidatesOrderFromRatingsCloned = new ArrayList<>(candidatesOrderFromRatingsOriginal);
                    List<CandidateEntity> candidatesOrderOriginalBeforeClone = p.getMembers().stream().sorted(Comparator.comparing(CandidateEntity::getNumberInParty)).collect(Collectors.toList());
                    ArrayList<CandidateEntity> candidatesOrderOriginal = new ArrayList<>(candidatesOrderOriginalBeforeClone);
                    candidatesOrderOriginal.removeAll(candidatesOrderFromRatingsCloned);
                    List<CandidateEntity> candidatesNewRatingOrder = new ArrayList();
                    candidatesNewRatingOrder.addAll(candidatesOrderFromRatingsCloned);
                    candidatesNewRatingOrder.addAll(candidatesOrderOriginal);
                    List<Long> singleWinnersIds = singleWinners.stream().map(c -> c.getCandidate().getId()).collect(Collectors.toList());
                    List<CandidateEntity> finalModifiedList = new ArrayList<>();

                    List<CandidateEntity> listToOrderClassic = new ArrayList();
                    for (CandidateEntity candidateEntity : candidatesNewRatingOrder) {
                        boolean isContains = singleWinnersIds.contains(candidateEntity.getId());
                        if (!isContains){
                            listToOrderClassic.add(candidateEntity);
                        }
                    }

                    for (int i = 0; i < listToOrderClassic.size(); i++) {
                        PartyEntity partyEntity = new PartyEntity();
                        partyEntity.setName(p.getName());
                        CandidateEntity candidate = new CandidateEntity();
                        candidate.setBirthDate(listToOrderClassic.get(i).getBirthDate());
                        candidate.setCounty(listToOrderClassic.get(i).getCounty());
                        candidate.setName(listToOrderClassic.get(i).getName());
                        candidate.setId(listToOrderClassic.get(i).getId());
                        candidate.setDescription(listToOrderClassic.get(i).getDescription());
                        candidate.setMultiList(listToOrderClassic.get(i).isMultiList());
                        candidate.setSurname(listToOrderClassic.get(i).getSurname());
                        candidate.setNumberInParty(i + 1);
                        candidate.setPartyDependencies(partyEntity);
                        finalModifiedList.add(candidate);
                    }
                    PartyEntity tp = new PartyEntity();
                    tp.setName(p.getName());
                    tp.setId(p.getId());
                    tp.setMembers(finalModifiedList);
                    tp.setResults(p.getResults());
                    tp.setPartyNumber(p.getPartyNumber());

                    return new PartyIntDTO(tp, sum, sortedRatings);
                })
                .sorted((v1, v2) -> v2.getVotes().compareTo(v1.getVotes()))
                .collect(Collectors.toList());
    }
}
