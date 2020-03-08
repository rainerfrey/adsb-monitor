import classic from 'ember-classic-decorator';
import { action, computed } from '@ember/object';
import { inject as service } from '@ember/service';
import { alias } from '@ember/object/computed';
import Controller from '@ember/controller';
import ENV from "adsb-client/config/environment";

@classic
export default class PositionsController extends Controller {
  @service
  map;

  home = ENV.adsb.home;
  homeIcon = 'https://mt0.google.com/vt/icon/name=icons/spotlight/home_S_8x.png&scale=1.0';

  @computed
  get mapOptions() {
    return {mapTypeId: this.get("map.defaultMapType")};
  }

  currentFlight = null;

  @alias('model.meta.lastTimestamp')
  lastTimestamp;

  @action
  selectFlight(flight) {
    this.set("currentFlight", flight);
  }
}
