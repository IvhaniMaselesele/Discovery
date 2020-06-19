package za.co.discovery.assignment.models;


public class Edge {
    public String edgeId;
    public Planet origin;
    public Planet destination;
    public double weight;
    public double traffic;
    public String destinationId;
    public String originId;

    public Edge(String edgeId, Planet origin, Planet destination, double weight) {
        this.edgeId = edgeId;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
    }

    public Edge() {
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public void setDestination(Planet destination) {
        this.destination = destination;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public double getTraffic() {
        return traffic;
    }

    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    public void setOrigin(Planet origin) {
        this.origin = origin;
    }
}