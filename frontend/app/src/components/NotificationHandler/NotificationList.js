import React from "react";
import PropTypes from "prop-types";
import "./NotificationList.css";
import NotificationItem from "./NotificationItem/NotificationItem";

const NotificationList = ({ visible, notifications }) => {
  return (
    <div id="notification-list" className={visible ? "extended" : ""}>
      <h3 id="list-header">Notifications</h3>
      <ul id="list-content">
        {notifications.map((notification) => (
          <NotificationItem notification={notification} key={notification.id} />
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
