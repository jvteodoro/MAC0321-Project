import React from 'react';
import PropTypes from 'prop-types';
import styles from './CalendarViewer.css';
import Calendar from '../Calendar/Calendar';

const CalendarViewer = (props) => (
  <div id="calendar-viewer">
    <button id="backward-calendar-month">
      <i class="fa-solid fa-arrow-left"></i>
    </button>
    <Calendar />
    <button id="forward-calendar-month">
      <i class="fa-solid fa-arrow-right"></i>
    </button>
  </div>
);


const CalendarViewerPropTypes = {
	// always use prop types!
};

CalendarViewer.propTypes = CalendarViewerPropTypes;

export default CalendarViewer;
