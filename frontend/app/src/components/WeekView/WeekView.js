import React from "react";
import PropTypes from "prop-types";
import "./WeekView.css";
import EventBlock from "../EventBlock/EventBlock";
import { useNavigate } from "react-router-dom";

const WeekView = ({ week, calendarId, events, onClose }) => {
  const navigate = useNavigate();
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

  const formatDateToShow = (dayInfo) => {
    return new Date(
      dayInfo.year,
      dayInfo.monthIndex,
      dayInfo.day
    ).toLocaleDateString("pt-BR", { day: "numeric", month: "short" });
  };

  const relatorioGerado = (firstDay, lastDay) => {
    }

  const goToCreateMenu = (clickDayInfo) => {
    navigate("evento/criar", {
      replace: true,
      state: { calendarId: calendarId, dayInfo: clickDayInfo },
    });
  };

  let firstDay = new Date(
    week[0].year,
    week[0].monthIndex,
    week[0].day
  ).toISOString();

  let lastDay = new Date(
    week[6].year,
    week[6].monthIndex,
    week[6].day
  ).toISOString();

  return (
    <div className="week-view-container">
      <div className="week-view-header">
        <h3>Visualização da semana</h3>
          <button
            onClick={() => relatorioGerado(firstDay, lastDay)}
            id="gera-relatorio"
            type="submit"
            name="generate"
            value={`${firstDay},${lastDay}`}
          >
            <p>Gerar relatório da semana</p>
            <span>(LLaMA 4)</span>
          </button>
        <button onClick={onClose} className="close-button">
          <i className="fa-solid fa-arrow-left"></i> Voltar para o Calendário
        </button>
      </div>
      <div className="week-view-grid">
        {week.map((dayInfo, index) => {
          const dayEvents = getEventsForDay(dayInfo);
          return (
            <div
              key={`week-day-${index}`}
              className={`week-view-day 
              ${dayInfo.isCurrentMonth ? "" : "other-month"} 
              ${dayInfo.isToday ? "today" : ""}`}
            >
              <div className="week-day-header">
                <span className="week-day-name">
                  {["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"][index]}
                </span>
                <span className="week-day-date">
                  {formatDateToShow(dayInfo)}
                </span>
              </div>
              <div className="week-day-number">{dayInfo.day}</div>
              <div className="week-day-schedule">
                {dayEvents.map((event, eventIndex) => (
                  <EventBlock
                    key={`event-${index}-${eventIndex}`}
                    clickable={true}
                    eventInfo={{
                      colorId: event.colorId,
                      title: event.summary,
                      startTime: event.start.dateTime
                        ? new Date(event.start.dateTime).toLocaleTimeString(
                            "pt-BR",
                            {
                              hour: "2-digit",
                              minute: "2-digit",
                            }
                          )
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
                      calendarId: calendarId,
                      eventId: event.id,
                      status: event.status
                    }}
                  />
                ))}
              </div>
              <button onClick={() => goToCreateMenu(dayInfo)}>
                Criar reunião
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

WeekView.propTypes = {
  week: PropTypes.arrayOf(
    PropTypes.shape({
      day: PropTypes.number.isRequired,
      monthIndex: PropTypes.number.isRequired,
      year: PropTypes.number.isRequired,
      isCurrentMonth: PropTypes.bool.isRequired,
      isToday: PropTypes.bool.isRequired,
    })
  ).isRequired,
  onClose: PropTypes.func.isRequired,
};

export default WeekView;
