import Evented from '@ember/object/evented';
import Service from '@ember/service';
import ENV from "adsb-client/config/environment";

export default Service.extend(Evented, {
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
