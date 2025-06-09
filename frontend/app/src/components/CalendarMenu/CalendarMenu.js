import React from 'react';
import PropTypes from 'prop-types';
import styles from './CalendarMenu.css';
import CalendarNavBar from '../CalendarNavBar/CalendarNavBar';
import CalendarViewer from '../CalendarViewer/CalendarViewer';

const CalendarMenu = props => (
	<div id='calendar-menu'>
		<CalendarNavBar />
		<CalendarViewer />
	</div>
);

const CalendarMenuPropTypes = {
	// always use prop types!
};

CalendarMenu.propTypes = CalendarMenuPropTypes;

export default CalendarMenu;
