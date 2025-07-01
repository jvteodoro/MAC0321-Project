import React from 'react';
import { shallow, render, mount } from 'enzyme';
import CreateEventMenu from './CreateEventMenu';

describe('CreateEventMenu', () => {
  let props;
  let shallowCreateEventMenu;
  let renderedCreateEventMenu;
  let mountedCreateEventMenu;

  const shallowTestComponent = () => {
    if (!shallowCreateEventMenu) {
      shallowCreateEventMenu = shallow(<CreateEventMenu {...props} />);
    }
    return shallowCreateEventMenu;
  };

  const renderTestComponent = () => {
    if (!renderedCreateEventMenu) {
      renderedCreateEventMenu = render(<CreateEventMenu {...props} />);
    }
    return renderedCreateEventMenu;
  };

  const mountTestComponent = () => {
    if (!mountedCreateEventMenu) {
      mountedCreateEventMenu = mount(<CreateEventMenu {...props} />);
    }
    return mountedCreateEventMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowCreateEventMenu = undefined;
    renderedCreateEventMenu = undefined;
    mountedCreateEventMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
