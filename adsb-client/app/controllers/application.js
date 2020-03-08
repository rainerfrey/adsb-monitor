import classic from 'ember-classic-decorator';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { isEmpty } from '@ember/utils';
import Controller from '@ember/controller';

@classic
export default class ApplicationController extends Controller {
  @service
  settings;

  showSettings = false;

  @action
  toggleSettings() {
    this.toggleProperty("showSettings");
  }

  @action
  applySettings(timeframe, liveMonitoring) {
    if(!isEmpty(timeframe)) {
      this.set("settings.timeframe", timeframe);
    }
    if(!isEmpty(liveMonitoring)) {
      this.settings.setLiveMonitoring(liveMonitoring);
    }
  }
}
