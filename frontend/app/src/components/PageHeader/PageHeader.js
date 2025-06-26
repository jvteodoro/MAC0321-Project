import React, { useState } from "react";
// import PropTypes from 'prop-types';
import "./PageHeader.css";
import NotificationList from "../NotificationList/NotificationList";
import StatsMenu from "../StatsMenu/StatsMenu";

const PageHeader = (props) => {
  const [notListVisible, setNotListVisible] = useState(false);
  const [statsMenuVisible, setStatsMenuVisible] = useState(false);

  const toggleNotificationListVisibility = () =>
    setNotListVisible(!notListVisible);

  const toggleStatsMenuVisibility = () =>
    setStatsMenuVisible(!statsMenuVisible);

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
          <button type="submit">Salvar</button>
        </form>
        <form action={null} method="POST">
          <button type="submit">Logout</button>
        </form>
      </div>
    </header>
  );
};

const PageHeaderPropTypes = {
  // always use prop types!
};

PageHeader.propTypes = PageHeaderPropTypes;

export default PageHeader;
