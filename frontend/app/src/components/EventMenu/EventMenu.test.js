import React from 'react';
import { shallow, render, mount } from 'enzyme';
import EventMenu from './EventMenu';

describe('EventMenu', () => {
  let props;
  let shallowEventMenu;
  let renderedEventMenu;
  let mountedEventMenu;

  const shallowTestComponent = () => {
    if (!shallowEventMenu) {
      shallowEventMenu = shallow(<EventMenu {...props} />);
    }
    return shallowEventMenu;
  };

  const renderTestComponent = () => {
    if (!renderedEventMenu) {
      renderedEventMenu = render(<EventMenu {...props} />);
    }
    return renderedEventMenu;
  };

  const mountTestComponent = () => {
    if (!mountedEventMenu) {
      mountedEventMenu = mount(<EventMenu {...props} />);
    }
    return mountedEventMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowEventMenu = undefined;
    renderedEventMenu = undefined;
    mountedEventMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
