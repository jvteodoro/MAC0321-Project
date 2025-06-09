import React from 'react';
import PropTypes from 'prop-types';
import styles from './Calendar.css';

const Calendar = props => (
	<div id='calendar'>
		{/* <div id='days-of-week'> */}
			<h3>Dom</h3>
			<h3>Seg</h3>
			<h3>Ter</h3>
			<h3>Qua</h3>
			<h3>Qui</h3>
			<h3>Sex</h3>
			<h3>Sáb</h3>
		{/* </div> */}
	</div>
);

const CalendarPropTypes = {
	// always use prop types!
};

Calendar.propTypes = CalendarPropTypes;

export default Calendar;
