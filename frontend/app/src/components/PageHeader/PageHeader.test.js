import React from 'react';
import { shallow, render, mount } from 'enzyme';
import PageHeader from './PageHeader';

describe('PageHeader', () => {
  let props;
  let shallowPageHeader;
  let renderedPageHeader;
  let mountedPageHeader;

  const shallowTestComponent = () => {
    if (!shallowPageHeader) {
      shallowPageHeader = shallow(<PageHeader {...props} />);
    }
    return shallowPageHeader;
  };

  const renderTestComponent = () => {
    if (!renderedPageHeader) {
      renderedPageHeader = render(<PageHeader {...props} />);
    }
    return renderedPageHeader;
  };

  const mountTestComponent = () => {
    if (!mountedPageHeader) {
      mountedPageHeader = mount(<PageHeader {...props} />);
    }
    return mountedPageHeader;
  };  

  beforeEach(() => {
    props = {};
    shallowPageHeader = undefined;
    renderedPageHeader = undefined;
    mountedPageHeader = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
