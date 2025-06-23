import React from 'react';
import { shallow, render, mount } from 'enzyme';
import VoteMenu from './VoteMenu';

describe('VoteMenu', () => {
  let props;
  let shallowVoteMenu;
  let renderedVoteMenu;
  let mountedVoteMenu;

  const shallowTestComponent = () => {
    if (!shallowVoteMenu) {
      shallowVoteMenu = shallow(<VoteMenu {...props} />);
    }
    return shallowVoteMenu;
  };

  const renderTestComponent = () => {
    if (!renderedVoteMenu) {
      renderedVoteMenu = render(<VoteMenu {...props} />);
    }
    return renderedVoteMenu;
  };

  const mountTestComponent = () => {
    if (!mountedVoteMenu) {
      mountedVoteMenu = mount(<VoteMenu {...props} />);
    }
    return mountedVoteMenu;
  };  

  beforeEach(() => {
    props = {};
    shallowVoteMenu = undefined;
    renderedVoteMenu = undefined;
    mountedVoteMenu = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
