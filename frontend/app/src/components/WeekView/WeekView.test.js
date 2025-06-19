import React from 'react';
import { shallow, render, mount } from 'enzyme';
import WeekView from './WeekView';

describe('WeekView', () => {
  let props;
  let shallowWeekView;
  let renderedWeekView;
  let mountedWeekView;

  const shallowTestComponent = () => {
    if (!shallowWeekView) {
      shallowWeekView = shallow(<WeekView {...props} />);
    }
    return shallowWeekView;
  };

  const renderTestComponent = () => {
    if (!renderedWeekView) {
      renderedWeekView = render(<WeekView {...props} />);
    }
    return renderedWeekView;
  };

  const mountTestComponent = () => {
    if (!mountedWeekView) {
      mountedWeekView = mount(<WeekView {...props} />);
    }
    return mountedWeekView;
  };  

  beforeEach(() => {
    props = {};
    shallowWeekView = undefined;
    renderedWeekView = undefined;
    mountedWeekView = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
