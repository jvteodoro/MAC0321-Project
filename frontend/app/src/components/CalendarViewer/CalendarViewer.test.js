import React from 'react';
import { shallow, render, mount } from 'enzyme';
import CalendarViewer from './CalendarViewer';

describe('CalendarViewer', () => {
  let props;
  let shallowCalendarViewer;
  let renderedCalendarViewer;
  let mountedCalendarViewer;

  const shallowTestComponent = () => {
    if (!shallowCalendarViewer) {
      shallowCalendarViewer = shallow(<CalendarViewer {...props} />);
    }
    return shallowCalendarViewer;
  };

  const renderTestComponent = () => {
    if (!renderedCalendarViewer) {
      renderedCalendarViewer = render(<CalendarViewer {...props} />);
    }
    return renderedCalendarViewer;
  };

  const mountTestComponent = () => {
    if (!mountedCalendarViewer) {
      mountedCalendarViewer = mount(<CalendarViewer {...props} />);
    }
    return mountedCalendarViewer;
  };  

  beforeEach(() => {
    props = {};
    shallowCalendarViewer = undefined;
    renderedCalendarViewer = undefined;
    mountedCalendarViewer = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
