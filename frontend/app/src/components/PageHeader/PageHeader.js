import React, { useState } from "react";
import "./PageHeader.css";
import NotificationHandler from "../NotificationHandler/NotificationHandler";
import StatsMenu from "../StatsMenu/StatsMenu";
import { useAuth } from "../../context/AuthContext";

const PageHeader = (props) => {
  const [statsMenuVisible, setStatsMenuVisible] = useState(false);
  const { logout, userId } = useAuth();

  const toggleStatsMenuVisibility = () =>
    setStatsMenuVisible(!statsMenuVisible);

  const fazerLogout = async () => {
    try {
      await logout();
      window.location.reload();
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
          <NotificationHandler />
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
            }}
          >
            Salvar
          </button>
        </form>
        <form action={null} method="POST">
          <button id="logout" type="button" onClick={fazerLogout}>
            Logout
          </button>
        </form>
      </div>
    </header>
  );
};

export default PageHeader;
