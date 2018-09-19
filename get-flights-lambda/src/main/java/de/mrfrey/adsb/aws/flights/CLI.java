package de.mrfrey.adsb.aws.flights;

import de.mrfrey.adsb.aws.flights.lambda.GetFlights;

public class CLI {
    public static void main(String[] args) {
        GetFlights getFlights = new GetFlights();
        getFlights.getFlights();
    }
}
