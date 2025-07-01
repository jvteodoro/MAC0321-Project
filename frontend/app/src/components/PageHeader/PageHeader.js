import React, { useState } from "react";
import "./PageHeader.css";
import NotificationList from "../NotificationList/NotificationList";
import StatsMenu from "../StatsMenu/StatsMenu";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";

const PageHeader = (props) => {
  const [notListVisible, setNotListVisible] = useState(false);
  const [statsMenuVisible, setStatsMenuVisible] = useState(false);
  const { logout } = useAuth(); // função que limpa estado do usuário

  const toggleNotificationListVisibility = () =>
    setNotListVisible(!notListVisible);

  const toggleStatsMenuVisibility = () =>
    setStatsMenuVisible(!statsMenuVisible);

  const fazerLogout = async () => {
    try {
      await axios.post("http://localhost:12003/api/auth/logout", null, {
        withCredentials: true,
      });
      logout(); // limpa estado local após logout do servidor
    } catch (err) {
      console.error("Erro ao fazer logout:", err);
    }
  };

  return (
    <header id="page-header">
      <h1>
        <a href="/">AgendUSP</a>
      </h1>
      <h1 id="page-title">{props["title"]}</h1>
      <div id="nav-buttons">
        <div className="menu-button-group">
          <button
            id="notifications-button"
            className={notListVisible ? "connected-to-list" : ""}
            onClick={toggleNotificationListVisibility}
          >
            <i className="fa-solid fa-bell"></i>
          </button>
          <NotificationList visible={notListVisible} />
        </div>
        <div className="menu-button-group">
          <button
            id="stats-button"
            className={statsMenuVisible ? "connected-to-stats" : ""}
            onClick={toggleStatsMenuVisibility}
          >
            <i className="fa-solid fa-chart-bar"></i>
          </button>
          <StatsMenu visible={statsMenuVisible} />
        </div>
        <form action={null} method="POST">
          <button 
            id="salvar"
            type="submit"
            onClick={() => {
              alert("implementação salvar ainda não feita");
            }}>
            Salvar
          </button>
        </form>
        <form action={null} method="POST">
          <button
            id="logout"
            type="button"
            onClick={fazerLogout}
          >
            Logout
          </button>
        </form>
      </div>
    </header>
  );
};

export default PageHeader;
