import React from 'react';
import { shallow, render, mount } from 'enzyme';
import EventBlock from './EventBlock';

describe('EventBlock', () => {
  let props;
  let shallowEventBlock;
  let renderedEventBlock;
  let mountedEventBlock;

  const shallowTestComponent = () => {
    if (!shallowEventBlock) {
      shallowEventBlock = shallow(<EventBlock {...props} />);
    }
    return shallowEventBlock;
  };

  const renderTestComponent = () => {
    if (!renderedEventBlock) {
      renderedEventBlock = render(<EventBlock {...props} />);
    }
    return renderedEventBlock;
  };

  const mountTestComponent = () => {
    if (!mountedEventBlock) {
      mountedEventBlock = mount(<EventBlock {...props} />);
    }
    return mountedEventBlock;
  };  

  beforeEach(() => {
    props = {};
    shallowEventBlock = undefined;
    renderedEventBlock = undefined;
    mountedEventBlock = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
