import React from "react";
import PropTypes from "prop-types";
import "./WeekView.css";

const WeekView = ({ week, onClose }) => {
  const formatDateToShow = (dayInfo) => {
    return new Date(
      dayInfo.year,
      dayInfo.monthIndex,
      dayInfo.day
    ).toLocaleDateString("pt-BR", { day: "numeric", month: "short" });
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
        <form action={"" /* INSERIR URL PARA GERAR O RELATÓRIO */}>
          <button id="geraRelatorio" type="submit" name="generate" value={`${firstDay},${lastDay}`}>
            <p>Gerar relatório da semana</p>
            <span style={{ fontSize: `small` }}>(LLaMA 4)</span>
          </button>
        </form>
        <button onClick={onClose} className="close-button">
          <i className="fa-solid fa-arrow-left"></i> Voltar para o Calendário
        </button>
      </div>
      <div className="week-view-grid">
        {week.map((dayInfo, index) => (
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
              <span className="week-day-date">{formatDateToShow(dayInfo)}</span>
            </div>
            <div className="week-day-number">{dayInfo.day}</div>
            <div className="week-day-schedule">
              {/* Listar eventos em EventBlocks */}
            </div>
          </div>
        ))}
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
