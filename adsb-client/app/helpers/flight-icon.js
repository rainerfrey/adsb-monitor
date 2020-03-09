import {helper} from '@ember/component/helper';
import planeIcons from 'adsb-client/utils/plane-icon';

export default helper(function flightIcon([flight, selected]) {
  let heading = flight.heading;
  let rotation = heading ? heading * 1.0 : 0.0;
  // let color = selected ? '#FF8000' : 'black';
  let color = '#FF8000';
  let weight = selected ? 1.2 : 1;
  let scale = selected ? 0.4 : 0.3;
  // let fillOpacity = selected ? 0.8 : 0;
  let fillOpacity = 0.8;

  return {
    scale: scale,
    anchor: {x: 32, y: 32},
    path: planeIcons.defaultPlane,
    opacity: 0.8,
    fillOpacity: fillOpacity,
    strokeColor: color,
    strokeWeight: weight,
    rotation: rotation
  };
});
