import React from 'react';
import { shallow, render, mount } from 'enzyme';
import NotificationItem from './NotificationItem';

describe('NotificationItem', () => {
  let props;
  let shallowNotificationItem;
  let renderedNotificationItem;
  let mountedNotificationItem;

  const shallowTestComponent = () => {
    if (!shallowNotificationItem) {
      shallowNotificationItem = shallow(<NotificationItem {...props} />);
    }
    return shallowNotificationItem;
  };

  const renderTestComponent = () => {
    if (!renderedNotificationItem) {
      renderedNotificationItem = render(<NotificationItem {...props} />);
    }
    return renderedNotificationItem;
  };

  const mountTestComponent = () => {
    if (!mountedNotificationItem) {
      mountedNotificationItem = mount(<NotificationItem {...props} />);
    }
    return mountedNotificationItem;
  };  

  beforeEach(() => {
    props = {};
    shallowNotificationItem = undefined;
    renderedNotificationItem = undefined;
    mountedNotificationItem = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
