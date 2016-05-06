import Ember from 'ember';

export default Ember.Controller.extend({
  settings: Ember.inject.service(),
  showSettings: false,

  actions: {
    toggleSettings() {
      this.toggleProperty("showSettings");
    },
    applySettings(timeframe, liveMonitoring) {
      if(!Ember.isEmpty(timeframe)) {
        this.set("settings.timeframe", timeframe);
      }
      if(!Ember.isEmpty(liveMonitoring)) {
        this.get("settings").setLiveMonitoring(liveMonitoring);
      }
    }
  }
});
