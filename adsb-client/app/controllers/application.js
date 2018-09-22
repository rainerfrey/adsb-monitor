import { isEmpty } from '@ember/utils';
import { inject as service } from '@ember/service';
import Controller from '@ember/controller';

export default Controller.extend({
  settings: service(),
  showSettings: false,

  actions: {
    toggleSettings() {
      this.toggleProperty("showSettings");
    },
    applySettings(timeframe, liveMonitoring) {
      if(!isEmpty(timeframe)) {
        this.set("settings.timeframe", timeframe);
      }
      if(!isEmpty(liveMonitoring)) {
        this.get("settings").setLiveMonitoring(liveMonitoring);
      }
    }
  }
});
