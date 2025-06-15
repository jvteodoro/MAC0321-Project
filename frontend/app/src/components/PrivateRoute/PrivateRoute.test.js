import React from 'react';
import { shallow, render, mount } from 'enzyme';
import PrivateRoute from './PrivateRoute';

describe('PrivateRoute', () => {
  let props;
  let shallowPrivateRoute;
  let renderedPrivateRoute;
  let mountedPrivateRoute;

  const shallowTestComponent = () => {
    if (!shallowPrivateRoute) {
      shallowPrivateRoute = shallow(<PrivateRoute {...props} />);
    }
    return shallowPrivateRoute;
  };

  const renderTestComponent = () => {
    if (!renderedPrivateRoute) {
      renderedPrivateRoute = render(<PrivateRoute {...props} />);
    }
    return renderedPrivateRoute;
  };

  const mountTestComponent = () => {
    if (!mountedPrivateRoute) {
      mountedPrivateRoute = mount(<PrivateRoute {...props} />);
    }
    return mountedPrivateRoute;
  };  

  beforeEach(() => {
    props = {};
    shallowPrivateRoute = undefined;
    renderedPrivateRoute = undefined;
    mountedPrivateRoute = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
