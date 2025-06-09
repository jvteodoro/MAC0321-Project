import React from 'react';
import { shallow, render, mount } from 'enzyme';
import LoginMenu from './LoginMenu';

describe('LoginMenu', () => {
  let props;
  let shallowLoginMenu;
  let renderedLoginMenu;
  let mountedLoginMenu;

  const shallowTestComponent = () => {
    if (!shallowLoginMenu) {
      shallowLoginMenu = shallow(<LoginMenu {...props} />);
    }
    return shallowLoginMenu;
  };

  const renderTestComponent = () => {
    if (!renderedLoginMenu) {
      renderedLoginMenu = render(<LoginMenu {...props} />);
    }
    return renderedLoginMenu;
  };

  const mountTestComponent = () => {
    if (!mountedLoginMenu) {
      mountedLoginMenu = mount(<LoginMenu {...props} />);
    }
    return mountedLoginMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowLoginMenu = undefined;
    renderedLoginMenu = undefined;
    mountedLoginMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
