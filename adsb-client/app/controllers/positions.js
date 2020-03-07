import { computed } from '@ember/object';
import { alias } from '@ember/object/computed';
import { inject as service } from '@ember/service';
import Controller from '@ember/controller';
import ENV from "adsb-client/config/environment";

export default Controller.extend({
  map: service(),
  home: ENV.adsb.home,
  homeIcon: 'https://mt0.google.com/vt/icon/name=icons/spotlight/home_S_8x.png&scale=1.0',
  mapOptions: computed(function() {
    return {mapTypeId: this.get("map.defaultMapType")};
  }),
  currentFlight: null,
  lastTimestamp: alias('model.meta.lastTimestamp'),
  actions: {
    selectFlight: function(flight) {
      this.set("currentFlight", flight);
    }
  }
});
