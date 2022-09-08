package example.micronaut.jpa;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/country")
public class CountryController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private CountryDAO countryDAO;

    public CountryController(CountryDAO countryDAO) {
        this.countryDAO = countryDAO;
    }

    @Get("/")
    public List<Country> getCountries() {
        logger.info("##### getCountries() #####");
        return countryDAO.getCountries();
    }

    @Get("/{countryId}")
    public Country getCountry(int countryId) throws CountryNotFoundException{
        logger.info("##### getCountry() #####");
        return countryDAO.getCountry(countryId);
    }

    @Post("/")
    public void insertCountry(@Body Country[] countries) {
        logger.info("##### insertCountry() #####");
        countryDAO.insertCountries(countries);
    }

    @Put("/")
    public void updateCountry(@Body Country country) {
        logger.info("##### updateCountry() #####");
        countryDAO.updateCountry(country.getCountryId(), country.getCountryName());
    }

    @Delete("/{countryId}")
    public void deleteCountry(int countryId) {
        logger.info("##### deleteCountry() #####");
        countryDAO.deleteCountry(countryId);
    }

}
