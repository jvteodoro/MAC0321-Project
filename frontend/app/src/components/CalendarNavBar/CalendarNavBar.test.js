import React from 'react';
import { shallow, render, mount } from 'enzyme';
import CalendarNavBar from './CalendarNavBar';

describe('CalendarNavBar', () => {
  let props;
  let shallowCalendarNavBar;
  let renderedCalendarNavBar;
  let mountedCalendarNavBar;

  const shallowTestComponent = () => {
    if (!shallowCalendarNavBar) {
      shallowCalendarNavBar = shallow(<CalendarNavBar {...props} />);
    }
    return shallowCalendarNavBar;
  };

  const renderTestComponent = () => {
    if (!renderedCalendarNavBar) {
      renderedCalendarNavBar = render(<CalendarNavBar {...props} />);
    }
    return renderedCalendarNavBar;
  };

  const mountTestComponent = () => {
    if (!mountedCalendarNavBar) {
      mountedCalendarNavBar = mount(<CalendarNavBar {...props} />);
    }
    return mountedCalendarNavBar;
  };  

  beforeEach(() => {
    props = {};
    shallowCalendarNavBar = undefined;
    renderedCalendarNavBar = undefined;
    mountedCalendarNavBar = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
