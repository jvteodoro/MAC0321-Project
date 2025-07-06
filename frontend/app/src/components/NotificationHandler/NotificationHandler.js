import React, { useState, useRef, useEffect } from "react";
import NotificationList from "./NotificationList";
import { useAuth } from "../../context/AuthContext";
import { Client } from "@stomp/stompjs";
import axios from "axios";

const NotificationHandler = () => {
  const [notListVisible, setNotListVisible] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const stompClient = useRef(null);
  const { authenticated, getAccessToken, user } = useAuth();

  // Fetch notifications from backend
  const fetchNotifications = async () => {
    if (!authenticated) return;
    try {
      const res = await axios.get("http://localhost:12003/api/notifications/me", {
        withCredentials: true,
      });
      setNotifications(res.data || []);
    } catch (err) {
      setNotifications([]);
    }
  };

  // WebSocket: trigger fetch on notification
  useEffect(() => {
    if (!authenticated) return;
    const accessToken = getAccessToken();
    stompClient.current = new Client({
      brokerURL: "ws://localhost:12003/gs-guide-websocket",
      connectHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: () => {},
      onConnect: () => {
        stompClient.current.subscribe("/notify/poll/*", () => {
          fetchNotifications();
        });
        if (user?.id) {
          stompClient.current.subscribe(
            `/user/${user.id}/queue/notifications`,
            () => {
              fetchNotifications();
            }
          );
        }
      },
      onStompError: () => {},
      onWebSocketError: () => {},
    });
    stompClient.current.activate();
    fetchNotifications();
    return () => {
      if (stompClient.current) {
        if (stompClient.current.active) {
          stompClient.current.deactivate();
        }
        stompClient.current = null;
      }
    };
    // eslint-disable-next-line
  }, [authenticated, getAccessToken, user]);

  const toggleNotificationListVisibility = () => {
    setNotListVisible((prev) => !prev);
    if (!notListVisible) fetchNotifications();
  };

  return (
    <>
      <button
        id="notifications-button"
        className={notListVisible ? "connected-to-list" : ""}
        onClick={toggleNotificationListVisibility}
      >
        <i className="fa-solid fa-bell"></i>
        {notifications.length > 0 && (
          <span className="notification-badge">{notifications.length}</span>
        )}
      </button>
      <NotificationList
        visible={notListVisible}
        notifications={notifications}
      />
    </>
  );
};

export default NotificationHandler;
