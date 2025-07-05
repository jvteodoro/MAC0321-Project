import React from "react";
import PropTypes from "prop-types";
import { useNavigate } from "react-router-dom";
import styles from "./NotificationItem.css";

const NotificationItem = (notification, key) => {
  const navigate = useNavigate();

  const handleGoClick = () => {
    // If notification.linkedObjectId exists, go to /votar with eventId = linkedObjectId
    if (notification.linkedObjectId) {
      navigate("/votar", {
        replace: true,
        state: { eventId: notification.linkedObjectId }
      });
    }
  };

  return (
    <li id={notification.id} key={key}>
      {notification.message}
      {notification.linkedObjectId != null && (
        <button className="goButton" onClick={handleGoClick}>Ir</button>
      )}
    </li>
  );
};

NotificationItem.propTypes = {
  notification: PropTypes.shape({
    id: PropTypes.any,
    message: PropTypes.string.isRequired,
    linkedObjectId: PropTypes.any,
    // ...other possible notification props...
  }).isRequired,
};

export default NotificationItem;
