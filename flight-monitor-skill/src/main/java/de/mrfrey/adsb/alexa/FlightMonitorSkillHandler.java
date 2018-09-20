package de.mrfrey.adsb.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import de.mrfrey.adsb.alexa.handlers.FlightExceptionHandler;
import de.mrfrey.adsb.alexa.handlers.OneshotFlightsIntentHandler;

public class FlightMonitorSkillHandler extends SkillStreamHandler {
    public FlightMonitorSkillHandler() {

        super( getSkill() );
    }

    private static Skill getSkill() {
        return Skills.standard()
                     .addRequestHandlers( new OneshotFlightsIntentHandler() )
                     .addExceptionHandlers( new FlightExceptionHandler() )
                     .build();
    }
}
