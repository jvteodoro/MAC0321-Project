import React from "react";
import PropTypes from "prop-types";
import { useNavigate } from "react-router-dom";
import styles from "./NotificationItem.css";

const NotificationItem = ({notification}) => {
  const navigate = useNavigate();
  const handleGoClick = () => {
    // If notification.eventPollId exists, go to /votar with eventId = eventPollId
    if (notification.eventPollId) {
      navigate("/votar", {
        replace: true,
        state: {
          userId: notification.userId,
          eventId: notification.eventPollId,
          calendarId: notification.calendarId
        }
      });
    }
  };

  return (
    <li id={notification.id}>
      {notification.message}
      {notification.eventPollId != null && (
        <button className="goButton" onClick={handleGoClick}>Ir</button>
      )}
    </li>
  );
};

NotificationItem.propTypes = {
  notification: PropTypes.shape({
    id: PropTypes.any,
    message: PropTypes.string.isRequired,
    eventPollId: PropTypes.any,
    calendarId: PropTypes.any,
    // ...other possible notification props...
  }).isRequired,
};

export default NotificationItem;
