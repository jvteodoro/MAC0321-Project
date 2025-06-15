import React from 'react';
import PropTypes from 'prop-types';
import styles from './LoginMenu.css';
import Google_Icon from '../../assets/Google_Icon.webp'

import axios from "axios";

const LoginMenu = (props) => (
  <div id="menu-wrapper" className="modal">
    <h1>AgendUSP</h1>
    <div id="login-menu" className="modal">
      <h2 id="login-header">Login</h2>
      <form id="login-form">
        <div id="form-inputs">
          <input type="email" placeholder="example@email.com"></input>
          <input type="password" placeholder="password"></input>
        </div>
        <button id="login" type="submit">
          Entrar
        </button>
        <button
          id="googleSignIn"
          type="button"
          onClick={() => {window.location.href =
            "http://localhost:12003/oauth2/authorization/Google";}}
        >
          <img src={Google_Icon}></img>
          Entrar com o Google
        </button>
      </form>
    </div>
  </div>
);

const LoginMenuPropTypes = {
	// always use prop types!
};

LoginMenu.propTypes = LoginMenuPropTypes;

export default LoginMenu;
