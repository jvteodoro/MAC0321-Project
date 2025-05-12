import React from 'react';
import { shallow, render, mount } from 'enzyme';
import SampleComponent from './SampleComponent';

describe('SampleComponent', () => {
  let props;
  let shallowSampleComponent;
  let renderedSampleComponent;
  let mountedSampleComponent;

  const shallowTestComponent = () => {
    if (!shallowSampleComponent) {
      shallowSampleComponent = shallow(<SampleComponent {...props} />);
    }
    return shallowSampleComponent;
  };

  const renderTestComponent = () => {
    if (!renderedSampleComponent) {
      renderedSampleComponent = render(<SampleComponent {...props} />);
    }
    return renderedSampleComponent;
  };

  const mountTestComponent = () => {
    if (!mountedSampleComponent) {
      mountedSampleComponent = mount(<SampleComponent {...props} />);
    }
    return mountedSampleComponent;
  };  

  beforeEach(() => {
    props = {};
    shallowSampleComponent = undefined;
    renderedSampleComponent = undefined;
    mountedSampleComponent = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
