import HalAdapter from "ember-data-hal-9000/adapter";
export default HalAdapter.extend({
  host: 'http://localhost:8080',
  headers: {
    Accept: 'application/hal+json'
  }
});
