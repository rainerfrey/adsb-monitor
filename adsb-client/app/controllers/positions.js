import Ember from 'ember';
import ENV from "adsb-client/config/environment";

export default Ember.Controller.extend({
  map: Ember.inject.service(),
  home: ENV.adsb.home,
  homeIcon: 'https://mt0.google.com/vt/icon/name=icons/spotlight/home_S_8x.png&scale=1.0',
  mapOptions: function() {
    return {mapTypeId: this.get("map.defaultMapType")};
  }.property(),
  currentFlight: null,
  lastTimestamp: Ember.computed.alias('model.meta.lastTimestamp'),
  actions: {
    selectFlight: function(flight) {
      this.set("currentFlight", flight);
    }
  }
});
