import classic from 'ember-classic-decorator';
import { observes } from '@ember-decorators/object';
import { alias } from '@ember/object/computed';
import Component from '@ember/component';
import { isPresent, isEmpty } from '@ember/utils';
import '@ember/object';
import { run } from '@ember/runloop';
import { assert } from '@ember/debug';
import GMapComponent from 'ember-g-map/components/g-map';

@classic
class GMapPolyLineComponent extends Component {
  selected = true;
  positionalParams = ['mapContext'];

  @alias('mapContext.map')
  map;

  init() {
    super.init(...arguments);
    let mapContext = this.mapContext;
    assert('Must be inside {{#g-map}} component with context set', mapContext instanceof GMapComponent);
  }

  didInsertElement() {
    super.didInsertElement();
    if (isEmpty(this.line)) {
      let line = new google.maps.Polyline(this.getPolyLineOptions());
      this.set('line', line);
    }
    this.setPositions();
    this.setMap();
  }

  willDestroyElement() {
    let line = this.line;
    if (isPresent(line)) {
      line.setMap(null);
    }
  }

  setMap() {
    let map = this.map;
    let line = this.line;

    if (isPresent(line) && isPresent(map)) {
      line.setMap(map);
    }
  }

  unsetFromMap() {
    let line = this.line;
    if(isPresent(line)) {
      line.setMap(null);
    }
  }

  setPositions() {
    let line = this.line;
    let positions = this.positions;
    if (isPresent(line) && isPresent(positions)) {
      let points = positions.map(position => new google.maps.LatLng(position.latitude, position.longitude));
      line.setPath(points);
    }
  }

  getPolyLineOptions() {
    let selected = this.selected;
    let weight = selected ? 2 : 1;
    // let color = selected ? '#FF8000' : '#FF0000';
    let color = '#FF8000';
    return {
      geodesic: true,
      strokeColor: color,
      strokeOpacity: 0.8,
      strokeWeight: weight
    };
  }

  setLineOptions() {
    let line = this.line;
    if(isPresent(line)) {
      line.setOptions(this.getPolyLineOptions());
    }
  }

  @observes('map')
  mapWasSet() {
    run.once(this, 'setMap');
  }

  @observes('positions.[]')
  positionsChanged() {
    run.once(this, 'setPositions');
  }

  @observes('selected')
  selectionChanged() {
    run.once(this, 'setLineOptions');
  }
}

GMapPolyLineComponent.reopenClass({
  positionalParams: ['mapContext']
});
export default GMapPolyLineComponent;
