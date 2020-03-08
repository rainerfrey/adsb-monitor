import classic from 'ember-classic-decorator';
import { computed } from '@ember/object';
import Service from '@ember/service';
@classic
export default class MapService extends Service {
  @computed
  get isLoaded() {
    return window.google !== undefined;
  }

  @computed
  get defaultMapType() {
    return google !== undefined ? google.maps.MapTypeId.TERRAIN : "";
  }
}
