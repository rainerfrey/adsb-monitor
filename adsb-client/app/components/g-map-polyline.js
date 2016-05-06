import Ember from "ember";
import GMapComponent from 'ember-g-map/components/g-map';

const {isEmpty, isPresent, observer, run, computed, assert} = Ember;

const GMapPolyLineComponent = Ember.Component.extend({
  selected: true,
  positionalParams: ['mapContext'],
  map: computed.alias('mapContext.map'),

  init() {
    this._super(...arguments);
    let mapContext = this.get('mapContext');
    assert('Must be inside {{#g-map}} component with context set', mapContext instanceof GMapComponent);
  },

  didInsertElement() {
    this._super();
    if (isEmpty(this.get('line'))) {
      let line = new google.maps.Polyline(this.getPolyLineOptions());
      this.set('line', line);
    }
    this.setPositions();
    this.setMap();
  },

  willDestroyElement() {
    let line = this.get('line');
    if (isPresent(line)) {
      line.setMap(null);
    }
  },

  setMap() {
    let map = this.get('map');
    let line = this.get('line');

    if (isPresent(line) && isPresent(map)) {
      line.setMap(map);
    }
  },

  unsetFromMap() {
    let line = this.get('line');
    if(isPresent(line)) {
      line.setMap(null);
    }
  },

  setPositions() {
    let line = this.get('line');
    let positions = this.get("positions");
    if (isPresent(line) && isPresent(positions)) {
      let points = positions.map(position => new google.maps.LatLng(position.latitude, position.longitude));
      line.setPath(points);
    }
  },

  getPolyLineOptions() {
    let selected = this.get("selected");
    let weight = selected ? 2 : 1;
    // let color = selected ? '#FF8000' : '#FF0000';
    let color = '#FF8000';
    return {
      geodesic: true,
      strokeColor: color,
      strokeOpacity: 0.8,
      strokeWeight: weight
    }
  },

  setLineOptions() {
    let line = this.get('line');
    if(isPresent(line)) {
      line.setOptions(this.getPolyLineOptions())
    }
  },

  mapWasSet: observer('map', function() {
    run.once(this, 'setMap');
  }),

  positionsChanged: observer('positions.[]', function () {
    run.once(this, 'setPositions');
  }),

  selectionChanged: observer('selected', function() {
    run.once(this, 'setLineOptions');
  })
});

GMapPolyLineComponent.reopenClass({
  positionalParams: ['mapContext']
});
export default GMapPolyLineComponent;
