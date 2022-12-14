
package me.opc.mp.database;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This class implements a REST endpoint to retrieve Pokemon types.
 *
 * <ul>
 * <li>GET /type: Retrieve list of all pokemon types</li>
 * </ul>
 *
 * Pokémon, and Pokémon character names are trademarks of Nintendo.
 */
@Path("type")
public class PokemonTypeResource {

    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PokemonType> getPokemonTypes() {
        return entityManager.createNamedQuery("getPokemonTypes", PokemonType.class).getResultList();
    }
}
