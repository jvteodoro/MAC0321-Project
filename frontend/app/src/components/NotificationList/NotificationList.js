import React, { useState, useEffect, useRef } from "react";
import PropTypes from "prop-types";
import "./NotificationList.css";
import { Client } from "@stomp/stompjs";
import { useAuth } from "../../context/AuthContext"; // <-- import useAuth

const NotificationList = ({ visible }) => {
  const [notifications, setNotifications] = useState([]);
  const stompClient = useRef(null);
  const { authenticated, getAccessToken, user } = useAuth(); // <-- use context

  useEffect(() => {
    if (!authenticated) return; // Only connect if authenticated

    const connectWebSocket = async () => {
      const accessToken = getAccessToken();

      stompClient.current = new Client({
        brokerURL: "ws://localhost:12003/gs-guide-websocket", // <-- use your backend URL
        connectHeaders: {
          Authorization: `Bearer ${accessToken}`,
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: (str) => console.log(str),
        onConnect: () => {
          // Subscribe to notification topics
          stompClient.current.subscribe("/notify/pool/*", (message) => {
            const notification = JSON.parse(message.body);
            setNotifications((prev) => [
              {
                id: Date.now(),
                message: `Pool update: ${notification.eventId}`,
              },
              ...prev,
            ]);
          });
        },
        onStompError: (frame) => {
          console.error("STOMP error:", frame.headers.message);
        },
        onWebSocketError: (error) => {
          console.error("WebSocket error:", error);
        },
      });

      stompClient.current.activate();
    };

    connectWebSocket();

    return () => {
      if (stompClient.current) {
        stompClient.current.deactivate();
      }
    };
  }, [authenticated, getAccessToken]);

  const sendVote = (eventPoolId, dateTimeIntervalId) => {
    if (stompClient.current && stompClient.current.connected) {
      stompClient.current.publish({
        destination: `/message/pool/vote/${eventPoolId}`,
        body: JSON.stringify({ dateTimeIntervalId }),
        headers: { "content-type": "application/json" },
      });
    }
  };

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
};

NotificationList.propTypes = {
  visible: PropTypes.bool.isRequired,
};

export default NotificationList;
