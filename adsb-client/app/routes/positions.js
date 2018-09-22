import { later, scheduleOnce } from '@ember/runloop';
import { inject as service } from '@ember/service';
import Route from '@ember/routing/route';

export default Route.extend({
  ajax: service(),
  settings: service(),
  init() {
    this._super(...arguments);
    this.settings.on("startLiveMonitoring", this, this.reloadModel);
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
      later(()=>scheduleOnce("actions", this, "doReload"), 3000);
    }
  }
});
