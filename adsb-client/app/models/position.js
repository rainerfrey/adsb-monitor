import Ember from "ember";
import Model from 'ember-data/model';
import attr from 'ember-data/attr';


export default Model.extend({
  flightId: Ember.computed.alias("id"),
  icao: attr('string'),
  aircraft: attr(),
  altitude: attr('number'),
  speed: attr('number'),
  heading: attr('number'),
  lastTimestamp: attr('date'),
  positions: attr()
});
