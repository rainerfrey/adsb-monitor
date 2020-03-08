import classic from 'ember-classic-decorator';
import AjaxService from 'ember-ajax/services/ajax';
import ENV from 'adsb-client/config/environment';

@classic
export default class _AjaxService extends AjaxService {
  host = ENV.adsb.server;
}
