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

  const handleGenerate = async () => {
    setLoading(true);
    setStats(null);
    setError(null);
    try {
      const params = {
        start: start ? start.toISOString() : "",
        end: end ? end.toISOString() : "",
      };
      const response = await axios.get(
        `/api/stats/generate`,
        { params, withCredentials: true }
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
            minTime={new Date(0, 0, 0, 0, 0)}
            maxTime={new Date(0, 0, 0, 23, 59)}
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
