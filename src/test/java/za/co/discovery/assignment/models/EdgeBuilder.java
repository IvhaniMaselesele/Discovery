package za.co.discovery.assignment.models;

public final class EdgeBuilder {
    public String edgeId;
    public Planet destination;
    public double weight;
    public double traffic;
    public String destinationId;
    public String originId;
    public Planet origin;

    private EdgeBuilder() {
    }

    public static EdgeBuilder anEdge() {
        return new EdgeBuilder();
    }

    public EdgeBuilder withEdgeId(String edgeId) {
        this.edgeId = edgeId;
        return this;
    }

    public EdgeBuilder withDestination(Planet destination) {
        this.destination = destination;
        return this;
    }

    public EdgeBuilder withWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public EdgeBuilder withTraffic(double traffic) {
        this.traffic = traffic;
        return this;
    }

    public EdgeBuilder withDestinationId(String destinationId) {
        this.destinationId = destinationId;
        return this;
    }

    public EdgeBuilder withOriginId(String originId) {
        this.originId = originId;
        return this;
    }

    public EdgeBuilder withOrigin(Planet origin) {
        this.origin = origin;
        return this;
    }

    public EdgeBuilder but() {
        return anEdge().withEdgeId(edgeId).withDestination(destination).withWeight(weight).withTraffic(traffic).withDestinationId(destinationId).withOriginId(originId).withOrigin(origin);
    }

    public Edge build() {
        Edge edge = new Edge();
        edge.setOrigin(origin);
        edge.setEdgeId(edgeId);
        edge.setDestination(destination);
        edge.setWeight(weight);
        edge.setTraffic(traffic);
        edge.setDestinationId(destinationId);
        edge.setOriginId(originId);
        return edge;
    }
}
