import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import "./CalendarMenu.css";
import WeekView from "../WeekView/WeekView";
import EventBlock from "../EventBlock/EventBlock";
import axios from "axios";
import { fetchNationalHolidays } from "../../utils/holidays";

const Calendar = ({ year, month }) => {
  // Estados para armazenar os calendários e eventos
  // const [calendars, setCalendars] = useState([]);
  const [events, setEvents] = useState([]);
  const [currentDate, setCurrentDate] = useState(new Date(year, month));
  const [selectedWeek, setSelectedWeek] = useState(null);
  const [calendarId, setCalendarId] = useState(null);
  const [holidays, setHolidays] = useState([]);

  // Busca os eventos quando o componente é montado
  useEffect(() => {
    const fetchData = async () => {
      try {
        // Busca a lista de calendários
        const calendarsResponse = await axios.get(
          "http://localhost:12003/calendarList/list",
          { withCredentials: true }
        );

        // Escolhe o primeiro calendário com accessRole owner ou writer
        const validCalendars = calendarsResponse.data.filter(
          (cal) =>
            cal.accessRole === "owner" ||
            cal.accessRole === "writer"
        );
        if (validCalendars.length > 0) {
          const newCalendarId = validCalendars[0].id;
          setCalendarId(newCalendarId);
          // Store in localStorage for NotificationHandler
          localStorage.setItem("currentCalendarId", newCalendarId);

          const eventsResponse = await axios.get(
            `http://localhost:12003/events/list?calendarId=${newCalendarId}`,
            { withCredentials: true }
          );
          setEvents(eventsResponse.data);
        }
      } catch (error) {
        console.error("Erro ao buscar eventos:", error);
      }
    };

    fetchData();
    resetToCurrent();
  }, []);
  useEffect(() => {
    if (
      currentDate instanceof Date &&
      !isNaN(currentDate.getFullYear())
    ) {
      fetchNationalHolidays(currentDate.getFullYear())
        .then(setHolidays)
        .catch(() => setHolidays([]));
    }
  }, [currentDate]);



  // Funções de Navegação
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

  // Pega informação da data
  const currentYear = currentDate.getFullYear();
  const currentMonth = currentDate.getMonth();
  const today = new Date();

  // Função para verificar se um evento ocorre em um dia específico
  const getEventsForDay = (dayInfo) => {
    if (!events || events.length === 0) return [];
    return events.filter((event) => {
      const eventDate = new Date(event.start.dateTime || event.start.date);
      return (
        eventDate.getDate() === dayInfo.day &&
        eventDate.getMonth() === dayInfo.monthIndex &&
        eventDate.getFullYear() === dayInfo.year
      );
    });
  };

  // Calcula a grade do calendário de forma dinâmica
  const getCalendarDays = () => {
    const firstDay = new Date(currentYear, currentMonth, 1);
    const lastDay = new Date(currentYear, currentMonth + 1, 0);

    const startDay = firstDay.getDay(); // 0 (Domingo) até 6 (Sábado)
    const daysInMonth = lastDay.getDate();

    // Calcula dias do mês anterior
    const prevMonthLastDay = new Date(currentYear, currentMonth, 0).getDate();
    const prevMonthDays = Array.from({ length: startDay }, (_, i) => ({
      day: prevMonthLastDay - startDay + i + 1,
      isCurrentMonth: false,
      isToday: false,
      monthIndex: currentMonth - 1,
      year: currentMonth === 0 ? currentYear - 1 : currentYear,
    }));

    // Calcula dias do mês atual
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

    // Junta todos os dias
    const allDays = [...prevMonthDays, ...currentMonthDays];

    // Calcula quantos dias faltam para completar a grade
    const totalDaysShown = allDays.length;
    const remainingCells =
      totalDaysShown % 7 === 0 ? 0 : 7 - (totalDaysShown % 7);

    // Adiciona dias do mês seguinte, se necessário
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

  // Agrupa dias em semanas
  const weeks = [];
  for (let i = 0; i < calendarDays.length; i += 7) {
    weeks.push(calendarDays.slice(i, i + 7));
  }

  return (
    <div id="calendar-menu">
      {selectedWeek ? (
        <WeekView
          week={selectedWeek}
          calendarId={calendarId}
          events={events}
          onClose={closeWeekView}
        />
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
              gridTemplateRows: `auto repeat(${numberOfRows}, ${
                18.35 * 5 / numberOfRows
              }%)`,
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
                {week.map((dayInfo, dayIndex) => {
                  const dayEvents = getEventsForDay(dayInfo);
                  // Monta a data no formato YYYY-MM-DD
                  const dateStr = `${dayInfo.year}-${String(dayInfo.monthIndex + 1).padStart(2, "0")}-${String(dayInfo.day).padStart(2, "0")}`;
                  const holiday = holidays.find(h => h.date === dateStr);
                  
                  return (
                    <div
                    key={`day-${weekIndex}-${dayIndex}`}
                    className={`calendar-day 
                      ${dayInfo.isCurrentMonth ? "" : "other-month"} 
                      ${dayInfo.isToday ? "today" : ""}
                      ${holiday ? "holiday" : ""}
                      `}
                      title={holiday ? holiday.name : ""}
                      >
      <span className="day-number">{dayInfo.day}</span>
      {holiday && <span className="holiday-emoji" title={holiday.name}>🎉</span>}
      {/* Renderiza um EventBlock para cada evento deste dia */}
      {dayEvents.map((event, eventIndex) => (
        <EventBlock
        key={`event-${weekIndex}-${dayIndex}-${eventIndex}`}
        eventInfo={{
          colorId: event.colorId,
          title: event.summary,
          startTime: event.start.dateTime
          ? new Date(
            event.start.dateTime
          ).toLocaleTimeString("pt-BR", {
            hour: "2-digit",
                  minute: "2-digit",
                })
                : "Dia todo",
                endTime: event.end.dateTime
                ? new Date(event.end.dateTime).toLocaleTimeString(
                  "pt-BR",
                  {
                    hour: "2-digit",
                    minute: "2-digit",
                  }
                )
                : "Dia todo",
                status: event.status
              }}
              clickable={false}
              />
            ))}
    </div>
  );
})}
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
  month: PropTypes.number, // 0-11 (Janeiro-Dezembro)
};

Calendar.defaultProps = {
  year: new Date().getFullYear(),
  month: new Date().getMonth(),
};

export default Calendar;
