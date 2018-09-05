package de.mrfrey.adsbmonitor.connector.flow;

import de.mrfrey.adsbmonitor.connector.config.AdsbConfiguration;
import de.mrfrey.adsbmonitor.connector.data.AwsData;
import de.mrfrey.adsbmonitor.connector.data.FlightData;
import de.mrfrey.adsbmonitor.connector.data.aircraft.Aircraft;
import de.mrfrey.adsbmonitor.connector.data.aircraft.AircraftRepository;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AwsFlightTransformer {

    private final AircraftRepository aircraftRepository;
    private final AdsbConfiguration adsbConfiguration;
    private final GlobalCoordinates home;
    private final GeodeticCalculator geoCalculator;

    @Autowired
    public AwsFlightTransformer( AircraftRepository aircraftRepository, AdsbConfiguration adsbConfiguration ) {
        this.aircraftRepository = aircraftRepository;
        this.adsbConfiguration = adsbConfiguration;
        this.home = new GlobalCoordinates( adsbConfiguration.getHome().getLatitude(), adsbConfiguration.getHome().getLongitude() );
        this.geoCalculator = new GeodeticCalculator();
    }

    public AwsData transform( FlightData flightData ) {
        AwsData result = new AwsData( flightData.getFlightId(), flightData.getTimestamp() );
        aircraftRepository.findById( flightData.getIcao().toUpperCase() ).ifPresent( aircraft -> {
            result.setRegistration( aircraft.getRegistration() );
            result.setModel( aircraft.getModel().getName() );
            result.setOperator( aircraft.getOperator().getName() );
        } );
        result.setDistanceFromHome( distanceFromHome( flightData ) );
        return result;
    }

    public double distanceFromHome( FlightData flightData ) {
        GlobalCoordinates position = new GlobalCoordinates( flightData.getLatitude(), flightData.getLongitude() );
        double distance = new GeodeticCalculator().calculateGeodeticCurve( Ellipsoid.WGS84, home, position ).getEllipsoidalDistance();
        return distance / 1000;
    }
}
