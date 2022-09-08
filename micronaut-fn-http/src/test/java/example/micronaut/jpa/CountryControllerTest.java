package example.micronaut.jpa;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.oraclecloud.function.http.test.FnHttpTest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class CountryControllerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Test
    void testGetCountryNormal() {
        HttpResponse<String> response = FnHttpTest.invoke(
                HttpRequest.GET("/country"));
        assertEquals(OK, response.status());
        logger.info("Response:\n" + response.body());

        response = FnHttpTest.invoke(HttpRequest.GET("/country/1"));
        assertEquals(OK, response.status());
        assertEquals("{\"countryId\":1,\"countryName\":\"USA\"}", response.body());
    }

    @Test
    void testGetCountryError() {
        HttpResponse<String> response = FnHttpTest.invoke(HttpRequest.GET("/country/2"));
        assertEquals(INTERNAL_SERVER_ERROR, response.status());
    }

    @Test
    void testUpdateCountry() {
        // POST
        String countries = "[{\"countryId\":9999,\"countryName\":\"TEST_DATA\"}]";
        HttpResponse<String> response = FnHttpTest.invoke(HttpRequest.POST("/country", countries));
        assertEquals(OK, response.status());

        response = FnHttpTest.invoke(HttpRequest.GET("/country/9999"));
        assertEquals(OK, response.status());
        assertEquals("{\"countryId\":9999,\"countryName\":\"TEST_DATA\"}", response.body());

        // PUT
        String country = "{\"countryId\":9999,\"countryName\":\"TEST_DATA_UPDATED\"}";
        response = FnHttpTest.invoke(HttpRequest.PUT("/country", country));
        assertEquals(OK, response.status());

        response = FnHttpTest.invoke(HttpRequest.GET("/country/9999"));
        assertEquals(OK, response.status());
        assertEquals("{\"countryId\":9999,\"countryName\":\"TEST_DATA_UPDATED\"}", response.body());

        // DELETE
        response = FnHttpTest.invoke(HttpRequest.DELETE("/country/9999"));
        assertEquals(OK, response.status());

        response = FnHttpTest.invoke(HttpRequest.GET("/country/9999"));
        assertEquals(INTERNAL_SERVER_ERROR, response.status());

    }

}
