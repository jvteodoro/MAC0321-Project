import React from "react";
import PropTypes from "prop-types";
import "./NotificationList.css";

const NotificationList = ({ visible, notifications }) => {
  return (
    <div id="notification-list" className={visible ? "extended" : ""}>
      <h3 id="list-header">Notifications</h3>
      <ul id="list-content">
        {notifications.map((notification) => (
          <li key={notification.id}>{notification.message}</li>
        ))}
      </ul>
    </div>
  );
};

NotificationList.propTypes = {
  visible: PropTypes.bool.isRequired,
  notifications: PropTypes.array.isRequired,
};

export default NotificationList;
