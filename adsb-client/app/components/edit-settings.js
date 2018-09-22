import Component from '@ember/component';

export default Component.extend({
  myLive: false,
  myTimeframe: 15,

  init() {
    this._super(...arguments);
    this.initValues();
  },

  initValues: function () {
    this.set("myLive", this.liveMonitoring);
    this.set("myTimeframe", this.timeframe);
  },

  didReceiveAttrs() {
    this._super(...arguments);
    this.initValues();
  },

  actions: {
    applySettings() {
      this.apply(this.myTimeframe, this.myLive);
      this.close();
    }
  }
});
