import Ember from 'ember';
export default Ember.Service.extend({
  isLoaded: function() {
    return window.google !== undefined
  }.property(),

  defaultMapType: function() {
    return google !== undefined ? google.maps.MapTypeId.TERRAIN : "";
  }.property()
});
