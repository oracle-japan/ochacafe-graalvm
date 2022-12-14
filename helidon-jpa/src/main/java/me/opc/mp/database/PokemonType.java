
package me.opc.mp.database;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A Pokemon Type entity. A type is represented by an ID and a name.
 *
 * Pokémon, and Pokémon character names are trademarks of Nintendo.
 */
@Entity(name = "PokemonType")
@Table(name = "POKEMONTYPE")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "getPokemonTypes",
                    query = "SELECT t FROM PokemonType t"),
        @NamedQuery(name = "getPokemonTypeById",
                    query = "SELECT t FROM PokemonType t WHERE t.id = :id")
})
public class PokemonType {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    private int id;

    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    public PokemonType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
