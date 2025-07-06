import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import "react-datepicker/dist/react-datepicker.css";
import "./VoteMenu.css";

const VoteMenu = (props) => {
  const location = useLocation();
  // const navigate = useNavigate();
  const { eventId, calendarId } = location.state || {};

  // State for the component
  const [poll, setPoll] = useState(null);
  const [selectedIds, setSelectedIds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState(false);
  const [eventName, setEventName] = useState("");
  const [eventDate, setEventDate] = useState("");

  // Fetch poll for the event
  useEffect(() => {
    const fetchPollAndEvent = async () => {
      if (!eventId) {
        setError("Evento não especificado.");
        setLoading(false);
        return;
      }
      try {
        // Fetch poll
        const pollResp = await axios.get(
          `http://localhost:12003/poll/byEvent`,
          {
            params: { eventId },
            withCredentials: true,
          }
        );
        if (pollResp.data && pollResp.data.posibleTimes) {
          setPoll(pollResp.data);
        } else {
          setPoll(null);
        }

        // Always fetch event details if calendarId and eventId are present
        if (calendarId && eventId) {
          const eventResp = await axios.get(
            `http://localhost:12003/events/get?calendarId=${calendarId}&eventId=${eventId}`,
            { withCredentials: true }
          );
          setEventName(eventResp.data?.summary || "");
          setEventDate(eventResp.data.start.dateTime || "");
        }
      } catch (err) {
        setError("Erro ao buscar dados da enquete.");
      } finally {
        setLoading(false);
      }
    };
    fetchPollAndEvent();
  }, [eventId, calendarId]);

  const toggleTimeSlot = (id) => {
    setSelectedIds((prev) =>
      prev.includes(id) ? prev.filter((sid) => sid !== id) : [...prev, id]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError(null);
    try {
      // Send a vote for each selected posibleTime id
      for (const dateTimeIntervalId of selectedIds) {
        await axios.post(
          `http://localhost:12003/poll/vote`,
          null,
          {
            params: {
              eventPollId: poll.id,
              dateTimeIntervalId,
            },
            withCredentials: true,
          }
        );
      }
      setSubmitSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || "Erro ao enviar votos");
    } finally {
      setSubmitting(false);
    }
  };

  const formatTime = (isoString) => {
    if (!isoString) return "";
    const date = new Date(isoString);
    return date.toLocaleTimeString("pt-BR", {
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const formatDate = (isoString) => {
    if (!isoString) return "";
    const date = new Date(isoString);
    return date.toLocaleDateString("pt-BR", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  if (loading) return <div className="loading">Carregando horários...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!poll)
    return (
      <main id="vote-menu">
        <h2>Vote nos melhores horários</h2>
        {eventName && (
          <div>
            <strong>Evento:</strong> {eventName}
          </div>
        )}
        {eventDate && (
          <div>
            <strong>Dia:</strong> {formatDate(eventDate)}
          </div>
        )}
        <div>Nenhuma enquete disponível para este evento.</div>
      </main>
    );
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
      {eventName && (
        <div>
          <strong>Evento:</strong> {eventName}
        </div>
      )}
      {eventDate && (
        <div>
          <strong>Dia:</strong> {formatDate(eventDate)}
        </div>
      )}
      <form onSubmit={handleSubmit} autoComplete="off">
        <div className="timeslot-list-container">
          <h3>Horários disponíveis:</h3>
          {poll.posibleTimes.length === 0 ? (
            <p>Nenhum horário disponível para votação.</p>
          ) : (
            <ul className="timeslot-list">
              {poll.posibleTimes.map((slot, idx) => {
                // Always use the same value for key and selection: fallbackId is used everywhere
                const fallbackId = slot.id ?? `slot-${idx}`;
                const isSelected = selectedIds.includes(fallbackId);
                const interval = slot.dateTimeInterval;
                return (
                  <li
                    key={fallbackId}
                    className={`timeslot-item ${isSelected ? "selected" : ""}`}
                    onClick={() => toggleTimeSlot(fallbackId)}
                  >
                    <div className="timeslot-time">
                      {formatTime(interval?.start)} - {formatTime(interval?.end)}
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
          disabled={selectedIds.length === 0 || submitting}
        >
          {submitting ? "Enviando..." : "Enviar Votos"}
        </button>
      </form>
    </main>
  );
};

VoteMenu.propTypes = {
  eventId: PropTypes.string,
};

export default VoteMenu;
