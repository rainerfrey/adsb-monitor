import Component from '@ember/component';

export default Component.extend({
  actions: {
    request: function() {
      this.$('#horrible_hack')[0].submit.call(this.$('#airframes_post')[0]);
      return false;
    }
  }
});
