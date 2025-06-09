import React from 'react';
import PropTypes from 'prop-types';
import styles from './CalendarNavBar.css';

const CalendarNavBar = (props) => (
  <nav id="calendar-nav">
    <div id="year-changer">
      <button id="backward-navbar-year">
        <i class="fa-solid fa-arrow-left"></i>
      </button>
      <select id="year-select">
        <option>2025</option> {/* Get from outsite input (API) */}
        {/* ... */}
      </select>
      <button id="forward-navbar-year">
        <i class="fa-solid fa-arrow-right"></i>
      </button>
    </div>
    <button type="submit">Salvar</button>
    <select id="month-select">
      <option value={1}>Janeiro</option>
      <option value={2}>Fevereiro</option>
      <option value={3}>Março</option>
      <option value={4}>Abril</option>
      <option value={5}>Maio</option>
      <option value={6}>Junho</option>
      <option value={7}>Julho</option>
      <option value={8}>Agosto</option>
      <option value={9}>Setembro</option>
      <option value={10}>Outubro</option>
      <option value={11}>Novembro</option>
      <option value={12}>Dezembro</option>
    </select>
  </nav>
);

const CalendarNavBarPropTypes = {
	// always use prop types!
};

CalendarNavBar.propTypes = CalendarNavBarPropTypes;

export default CalendarNavBar;
