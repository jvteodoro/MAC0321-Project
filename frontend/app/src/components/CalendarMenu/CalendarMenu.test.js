import React from 'react';
import { shallow, render, mount } from 'enzyme';
import CalendarMenu from './CalendarMenu';

describe('CalendarMenu', () => {
  let props;
  let shallowCalendarMenu;
  let renderedCalendarMenu;
  let mountedCalendarMenu;

  const shallowTestComponent = () => {
    if (!shallowCalendarMenu) {
      shallowCalendarMenu = shallow(<CalendarMenu {...props} />);
    }
    return shallowCalendarMenu;
  };

  const renderTestComponent = () => {
    if (!renderedCalendarMenu) {
      renderedCalendarMenu = render(<CalendarMenu {...props} />);
    }
    return renderedCalendarMenu;
  };

  const mountTestComponent = () => {
    if (!mountedCalendarMenu) {
      mountedCalendarMenu = mount(<CalendarMenu {...props} />);
    }
    return mountedCalendarMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowCalendarMenu = undefined;
    renderedCalendarMenu = undefined;
    mountedCalendarMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
