import { alias } from '@ember/object/computed';
import Model from 'ember-data/model';
import attr from 'ember-data/attr';


export default Model.extend({
  flightId: alias("id"),
  icao: attr('string'),
  aircraft: attr(),
  altitude: attr('number'),
  speed: attr('number'),
  heading: attr('number'),
  lastTimestamp: attr('date'),
  positions: attr()
});
