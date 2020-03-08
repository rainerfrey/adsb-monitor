import classic from 'ember-classic-decorator';
import { inject as service } from '@ember/service';
import { later, scheduleOnce } from '@ember/runloop';
import Route from '@ember/routing/route';

@classic
export default class PositionsRoute extends Route {
  @service
  ajax;

  @service
  settings;

  init() {
    super.init(...arguments);
    this.settings.on("startLiveMonitoring", this, this.reloadModel);
  }

  model() {
    return this.store.query('position', {from: this.get("settings.timeframe")});
  }

  afterModel() {
    this.reloadModel();
  }

  doReload() {
    this.refresh();
  }

  reloadModel() {
    let live = this.get("settings.live");
    if(live === true) {
      later(()=>scheduleOnce("actions", this, "doReload"), 3000);
    }
  }
}
