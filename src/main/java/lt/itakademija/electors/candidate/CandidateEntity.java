package lt.itakademija.electors.candidate;

import lt.itakademija.electors.county.CountyEntity;
import lt.itakademija.electors.party.PartyEntity;
import lt.itakademija.electors.results.multi.rating.RatingEntity;
import lt.itakademija.electors.results.single.ResultSingleEntity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Pavel on 2017-01-12.
 */
@Entity
public class CandidateEntity {

    @Id
    @Column(name = "CANDIDATE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name= "PARTY_ID")
    private PartyEntity partyDependencies;

    @Column(name = "NUMBER_IN_PARTY")
    private Integer numberInParty;

    private String description;

    @ManyToOne
    @JoinColumn(name= "COUNTY_ID")
    private CountyEntity county;

    @Column(nullable=false)
    private boolean isMultiList = true;

    @OneToMany(mappedBy="candidate", cascade = CascadeType.REMOVE)
    private List<ResultSingleEntity> results;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.REMOVE)
    private List<RatingEntity> ratingPoints;

    public List<RatingEntity> getRatingPoints() {
        return ratingPoints;
    }

    public void setRatingPoints(List<RatingEntity> ratingPoints) {
        this.ratingPoints = ratingPoints;
    }

    public boolean isMultiList() {
        return isMultiList;
    }

    public void setMultiList(boolean multiList) {
        isMultiList = multiList;
    }

    public Long getId() {
        return id;
    }

    public CountyEntity getCounty() {
        return county;
    }

    public void setCounty(CountyEntity county) {
        this.county = county;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumberInParty() {
        return numberInParty;
    }

    public void setNumberInParty(Integer numberInParty) {
        this.numberInParty = numberInParty;
    }

    public PartyEntity getPartyDependencies() {
        return partyDependencies;
    }

    public void setPartyDependencies(PartyEntity partyDependencies) {
        this.partyDependencies = partyDependencies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ResultSingleEntity> getResults() {
        return results;
    }

    public void setResults(List<ResultSingleEntity> results) {
        this.results = results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateEntity that = (CandidateEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(birthDate, that.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, birthDate);
    }
}

