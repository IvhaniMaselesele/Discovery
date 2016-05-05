package za.co.discovery.assignment.models;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "route")
public class Route {
    @Id
    @Column(nullable = false)
    int id;

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.EAGER)
    private Planet origin;

    public void setDestination(Planet destination) {
        this.destination = destination;
    }

    @OneToOne(fetch = FetchType.EAGER)
    private Planet destination;

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Column
    private double distance;
    @Column
    private double traffic;

    public Route() {

    }

    public Route(int id, Planet origin, Planet destination, double distance) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public Route(int id, Planet origin, Planet destination, double distance, double traffic) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.traffic = traffic;
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
