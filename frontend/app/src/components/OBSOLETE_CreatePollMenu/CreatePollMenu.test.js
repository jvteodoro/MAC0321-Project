import React from 'react';
import { shallow, render, mount } from 'enzyme';
import CreatePollMenu from './CreatePollMenu';

describe('CreatePollMenu', () => {
  let props;
  let shallowCreatePollMenu;
  let renderedCreatePollMenu;
  let mountedCreatePollMenu;

  const shallowTestComponent = () => {
    if (!shallowCreatePollMenu) {
      shallowCreatePollMenu = shallow(<CreatePollMenu {...props} />);
    }
    return shallowCreatePollMenu;
  };

  const renderTestComponent = () => {
    if (!renderedCreatePollMenu) {
      renderedCreatePollMenu = render(<CreatePollMenu {...props} />);
    }
    return renderedCreatePollMenu;
  };

  const mountTestComponent = () => {
    if (!mountedCreatePollMenu) {
      mountedCreatePollMenu = mount(<CreatePollMenu {...props} />);
    }
    return mountedCreatePollMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowCreatePollMenu = undefined;
    renderedCreatePollMenu = undefined;
    mountedCreatePollMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
