import React from 'react';
import { shallow, render, mount } from 'enzyme';
import EditEventMenu from './EditEventMenu';

describe('EditEventMenu', () => {
  let props;
  let shallowEditEventMenu;
  let renderedEditEventMenu;
  let mountedEditEventMenu;

  const shallowTestComponent = () => {
    if (!shallowEditEventMenu) {
      shallowEditEventMenu = shallow(<EditEventMenu {...props} />);
    }
    return shallowEditEventMenu;
  };

  const renderTestComponent = () => {
    if (!renderedEditEventMenu) {
      renderedEditEventMenu = render(<EditEventMenu {...props} />);
    }
    return renderedEditEventMenu;
  };

  const mountTestComponent = () => {
    if (!mountedEditEventMenu) {
      mountedEditEventMenu = mount(<EditEventMenu {...props} />);
    }
    return mountedEditEventMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowEditEventMenu = undefined;
    renderedEditEventMenu = undefined;
    mountedEditEventMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
