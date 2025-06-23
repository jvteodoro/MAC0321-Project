import React, { useState } from "react";
import PropTypes from "prop-types";
import "./StatsMenu.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";

const StatsMenu = ({ visible }) => {
  const [start, setStart] = useState(null);
  const [end, setEnd] = useState(null);
  const [loading, setLoading] = useState(false);
  const [stats, setStats] = useState(null);
  const [error, setError] = useState(null);

  // Send date-times in ISO 8601 format: 'YYYY-MM-DDTHH:mm:ss'
  function formatLocalDateTime(date) {
    if (!date) return "";
    const pad = (n) => n.toString().padStart(2, "0");
    return (
      date.getFullYear() +
      "-" +
      pad(date.getMonth() + 1) +
      "-" +
      pad(date.getDate()) +
      "T" +
      pad(date.getHours()) +
      ":" +
      pad(date.getMinutes()) +
      ":" +
      pad(date.getSeconds())
    );
  }

  const handleGenerate = async () => {
    setLoading(true);
    setStats(null);
    setError(null);
    try {
      const params = {
        start: formatLocalDateTime(start),
        end: formatLocalDateTime(end),
      };
      const response = await axios.get(
        `http://localhost:12003/stats`,
        {
          params,
          withCredentials: true
        }
      );
      setStats(response.data);
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao gerar estatísticas");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div id="stats-menu" className={visible ? "extended" : ""}>
      <h3 id="stats-header">Estatísticas</h3>
      <div className="stats-inputs">
        <label>
          Início:
          <DatePicker
            selected={start}
            onChange={setStart}
            showTimeSelect
            dateFormat="dd/MM/yyyy HH:mm"
            timeFormat="HH:mm"
            placeholderText="Selecione início"
            minTime={new Date(0, 0, 0, 0, 0, 0)}
            maxTime={new Date(0, 0, 0, 23, 59, 59)}
          />
        </label>
        <label>
          Fim:
          <DatePicker
            selected={end}
            onChange={setEnd}
            showTimeSelect
            dateFormat="dd/MM/yyyy HH:mm"
            timeFormat="HH:mm"
            placeholderText="Selecione fim"
            minDate={start}
            minTime={new Date(0, 0, 0, 0, 0)}
            maxTime={new Date(0, 0, 0, 23, 59)}
          />
        </label>
        <button
          className="generate-stats-button"
          onClick={handleGenerate}
          disabled={!start || !end || loading}
        >
          {loading ? "Gerando..." : "Gerar Estatísticas"}
        </button>
      </div>
      <div className="stats-result">
        {error && <div className="error">{error}</div>}
        {stats && (
          <pre className="stats-output">
            {typeof stats === "object"
              ? JSON.stringify(stats, null, 2)
              : String(stats)}
          </pre>
        )}
      </div>
    </div>
  );
};

StatsMenu.propTypes = {
  visible: PropTypes.bool.isRequired,
};

export default StatsMenu;
