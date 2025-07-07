import React from 'react';
import { shallow, render, mount } from 'enzyme';
import PollResultViewer from './PollResultViewer';

describe('PollResultViewer', () => {
  let props;
  let shallowPollResultViewer;
  let renderedPollResultViewer;
  let mountedPollResultViewer;

  const shallowTestComponent = () => {
    if (!shallowPollResultViewer) {
      shallowPollResultViewer = shallow(<PollResultViewer {...props} />);
    }
    return shallowPollResultViewer;
  };

  const renderTestComponent = () => {
    if (!renderedPollResultViewer) {
      renderedPollResultViewer = render(<PollResultViewer {...props} />);
    }
    return renderedPollResultViewer;
  };

  const mountTestComponent = () => {
    if (!mountedPollResultViewer) {
      mountedPollResultViewer = mount(<PollResultViewer {...props} />);
    }
    return mountedPollResultViewer;
  };  

  beforeEach(() => {
    props = {};
    shallowPollResultViewer = undefined;
    renderedPollResultViewer = undefined;
    mountedPollResultViewer = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
