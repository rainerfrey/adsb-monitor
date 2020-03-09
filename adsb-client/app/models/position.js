import classic from 'ember-classic-decorator';
import {computed} from '@ember/object';
import {alias} from '@ember/object/computed';
import Model, {attr} from '@ember-data/model';
import planeIcons from 'adsb-client/utils/plane-icon';


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

  @computed('positions')
  get points() {
    return this.positions.map(position => {
      return {
        lat: position.latitude, lng: position.longitude
      }
    });
  }
}
