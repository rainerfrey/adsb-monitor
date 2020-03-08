import classic from 'ember-classic-decorator';
import { action } from '@ember/object';
import Component from '@ember/component';

@classic
export default class EditSettings extends Component {
  myLive = false;
  myTimeframe = 15;

  init() {
    super.init(...arguments);
    this.initValues();
  }

  initValues() {
    this.set("myLive", this.liveMonitoring);
    this.set("myTimeframe", this.timeframe);
  }

  didReceiveAttrs() {
    super.didReceiveAttrs(...arguments);
    this.initValues();
  }

  @action
  applySettings() {
    this.apply(this.myTimeframe, this.myLive);
    this.close();
  }
}
