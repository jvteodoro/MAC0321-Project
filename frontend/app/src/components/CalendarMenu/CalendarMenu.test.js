import React from 'react';
import { shallow, render, mount } from 'enzyme';
import Calendar from './CalendarMenu';

describe('Calendar', () => {
  let props;
  let shallowCalendar;
  let renderedCalendar;
  let mountedCalendar;

  const shallowTestComponent = () => {
    if (!shallowCalendar) {
      shallowCalendar = shallow(<Calendar {...props} />);
    }
    return shallowCalendar;
  };

  const renderTestComponent = () => {
    if (!renderedCalendar) {
      renderedCalendar = render(<Calendar {...props} />);
    }
    return renderedCalendar;
  };

  const mountTestComponent = () => {
    if (!mountedCalendar) {
      mountedCalendar = mount(<Calendar {...props} />);
    }
    return mountedCalendar;
  };  

  beforeEach(() => {
    props = {};
    shallowCalendar = undefined;
    renderedCalendar = undefined;
    mountedCalendar = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
