package example.micronaut.jpa;

import java.util.Arrays;
import java.util.List;

import jakarta.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import io.micronaut.transaction.annotation.ReadOnly;


@Singleton
public class CountryDAO {

    private final EntityManager em;

    public CountryDAO(EntityManager em){
        this.em = em;
    }

    @ReadOnly
    public List<Country> getCountries(){
        final List<Country> countries = em.createQuery("select c from Country c", Country.class).getResultList();
        return countries;
    }

    @ReadOnly
    public Country getCountry(int countryId) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        return country;
    }

    @Transactional
    public void insertCountries(Country[] countries) {
        Arrays.stream(countries).forEach(em::persist);
    }

    @Transactional
    public void insertCountries(List<Country> countries) {
        countries.stream().forEach(em::persist);
    }

    @Transactional
    public void insertCountry(int countryId, String countryName) {
        em.persist(new Country(countryId, countryName));
    }

    @Transactional
    public void updateCountry(int countryId, String countryName) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        country.setCountryName(countryName);
        em.persist(country);
    }

    @Transactional
    public void deleteCountry(int countryId) {
        final Country country = em.find(Country.class, countryId);
        if(null == country) 
            throw new CountryNotFoundException(String.format("Couldn't find country, id=%d", countryId));
        em.remove(country);
    }

}