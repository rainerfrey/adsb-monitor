import classic from 'ember-classic-decorator';
import { classNames, tagName } from '@ember-decorators/component';
import Component from '@ember/component';

@classic
@tagName('button')
@classNames('button')
export default class ClickableLi extends Component {
  click() {
    this.onClick();
  }
}
