import Ember from "ember";

export default Ember.Component.extend({
  tagName: 'button',
  classNames: 'button',
  click: function() {
    this.get("onClick")();
  }
});
