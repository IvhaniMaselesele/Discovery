package za.co.discovery.assignment.models;

public final class RouteBuilder {
    int id;
    private Planet origin;
    private double traffic;
    private Planet destination;
    private double distance;

    private RouteBuilder() {
    }

    public static RouteBuilder aRoute() {
        return new RouteBuilder();
    }

    public RouteBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public RouteBuilder withOrigin(Planet origin) {
        this.origin = origin;
        return this;
    }

    public RouteBuilder withTraffic(double traffic) {
        this.traffic = traffic;
        return this;
    }

    public RouteBuilder withDestination(Planet destination) {
        this.destination = destination;
        return this;
    }

    public RouteBuilder withDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public RouteBuilder but() {
        return aRoute().withId(id).withOrigin(origin).withTraffic(traffic).withDestination(destination).withDistance(distance);
    }

    public Route build() {
        Route route = new Route(id, origin, destination, distance);
        route.setTraffic(traffic);
        return route;
    }
}
