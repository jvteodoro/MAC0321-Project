import React from 'react';
import { shallow, render, mount } from 'enzyme';
import LoginSuccess from './LoginSuccess';

describe('LoginSuccess', () => {
  let props;
  let shallowLoginSuccess;
  let renderedLoginSuccess;
  let mountedLoginSuccess;

  const shallowTestComponent = () => {
    if (!shallowLoginSuccess) {
      shallowLoginSuccess = shallow(<LoginSuccess {...props} />);
    }
    return shallowLoginSuccess;
  };

  const renderTestComponent = () => {
    if (!renderedLoginSuccess) {
      renderedLoginSuccess = render(<LoginSuccess {...props} />);
    }
    return renderedLoginSuccess;
  };

  const mountTestComponent = () => {
    if (!mountedLoginSuccess) {
      mountedLoginSuccess = mount(<LoginSuccess {...props} />);
    }
    return mountedLoginSuccess;
  };  

  beforeEach(() => {
    props = {};
    shallowLoginSuccess = undefined;
    renderedLoginSuccess = undefined;
    mountedLoginSuccess = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
