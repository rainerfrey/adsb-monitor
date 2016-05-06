import Ember from "ember";

export default Ember.Component.extend({
  myLive: false,
  myTimeframe: 15,

  init() {
    this._super(...arguments);
    this.initValues();
  },

  initValues: function () {
    this.set("myLive", this.get("liveMonitoring"));
    this.set("myTimeframe", this.get("timeframe"));
  },

  didReceiveAttrs() {
    this._super(...arguments);
    this.initValues();
  },

  actions: {
    applySettings() {
      this.get("apply")(this.get("myTimeframe"), this.get("myLive"));
      this.get("close")();
    }
  }
});
