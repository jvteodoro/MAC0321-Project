import React from 'react';
import { shallow, render, mount } from 'enzyme';
import StatsMenu from './StatsMenu';

describe('StatsMenu', () => {
  let props;
  let shallowStatsMenu;
  let renderedStatsMenu;
  let mountedStatsMenu;

  const shallowTestComponent = () => {
    if (!shallowStatsMenu) {
      shallowStatsMenu = shallow(<StatsMenu {...props} />);
    }
    return shallowStatsMenu;
  };

  const renderTestComponent = () => {
    if (!renderedStatsMenu) {
      renderedStatsMenu = render(<StatsMenu {...props} />);
    }
    return renderedStatsMenu;
  };

  const mountTestComponent = () => {
    if (!mountedStatsMenu) {
      mountedStatsMenu = mount(<StatsMenu {...props} />);
    }
    return mountedStatsMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowStatsMenu = undefined;
    renderedStatsMenu = undefined;
    mountedStatsMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
