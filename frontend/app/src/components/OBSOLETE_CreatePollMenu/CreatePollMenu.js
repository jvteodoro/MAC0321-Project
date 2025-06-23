import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { useLocation } from "react-router-dom";
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ptBR from "date-fns/locale/pt-BR";
import "./CreatePollMenu.css";

const CreatePollMenu = () => {
  const location = useLocation();
  const { eventId } = location.state || {};

  // Component state
  const [availableSlots, setAvailableSlots] = useState([]);
  const [selectedSlots, setSelectedSlots] = useState([]);
  const [pollTitle, setPollTitle] = useState("");
  const [pollDescription, setPollDescription] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const [newSlot, setNewSlot] = useState({
    start: null,
    end: null,
  });

  // Fetch available time slots from backend
  useEffect(() => {
    registerLocale("pt-BR", ptBR);

    const fetchAvailableSlots = async () => {
      try {
        const response = await axios.get(
          `/events/${eventId}/timeslots`,
          { withCredentials: true }
        );
        setAvailableSlots(
          response.data.timeSlots.map((slot) => ({
            ...slot,
            start: new Date(slot.start),
            end: new Date(slot.end),
          }))
        );
        setLoading(false);
      } catch (err) {
        setError(
          err.response?.data?.message || "Falha ao carregar horários disponíveis"
        );
        setLoading(false);
      }
    };

    if (eventId) {
      fetchAvailableSlots();
    }
  }, [eventId]);

  // Handle time selection for manual slot addition
  const handleTimeChange = (time, field) => {
    if (!time) return;

    setNewSlot((prev) => {
      const updatedSlot = {
        start: prev.start ? new Date(prev.start) : null,
        end: prev.end ? new Date(prev.end) : null,
        [field]: new Date(time),
      };

      // Ensure end time is after start time
      if (
        field === "start" &&
        updatedSlot.end &&
        updatedSlot.start > updatedSlot.end
      ) {
        updatedSlot.end = new Date(
          updatedSlot.start.getTime() + 60 * 60 * 1000
        );
      }

      return updatedSlot;
    });
  };

  // Add a slot to selected options
  const addToPoll = (slot) => {
    if (
      !selectedSlots.some(
        (s) =>
          s.start.getTime() === slot.start.getTime() &&
          s.end.getTime() === slot.end.getTime()
      )
    ) {
      setSelectedSlots((prev) => [...prev, slot]);
    }
  };

  // Remove a slot from selected options
  const removeFromPoll = (index) => {
    setSelectedSlots((prev) => prev.filter((_, i) => i !== index));
  };

  // Add a manually configured time slot
  const addManualSlot = () => {
    if (newSlot.start && newSlot.end) {
      addToPoll({
        start: new Date(newSlot.start),
        end: new Date(newSlot.end),
      });
      // Reset for next slot
      setNewSlot({
        start: new Date(newSlot.end),
        end: new Date(newSlot.end.getTime() + 60 * 60 * 1000),
      });
    }
  };

  // Submit the poll to backend
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (selectedSlots.length === 0) {
      setError("Selecione pelo menos um horário");
      return;
    }

    setSubmitting(true);
    setError(null);

    try {
      await axios.post(
        `/api/polls/create`,
        {
          eventId,
          title: pollTitle,
          description: pollDescription,
          timeSlots: selectedSlots,
        },
        { withCredentials: true }
      );
      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || "Falha ao criar enquete");
    } finally {
      setSubmitting(false);
    }
  };

  // Format time for display
  const formatTime = (date) => {
    return (
      date?.toLocaleTimeString("pt-BR", {
        hour: "2-digit",
        minute: "2-digit",
      }) || ""
    );
  };

  if (loading)
    return <div className="loading">Carregando horários disponíveis...</div>;
  if (success)
    return (
      <div className="success">
        <h3>Enquete criada com sucesso!</h3>
        <p>Os participantes podem votar nos horários selecionados.</p>
      </div>
    );

  return (
    <div className="create-poll-container">
      <h2>Criar nova enquete de horários</h2>

      <form id="create-poll-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="pollTitle">Título da Enquete*</label>
          <input
            id="pollTitle"
            type="text"
            value={pollTitle}
            onChange={(e) => setPollTitle(e.target.value)}
            required
            placeholder="Digite o título da enquete"
            autoComplete="off"
          />
        </div>

        <div className="form-group">
          <label htmlFor="pollDescription">Descrição</label>
          <textarea
            id="pollDescription"
            value={pollDescription}
            onChange={(e) => setPollDescription(e.target.value)}
            placeholder="Descrição opcional para sua enquete"
            rows="3"
            autoComplete="off"
          />
        </div>

        <div className="time-selection-section">
          <h3>Horários disponíveis</h3>

          {availableSlots.length === 0 ? (
            <p>Nenhum horário disponível encontrado.</p>
          ) : (
            <div className="available-slots">
              {availableSlots.map((slot, index) => (
                <div
                  key={`available-${index}`}
                  className="time-slot"
                  onClick={() => addToPoll(slot)}
                >
                  {formatTime(slot.start)} - {formatTime(slot.end)}
                  <button
                    type="button"
                    className="add-button"
                    onClick={(e) => {
                      e.stopPropagation();
                      addToPoll(slot);
                    }}
                  >
                    <i className="fa-solid fa-plus"></i>
                  </button>
                </div>
              ))}
            </div>
          )}

          <div className="manual-slot-creation">
            <h4>Adicionar horário personalizado</h4>
            <div className="time-inputs">
              <DatePicker
                selected={newSlot.start}
                onChange={(time) => handleTimeChange(time, "start")}
                showTimeSelect
                showTimeSelectOnly
                timeIntervals={15}
                timeCaption="Hora"
                dateFormat="HH:mm"
                timeFormat="HH:mm"
                locale="pt-BR"
                placeholderText="Início"
              />
              <span>até</span>
              <DatePicker
                selected={newSlot.end}
                onChange={(time) => handleTimeChange(time, "end")}
                showTimeSelect
                showTimeSelectOnly
                timeIntervals={15}
                timeCaption="Hora"
                dateFormat="HH:mm"
                timeFormat="HH:mm"
                locale="pt-BR"
                placeholderText="Fim"
                minTime={newSlot.start}
              />
              <button
                type="button"
                className="add-button"
                onClick={addManualSlot}
                disabled={!newSlot.start || !newSlot.end}
              >
                Adicionar horário
              </button>
            </div>
          </div>
        </div>

        <div className="selected-slots-section">
          <h3>Horários selecionados para a enquete ({selectedSlots.length})</h3>

          {selectedSlots.length === 0 ? (
            <p className="empty-message">Nenhum horário selecionado ainda</p>
          ) : (
            <ul className="selected-slots">
              {selectedSlots.map((slot, index) => (
                <li key={`selected-${index}`} className="selected-slot">
                  <span>
                    {formatTime(slot.start)} - {formatTime(slot.end)}
                  </span>
                  <button
                    type="button"
                    className="remover"
                    onClick={() => removeFromPoll(index)}
                  >
                    <i className="fa-solid fa-times"></i>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>

        {error && <div className="error-message">{error}</div>}

        <div className="form-actions">
          <button
            type="submit"
            className="submit-button"
            disabled={selectedSlots.length === 0 || submitting}
          >
            {submitting ? "Criando enquete..." : "Criar Enquete"}
          </button>
        </div>
      </form>
    </div>
  );
};

CreatePollMenu.propTypes = {
  // eventId is passed via route state
};

export default CreatePollMenu;
