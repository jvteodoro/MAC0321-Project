import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { useLocation } from "react-router-dom";
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ptBR from "date-fns/locale/pt-BR";
import "./VoteMenu.css";

const VoteMenu = (props) => {
  const location = useLocation();
  const { eventId } = location.state || {};

  // State for the component
  const [timeSlots, setTimeSlots] = useState([]);
  const [selectedSlots, setSelectedSlots] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState(false);

  // Fetch available time slots from backend
  useEffect(() => {
    registerLocale("pt-BR", ptBR);

    const fetchTimeSlots = async () => {
      try {
        const response = await axios.get(`/api/events/${eventId}/timeslots`, {
          withCredentials: true,
        });
        setTimeSlots(
          response.data.timeSlots.map((slot) => ({
            ...slot,
            start: new Date(slot.start),
            end: new Date(slot.end),
          }))
        );
        setLoading(false);
      } catch (err) {
        setError(err.response?.data?.message || "Erro ao carregar horários");
        setLoading(false);
      }
    };

    if (eventId) {
      fetchTimeSlots();
    }
  }, [eventId]);

  const toggleTimeSlot = (slot) => {
    setSelectedSlots((prev) => {
      const isSelected = prev.some(
        (s) =>
          s.start.getTime() === slot.start.getTime() &&
          s.end.getTime() === slot.end.getTime()
      );

      if (isSelected) {
        return prev.filter(
          (s) =>
            s.start.getTime() !== slot.start.getTime() ||
            s.end.getTime() !== slot.end.getTime()
        );
      } else {
        return [...prev, slot];
      }
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    try {
      await axios.post(
        `http://localhost:12003/events/${eventId}/vote`,
        { selectedSlots },
        { withCredentials: true }
      );
      setSubmitSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao enviar votos");
    } finally {
      setSubmitting(false);
    }
  };

  const formatTime = (date) => {
    return (
      date?.toLocaleTimeString("pt-BR", {
        hour: "2-digit",
        minute: "2-digit",
      }) || ""
    );
  };

  if (loading) return <div className="loading">Carregando horários...</div>;
  if (error) return <div className="error">{error}</div>;
  if (submitSuccess)
    return (
      <div className="success">
        <h3>Obrigado por votar!</h3>
        <p>Seus horários preferidos foram registrados.</p>
      </div>
    );
  return (
    <main id="vote-menu">
      <h2>Vote nos melhores horários</h2>

      <form onSubmit={handleSubmit} autoComplete="off">
        <div className="timeslot-list-container">
          <h3>Horários disponíveis:</h3>

          {timeSlots.length === 0 ? (
            <p>Nenhum horário disponível para votação.</p>
          ) : (
            <ul className="timeslot-list">
              {timeSlots.map((slot, index) => {
                const isSelected = selectedSlots.some(
                  (s) =>
                    s.start.getTime() === slot.start.getTime() &&
                    s.end.getTime() === slot.end.getTime()
                );

                return (
                  <li
                    key={index}
                    className={`timeslot-item ${isSelected ? "selected" : ""}`}
                    onClick={() => toggleTimeSlot(slot)}
                  >
                    <div className="timeslot-time">
                      {formatTime(slot.start)} - {formatTime(slot.end)}
                    </div>
                    <div className="timeslot-votes">
                      {slot.votes || 0} votos
                    </div>
                    {isSelected && (
                      <div className="timeslot-selected-icon">
                        <i className="fa-solid fa-check"></i>
                      </div>
                    )}
                  </li>
                );
              })}
            </ul>
          )}
        </div>

        {error && <div className="error-message">{error}</div>}

        <button
          type="submit"
          className="submit-button"
          disabled={selectedSlots.length === 0 || submitting}
        >
          {submitting ? "Enviando..." : "Enviar Votos"}
        </button>
      </form>
    </main>
  );
};

VoteMenu.propTypes = {
  // Add prop types as needed
  eventId: PropTypes.string,
};

export default VoteMenu;
