import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from './NotificationList.css';

import axios from 'axios';

const NotificationList = ({ visible }) => {

  const [notifications, setNotifications] = useState([]);
  
  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await axios.get('/api/notifications'); // temporário
        setNotifications(response.data);
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    };
    
    // Initial fetch
    fetchNotifications();
    
    // Set up polling (every 30 seconds)
    const intervalId = setInterval(fetchNotifications, 30000);
    
    // Clean up interval on component unmount
    return () => clearInterval(intervalId);
  }, []);

  return (
    <div id="notification-list" className={(visible ? 'extended' : '')}>
      <h3 id='list-header'>Notifications</h3>
      <ul id='list-content'>
        {notifications.map(notification => (
          <li key={notification.id}>{notification.message}</li>
        ))}
      </ul>
    </div>
  );
};

const NotificationListPropTypes = {
	// always use prop types!
};

NotificationList.propTypes = NotificationListPropTypes;

export default NotificationList;
