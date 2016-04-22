package za.co.discovery.assignment.models;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "route")
public class Route {
    @Id
    @Column(nullable = false)
    int id;

    @OneToOne(fetch = FetchType.EAGER)
    private Planet origin;

    @OneToOne(fetch = FetchType.EAGER)
    private Planet destination;

    @Column
    private double distance;
    @Column
    private double traffic;

    protected Route() {

    }

    public Route(int id, Planet origin, Planet destination, double distance) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return Integer.toString(id) + "\t" + origin + "\t" + destination + "\t" + distance;
    }

    public double getDistance() {
        return distance;
    }

    public int getId() {
        return id;
    }

    public void setOrigin(Planet origin) {
        this.origin = origin;
    }

    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    public double getTraffic() {
        return traffic;
    }

    public Planet getOrigin() {
        return origin;
    }

    public Planet getDestination() {
        return destination;
    }
}
