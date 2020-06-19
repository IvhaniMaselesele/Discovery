package za.co.discovery.assignment.models;

public final class PlanetBuilder {
    private String node;
    private String name;

    private PlanetBuilder() {
    }

    public static PlanetBuilder aPlanet() {
        return new PlanetBuilder();
    }

    public PlanetBuilder withNode(String node) {
        this.node = node;
        return this;
    }

    public PlanetBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PlanetBuilder but() {
        return aPlanet().withNode(node).withName(name);
    }

    public Planet build() {
        Planet planet = new Planet();
        planet.setNode(node);
        planet.setName(name);
        return planet;
    }
}
