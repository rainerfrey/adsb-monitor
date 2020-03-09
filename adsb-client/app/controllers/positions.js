import classic from 'ember-classic-decorator';
import {action} from '@ember/object';
import {inject as service} from '@ember/service';
import {alias, reads} from '@ember/object/computed';
import Controller from '@ember/controller';
import ENV from "adsb-client/config/environment";

@classic
export default class PositionsController extends Controller {

  @service
  googleMapsApi;

  @reads('googleMapsApi.google')
  google;

  home = ENV.adsb.home;
  homeIcon = 'https://mt0.google.com/vt/icon/name=icons/spotlight/home_S_8x.png&scale=1.0';

  currentFlight = null;

  @alias('model.meta.lastTimestamp')
  lastTimestamp;

  @action
  selectFlight(flight) {
    this.set("currentFlight", flight);
  }
}
