import React, { useState, useEffect, useRef } from "react";
import PropTypes from "prop-types";
import "./NotificationList.css";
import { Client } from "@stomp/stompjs";
import { useAuth } from "../../context/AuthContext";
import axios from "axios";

const NotificationList = ({ visible }) => {
  const [notifications, setNotifications] = useState([]);
  const stompClient = useRef(null);
  const { authenticated, getAccessToken, user } = useAuth();

  useEffect(() => {
    if (!authenticated) return;

    const connectWebSocket = async () => {
      const accessToken = getAccessToken();

      stompClient.current = new Client({
        brokerURL: "ws://localhost:12003/gs-guide-websocket",
        connectHeaders: {
          Authorization: `Bearer ${accessToken}`,
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        debug: (str) => console.log(str),
        onConnect: () => {
          // Existing pool subscription
          stompClient.current.subscribe("/notify/pool/*", (message) => {
            const notification = JSON.parse(message.body);
            const notifObj = {
              id: Date.now(),
              message: `Pool update: ${notification.eventId}`,
            };
            setNotifications((prev) => [notifObj, ...prev]);
            alert(notifObj.message); // Alert on pool notification
          });

          // user-specific notification subscription
          if (user?.id) {
            stompClient.current.subscribe(
              `/user/${user.id}/queue/notifications`,
              (message) => {
                const notification = JSON.parse(message.body);
                const notifObj = {
                  id: notification.id || Date.now(),
                  message: notification.message,
                };
                setNotifications((prev) => [notifObj, ...prev]);
                alert(notifObj.message); // Alert on user notification
              }
            );
          }
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
        if (stompClient.current.active) {
          stompClient.current.deactivate();
        }
        stompClient.current = null;
      }
    };
  }, [authenticated, getAccessToken, user]);

  useEffect(() => {
    if (authenticated && visible) {
      axios
        .get("http://localhost:12003/api/notifications/me", {
          withCredentials: true,
        })
        .then((res) => {
          const data = res.data;
          const newNotifications = data
            .filter(
              (n) =>
                !notifications.some(
                  (p) => p.id === n.id && p.message === n.message
                )
            )
            .map((n) => ({
              id: n.id || Date.now() + Math.random(),
              message: n.message,
            }));
          setNotifications((prev) => [...newNotifications, ...prev]);
        })
        .catch((err) => console.error("Falha ao buscar as notificações", err));
    }
    // eslint-disable-next-line
  }, [authenticated, visible, user]);

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

export default NotificationList;
