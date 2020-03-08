import classic from 'ember-classic-decorator';
import { alias } from '@ember/object/computed';
import Model, { attr } from '@ember-data/model';


@classic
export default class Position extends Model {
  @alias("id")
  flightId;

  @attr('string')
  icao;

  @attr()
  aircraft;

  @attr('number')
  altitude;

  @attr('number')
  speed;

  @attr('number')
  heading;

  @attr('date')
  lastTimestamp;

  @attr()
  positions;
}
