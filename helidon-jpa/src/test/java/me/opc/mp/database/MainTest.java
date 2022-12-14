
package me.opc.mp.database;

import javax.inject.Inject;
import javax.json.JsonArray;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @Test
    void testPokemonTypes() {
        JsonArray types = target
                .path("type")
                .request()
                .get(JsonArray.class);
        assertThat(types.size(), is(18));
    }

    @Test
    void testPokemon() {
        assertThat(getPokemonCount(), is(6));

        Pokemon pokemon = target
                .path("pokemon/1")
                .request()
                .get(Pokemon.class);
        assertThat(pokemon.getName(), is("Bulbasaur"));

        pokemon = target
                .path("pokemon/name/Charmander")
                .request()
                .get(Pokemon.class);
        assertThat(pokemon.getType(), is(10));

        try (Response response = target
                .path("pokemon/1")
                .request()
                .get()) {
            assertThat(response.getStatus(), is(200));
        }

        Pokemon test = new Pokemon();
        test.setType(1);
        test.setId(100);
        test.setName("Test");
        try (Response response = target
                .path("pokemon")
                .request()
                .post(Entity.entity(test, MediaType.APPLICATION_JSON))) {
            assertThat(response.getStatus(), is(204));
            assertThat(getPokemonCount(), is(7));
        }

        try (Response response = target
                .path("pokemon/100")
                .request()
                .delete()) {
            assertThat(response.getStatus(), is(204));
            assertThat(getPokemonCount(), is(6));
        }
    }

    @Test
    void testHealthMetrics() {
        try (Response response = target
                .path("health")
                .request()
                .get()) {
            assertThat(response.getStatus(), is(200));
        }
        try (Response response = target
                .path("metrics")
                .request()
                .get()) {
            assertThat(response.getStatus(), is(200));
        }
    }

    private int getPokemonCount() {
        JsonArray pokemons = target
                .path("pokemon")
                .request()
                .get(JsonArray.class);
        return pokemons.size();
    }
}
