import Ember from 'ember';
import ENV from "adsb-client/config/environment";

export default Ember.Service.extend(Ember.Evented, {
  timeframe: ENV.adsb.timeframe,
  live: ENV.adsb.liveMonitoring,

  startLiveMonitoring() {
    this.set("live", true);
    this.trigger("startLiveMonitoring");
  },

  setLiveMonitoring(live) {
    if(live===true) {
      this.startLiveMonitoring();
    }
    else {
      this.set("live", live);
    }
  }
});
