import classic from 'ember-classic-decorator';
import { observes } from '@ember-decorators/object';
import '@ember/object';
import { run } from '@ember/runloop';
import GMapMarkerComponent from 'ember-g-map/components/g-map-marker';
import planeIcons from 'adsb-client/utils/plane-icon';

@classic
export default class FlightMarker extends GMapMarkerComponent {
  selected = false;

  init() {
    super.init(...arguments);
    this.updateIcon();
  }

  getIcon() {
    let heading = this.heading;
    let selected = this.selected;
    let rotation = heading ? heading * 1.0 : 0.0;
    // let color = selected ? '#FF8000' : 'black';
    let color = '#FF8000';
    let weight = selected ? 1.2 : 1;
    let scale = selected ? 0.4 : 0.3;
    // let fillOpacity = selected ? 0.8 : 0;
    let fillOpacity = 0.8;

    return {
      scale: scale,
      anchor: new google.maps.Point(32, 32),
      path: planeIcons.defaultPlane,
      opacity: 0.8,
      fillOpacity: fillOpacity,
      strokeColor: color,
      strokeWeight: weight,
      rotation: rotation
    };
  }

  updateIcon() {
    this.set('icon', this.getIcon());
  }

  @observes('selected')
  selectedChanged() {
    run.once(this, 'updateIcon');
  }
}
