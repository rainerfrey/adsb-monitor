import HalAdapter from "ember-data-hal-9000/adapter";
import ENV from "adsb-client/config/environment";

export default HalAdapter.extend({
  host: ENV.adsb.server,
  headers: {
    Accept: 'application/hal+json'
  }
});
