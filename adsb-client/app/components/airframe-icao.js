import classic from 'ember-classic-decorator';
import { action } from '@ember/object';
import Component from '@ember/component';

@classic
export default class AirframeIcao extends Component {
  @action
  request() {
    this.$('#horrible_hack')[0].submit.call(this.$('#airframes_post')[0]);
    return false;
  }
}
