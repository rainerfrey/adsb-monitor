import AjaxService from 'ember-ajax/services/ajax';
import ENV from 'adsb-client/config/environment';

export default AjaxService.extend({
  host: ENV.adsb.server
});
