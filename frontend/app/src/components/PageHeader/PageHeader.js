import React, { useState } from "react";
// import PropTypes from 'prop-types';
import "./PageHeader.css";
import NotificationList from "../NotificationList/NotificationList";

const PageHeader = (props) => {
  const [notListVisible, setNotListVisible] = useState(false);

  const toggleNotificationListVisibility = () =>
    setNotListVisible(!notListVisible);
  return (
    <header id="page-header">
      <h1>AgendUSP</h1>
      <h1 id="page-title">{props["title"]}</h1>
      <div id="nav-buttons">
        <button
          id="notifications-button"
		  className={notListVisible ? 'connected-to-list' : ''}
          onClick={toggleNotificationListVisibility}
        >
          <i className="fa-solid fa-bell"></i>
          <NotificationList visible={notListVisible} />
        </button>
        <form action={null} method="POST">
          <button type="submit">Logout</button>
        </form>
        <form action={null} method="POST">
          <button type="submit">Salvar</button>
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
