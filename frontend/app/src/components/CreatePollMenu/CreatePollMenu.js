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
  const { calendarId, initialDate } = location.state || {};

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
    start: initialDate ? new Date(initialDate) : null,
    end: initialDate
      ? new Date(new Date(initialDate).getTime() + 60 * 60 * 1000)
      : null,
  });

  // Fetch available time slots from backend
  useEffect(() => {
    registerLocale("pt-BR", ptBR);

    const fetchAvailableSlots = async () => {
      try {
        const response = await axios.get(
          `/api/calendar/${calendarId}/availability`,
          {
            params: {
              date: initialDate
                ? new Date(initialDate).toISOString()
                : new Date().toISOString(),
            },
            withCredentials: true,
          }
        );

        setAvailableSlots(
          response.data.availableSlots.map((slot) => ({
            ...slot,
            start: new Date(slot.start),
            end: new Date(slot.end),
          }))
        );
        setLoading(false);
      } catch (err) {
        setError(
          err.response?.data?.message || "Failed to load available time slots"
        );
        setLoading(false);
      }
    };

    if (calendarId) {
      fetchAvailableSlots();
    }
  }, [calendarId, initialDate]);

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
      setError("Please select at least one time slot");
      return;
    }

    setSubmitting(true);
    setError(null);

    try {
      await axios.post(
        `/api/polls/create`,
        {
          title: pollTitle,
          description: pollDescription,
          timeSlots: selectedSlots,
          calendarId,
        },
        { withCredentials: true }
      );
      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || "Failed to create poll");
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
    return <div className="loading">Loading available time slots...</div>;
  if (success)
    return (
      <div className="success">
        <h3>Poll created successfully!</h3>
        <p>Your participants can now vote on the selected time slots.</p>
      </div>
    );
	
  return (
    <div className="create-poll-container">
      <h2>Create New Time Poll</h2>

      <form id="create-poll-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="pollTitle">Poll Title*</label>
          <input
            id="pollTitle"
            type="text"
            value={pollTitle}
            onChange={(e) => setPollTitle(e.target.value)}
            required
            placeholder="Enter poll title"
            autoComplete="off"
          />
        </div>

        <div className="form-group">
          <label htmlFor="pollDescription">Description</label>
          <textarea
            id="pollDescription"
            value={pollDescription}
            onChange={(e) => setPollDescription(e.target.value)}
            placeholder="Optional description for your poll"
            rows="3"
            autoComplete="off"
          />
        </div>

        <div className="time-selection-section">
          <h3>Available Time Slots</h3>

          {availableSlots.length === 0 ? (
            <p>No available time slots found for the selected date.</p>
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
            <h4>Add Custom Time Slot</h4>
            <div className="time-inputs">
              <DatePicker
                selected={newSlot.start}
                onChange={(time) => handleTimeChange(time, "start")}
                showTimeSelect
                showTimeSelectOnly
                timeIntervals={15}
                timeCaption="Time"
                dateFormat="HH:mm"
                timeFormat="HH:mm"
                locale="pt-BR"
                placeholderText="Start time"
              />
              <span>to</span>
              <DatePicker
                selected={newSlot.end}
                onChange={(time) => handleTimeChange(time, "end")}
                showTimeSelect
                showTimeSelectOnly
                timeIntervals={15}
                timeCaption="Time"
                dateFormat="HH:mm"
                timeFormat="HH:mm"
                locale="pt-BR"
                placeholderText="End time"
                minTime={newSlot.start}
              />
              <button
                type="button"
                className="add-button"
                onClick={addManualSlot}
                disabled={!newSlot.start || !newSlot.end}
              >
                Add Custom Slot
              </button>
            </div>
          </div>
        </div>

        <div className="selected-slots-section">
          <h3>Selected Time Slots for Poll ({selectedSlots.length})</h3>

          {selectedSlots.length === 0 ? (
            <p className="empty-message">No slots selected yet</p>
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
            {submitting ? "Creating Poll..." : "Create Poll"}
          </button>
        </div>
      </form>
    </div>
  );
};

CreatePollMenu.propTypes = {
  calendarId: PropTypes.string,
  initialDate: PropTypes.instanceOf(Date),
};

export default CreatePollMenu;
