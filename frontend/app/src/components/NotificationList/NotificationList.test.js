import React from 'react';
import { shallow, render, mount } from 'enzyme';
import NotificationList from './NotificationList';

describe('NotificationList', () => {
  let props;
  let shallowNotificationList;
  let renderedNotificationList;
  let mountedNotificationList;

  const shallowTestComponent = () => {
    if (!shallowNotificationList) {
      shallowNotificationList = shallow(<NotificationList {...props} />);
    }
    return shallowNotificationList;
  };

  const renderTestComponent = () => {
    if (!renderedNotificationList) {
      renderedNotificationList = render(<NotificationList {...props} />);
    }
    return renderedNotificationList;
  };

  const mountTestComponent = () => {
    if (!mountedNotificationList) {
      mountedNotificationList = mount(<NotificationList {...props} />);
    }
    return mountedNotificationList;
  };  

  beforeEach(() => {
    props = {};
    shallowNotificationList = undefined;
    renderedNotificationList = undefined;
    mountedNotificationList = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
