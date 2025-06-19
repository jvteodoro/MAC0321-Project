import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import "./CalendarMenu.css";
import WeekView from "../WeekView/WeekView";

const Calendar = ({ year, month }) => {
  const [currentDate, setCurrentDate] = useState(new Date(year, month));
  const [selectedWeek, setSelectedWeek] = useState(null);

  useEffect(() => {
    resetToCurrent();
  }, []);

  // Navigation functions
  const prevMonth = () => {
    setCurrentDate(
      new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1)
    );
  };

  const nextMonth = () => {
    setCurrentDate(
      new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1)
    );
  };

  const resetToCurrent = () => {
    setCurrentDate(new Date());
  };

  const handleWeekClick = (week) => {
    setSelectedWeek(week);
  };

  const closeWeekView = () => {
    setSelectedWeek(null);
  };

  // Get date information
  const currentYear = currentDate.getFullYear();
  const currentMonth = currentDate.getMonth();
  const today = new Date();

  // Calculate the calendar grid with dynamic rows
  const getCalendarDays = () => {
    const firstDay = new Date(currentYear, currentMonth, 1);
    const lastDay = new Date(currentYear, currentMonth + 1, 0);

    const startDay = firstDay.getDay(); // 0 (Sunday) to 6 (Saturday)
    const daysInMonth = lastDay.getDate();

    // Calculate days from previous month to show
    const prevMonthLastDay = new Date(currentYear, currentMonth, 0).getDate();
    const prevMonthDays = Array.from({ length: startDay }, (_, i) => ({
      day: prevMonthLastDay - startDay + i + 1,
      isCurrentMonth: false,
      isToday: false,
      monthIndex: currentMonth - 1,
      year: currentMonth === 0 ? currentYear - 1 : currentYear,
    }));

    // Current month days
    const currentMonthDays = Array.from({ length: daysInMonth }, (_, i) => ({
      day: i + 1,
      isCurrentMonth: true,
      isToday:
        i + 1 === today.getDate() &&
        currentMonth === today.getMonth() &&
        currentYear === today.getFullYear(),
      monthIndex: currentMonth,
      year: currentYear,
    }));

    // Combine all days
    const allDays = [...prevMonthDays, ...currentMonthDays];

    // Calculate how many days we've shown and how many empty cells we need
    const totalDaysShown = allDays.length;
    const remainingCells =
      totalDaysShown % 7 === 0 ? 0 : 7 - (totalDaysShown % 7);

    // Add days from next month if needed
    const nextMonthDays = Array.from({ length: remainingCells }, (_, i) => ({
      day: i + 1,
      isCurrentMonth: false,
      isToday: false,
      monthIndex: currentMonth + 1,
      year: currentMonth === 11 ? currentYear + 1 : currentYear,
    }));

    return [...allDays, ...nextMonthDays];
  };

  const calendarDays = getCalendarDays();
  const numberOfRows = Math.ceil(calendarDays.length / 7);

  // Group days into weeks
  const weeks = [];
  for (let i = 0; i < calendarDays.length; i += 7) {
    weeks.push(calendarDays.slice(i, i + 7));
  }

  return (
    <div id="calendar-menu">
      {selectedWeek ? (
        <WeekView week={selectedWeek} onClose={closeWeekView} />
      ) : (
        <>
          <div className="calendar-header">
            <button onClick={prevMonth} className="nav-button">
              <i className="fa-solid fa-arrow-left"></i>
            </button>
            <h2 onClick={resetToCurrent} className="month-title">
              {currentDate.toLocaleDateString("pt-BR", {
                month: "long",
                year: "numeric",
              })}
              {currentMonth === today.getMonth() &&
                currentYear === today.getFullYear() && (
                  <span className="current-month-indicator">•</span>
                )}
            </h2>
            <button onClick={nextMonth} className="nav-button">
              <i className="fa-solid fa-arrow-right"></i>
            </button>
          </div>

          <div
            id="calendar"
            style={{
              gridTemplateRows: `auto repeat(${numberOfRows}, 1fr)`,
            }}
          >
            {/* Weekday headers */}
            <div id="weekday-headers">
              <h3 className="weekday-header">Dom</h3>
              <h3 className="weekday-header">Seg</h3>
              <h3 className="weekday-header">Ter</h3>
              <h3 className="weekday-header">Qua</h3>
              <h3 className="weekday-header">Qui</h3>
              <h3 className="weekday-header">Sex</h3>
              <h3 className="weekday-header">Sáb</h3>
            </div>

            {/* Render weeks */}
            {weeks.map((week, weekIndex) => (
              <div
                key={`week-${weekIndex}`}
                id={`week-${weekIndex}`}
                className="week"
                onClick={() => handleWeekClick(week)}
              >
                {week.map((dayInfo, dayIndex) => (
                  <div
                    key={`day-${weekIndex}-${dayIndex}`}
                    className={`calendar-day 
                      ${dayInfo.isCurrentMonth ? "" : "other-month"} 
                      ${dayInfo.isToday ? "today" : ""}`}
                  >
                    <span className="day-number">{dayInfo.day}</span>
                  </div>
                ))}
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
};

Calendar.propTypes = {
  year: PropTypes.number,
  month: PropTypes.number, // 0-11 (January-December)
};

Calendar.defaultProps = {
  year: new Date().getFullYear(),
  month: new Date().getMonth(),
};

export default Calendar;
