/* jshint node: true */

module.exports = function(environment) {
  var ENV = {
    modulePrefix: 'adsb-client',
    environment: environment,
    baseURL: '/',
    locationType: 'hash',
    EmberENV: {
      FEATURES: {
        // Here you can enable experimental features on an ember canary build
        // e.g. 'with-controller': true
      },
      ENABLE_DS_FILTER: true
    },

    APP: {
      // Here you can pass flags/options to your application instance
      // when it is created
    },

    'g-map': {
      key: 'AIzaSyDN77dL_9QXOqlcmtJTnAwkKaOHaH_QYMA'
    },
    moment: {
      // allowEmpty: true
    },
    adsb: {
      home: {
        lat: 47.859541,
        lng: 7.693784
      },
      timeframe: 15,
      liveMonitoring: true,
      server: "http://localhost:8080"
    }
  };

  if (environment === 'development') {
    // ENV.APP.LOG_RESOLVER = true;
    // ENV.APP.LOG_ACTIVE_GENERATION = true;
    // ENV.APP.LOG_TRANSITIONS = true;
    // ENV.APP.LOG_TRANSITIONS_INTERNAL = true;
    // ENV.APP.LOG_VIEW_LOOKUPS = true;
  }

  if (environment === 'test') {
    // Testem prefers this...
    ENV.baseURL = '/';
    ENV.locationType = 'none';

    // keep test console output quieter
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;

    ENV.APP.rootElement = '#ember-testing';
  }

  if (environment === 'production') {
    ENV.adsb.server = "http://pi3:8080"
  }

  return ENV;
};
