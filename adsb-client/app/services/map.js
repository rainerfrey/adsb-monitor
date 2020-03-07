import { computed } from '@ember/object';
import Service from '@ember/service';
export default Service.extend({
  isLoaded: computed(function() {
    return window.google !== undefined;
  }),

  defaultMapType: computed(function() {
    return google !== undefined ? google.maps.MapTypeId.TERRAIN : "";
  })
});
