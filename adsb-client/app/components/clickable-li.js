import Component from '@ember/component';

export default Component.extend({
  tagName: 'button',
  classNames: 'button',
  click: function() {
    this.onClick();
  }
});
