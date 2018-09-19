package de.mrfrey.adsb.aws.flights.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.mrfrey.adsb.aws.flights.data.Flight;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class GetFlightsForGateway implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private GetFlights getFlights;
    private ObjectMapper objectMapper;

    public GetFlightsForGateway() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        getFlights = new GetFlights();
    }


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        List<Flight> flights = getFlights.getFlights();
        try {
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(flights));
        } catch (JsonProcessingException e) {
            StringWriter sw = new StringWriter();
            PrintWriter p = new PrintWriter(sw);
            p.println(e.getMessage());
            e.printStackTrace(p);

            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody(sw.toString());
        }
    }
}