import Ember from "ember";

export default Ember.Route.extend({
  ajax: Ember.inject.service(),
  settings: Ember.inject.service(),
  init() {
    this._super(...arguments);
    this.get("settings").on("startLiveMonitoring", this, this.reloadModel);
  },
  model() {
    return this.store.query('position', {from: this.get("settings.timeframe")});
  },
  afterModel: function() {
    this.reloadModel();
  },
  doReload() {
    this.refresh();
  },
  reloadModel() {
    let live = this.get("settings.live");
    if(live === true) {
      Ember.run.later(()=>Ember.run.scheduleOnce("actions", this, "doReload"), 3000);
    }
  }
});
