package za.co.discovery.assignment.models;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "planet")
public class Planet {

    @Id
    @Column(nullable = false)
    private String node;

    @Column
    private String name;

    public Planet() {
    }

    public Planet(String node, String name) {
        this.name = name;
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Planet planet = (Planet) o;

        if (node != null ? !node.equals(planet.node) : planet.node != null) return false;
        return !(name != null ? !name.equals(planet.name) : planet.name != null);

    }

    @Override
    public int hashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
