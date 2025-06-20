import React from 'react';
import PropTypes from 'prop-types';
import "./EventMenu.css";

const EventMenu = (props) => (
  <main id="event-menu">
    <button id="close-button">
      <i class="fa-solid fa-close"></i>
    </button>
    <form id="event-data-form">
      <label htmlFor="event-name">
        Nome do Evento: <br />
        <input type="text" id="event-name"></input>
      </label>
      <label htmlFor="event-start-day">
        Data inicial: <br />
        <input type="text" id="event-start-day"></input>
      </label>
      <label htmlFor="event-start-time">
        Horário de início: <br />
        <input type="text" id="event-start-time"></input>
      </label>
      <label htmlFor="event-end-day">
        Data final: <br />
        <input type="text" id="event-end-day"></input>
      </label>
      <label htmlFor="event-end-time">
        Horário final: <br />
        <input type="text" id="event-end-time"></input>
      </label>
      <label htmlFor="event-color">
        Cor do evento: <br />
        {/* Usar usestate e useeffect */}
        <input type="color" id="event-color" value={props["color"]}></input>
      </label>

      <label htmlFor="event-meet-link">
        Link do Meet: <br />
        <input type="text" id="event-meet-link"></input>
      </label>
	  
      <button type="submit" id="save-button">
        Salvar
      </button>
    </form>
  </main>
);

const EventMenuPropTypes = {
	// always use prop types!
};

EventMenu.propTypes = EventMenuPropTypes;

export default EventMenu;
