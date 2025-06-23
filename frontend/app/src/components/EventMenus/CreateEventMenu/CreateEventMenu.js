import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { useLocation } from "react-router-dom";
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "../EventMenu.css";
import "./CreateEventMenu.css";
import ptBR from "date-fns/locale/pt-BR";

const CriarEventoMenu = (props) => {
  const colorMap = {
    0: "rgb(3, 155, 229)",
    1: "rgb(121, 134, 203)",
    2: "rgb(51, 182, 121)",
    3: "rgb(142, 36, 170)",
    4: "rgb(230, 124, 115)",
    5: "rgb(246, 191, 38)",
    6: "rgb(244, 81, 30)",
    8: "rgb(97, 97, 97)",
    9: "rgb(63, 81, 181)",
    10: "rgb(11, 128, 67)",
    11: "rgb(213, 0, 0)",
  };

  const location = useLocation();
  const { calendarId, dayInfo } = location.state || {};

  // Estados do componente
  const [formData, setFormData] = useState({
    titulo: "",
    descricao: "",
    local: "",
    cor: 0,
    // remover criarMeet
    convidados: [],
    novoConvidado: "",
    start: null, // single start datetime
    end: null,   // single end datetime
  });

  const [errosFormulario, setErrosFormulario] = useState({});
  const [mostrarSeletorCor, setMostrarSeletorCor] = useState(false);
  const [carregando, setCarregando] = useState(false);

  useEffect(() => {
    registerLocale("pt-BR", ptBR);
    // Define a data padrão para o dia selecionado
    const dataPadrao = new Date(dayInfo.year, dayInfo.monthIndex, dayInfo.day, 9, 0, 0, 0); // 9:00 AM
    setFormData((prev) => ({
      ...prev,
      start: dataPadrao,
      end: new Date(dataPadrao.getTime() + 60 * 60 * 1000), // +1 hora
    }));
  }, [dayInfo]);

  // Helper to create a date with the same day as dayInfo and given hours/minutes
  const createDateWithTime = (hours, minutes = 0) => {
    return new Date(dayInfo.year, dayInfo.monthIndex, dayInfo.day, hours, minutes, 0, 0);
  };

  // Handler for start/end datetime changes
  const handleDateTimeChange = (date, field) => {
    if (!date) return;
    setFormData((prev) => {
      const newValue = { ...prev, [field]: new Date(date) };
      // Ensure end is after start
      if (field === "start" && newValue.end && newValue.start > newValue.end) {
        newValue.end = new Date(newValue.start.getTime() + 60 * 60 * 1000);
      }
      if (field === "end" && newValue.start && newValue.end < newValue.start) {
        newValue.start = new Date(newValue.end.getTime() - 60 * 60 * 1000);
      }
      return newValue;
    });
  };

  const selecionarCor = (colorId) => {
    setFormData((prev) => ({ ...prev, cor: colorId }));
    setMostrarSeletorCor(false);
  };

  const validarFormulario = () => {
    const erros = {};
    if (!formData.titulo.trim()) erros.titulo = "Nome obrigatório";
    if (!formData.start || !formData.end)
      erros.time = "Defina início e fim do evento";
    return erros;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const erros = validarFormulario();
    if (Object.keys(erros).length > 0) return setErrosFormulario(erros);

    setCarregando(true);

    try {
      // Prepare attendees as array of Attendee objects (with email as calendarPerson)
      const attendees = formData.convidados.map((email) => ({
        calendarPerson: { id: null, email: email, displayName: null },
        organizer: false,
        resource: false,
        optional: false,
        responseStatus: "needsAction",
        comment: "",
        additionalGuests: 0,
      }));

      // Compose the event object to match EventsResource.java
      const dadosEvento = {
        // Required/standard fields
        summary: formData.titulo,
        description: formData.descricao || "",
        location: formData.local || null,
        colorId: formData.cor ? String(formData.cor) : null,
        start: { dateTime: formData.start }, // EventDate object
        end: { dateTime: formData.end },     // EventDate object
        status: "confirmed",
        attendees: attendees,
        // Optional/empty fields for EventsResource compatibility
        kind: "calendar#event",
        etag: "",
        mainCalendarId: calendarId,
        calendarIds: [calendarId],
        htmlLink: "",
        created: new Date().toISOString(),
        updated: new Date().toISOString(),
        creator: null,
        organizer: null,
        endTimeUnspecified: false,
        recurrence: [],
        recurringEventId: null,
        originalStartTime: null,
        transparency: null,
        visibility: null,
        iCalUID: null,
        sequence: null,
        attendeesOmitted: false,
        extendedProperties: null,
        hangoutLink: null,
        links: 1,
        eventId: null,
      };

      const resposta = await axios.post(
        `http://localhost:12003/events/insert?calendarId=${calendarId}`,
        dadosEvento,
        { withCredentials: true }
      );

      console.log(
        `Evento criado com sucesso!${
          resposta.data.hangoutLink
            ? `\nLink do Meet: ${resposta.data.hangoutLink}`
            : ""
        }`
      );
      window.location.href = "/";
    } catch (erro) {
      console.error("Erro:", erro);
      console.error(erro.response?.data?.message || "Erro ao criar evento");
    } finally {
      setCarregando(false);
    }
  };

  const aoFechar = () => {
    window.location.href = "/";
  };

  const formatTime = (date) => {
    return (
      date?.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }) || ""
    );
  };

  const handleInputChange = (e) => {
    const { id, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [id]: type === "checkbox" ? checked : value,
    }));
  };

  // Adiciona convidado ao array de convidados
  const adicionarConvidado = () => {
    if (
      formData.novoConvidado &&
      !formData.convidados.includes(formData.novoConvidado)
    ) {
      setFormData((prev) => ({
        ...prev,
        convidados: [...prev.convidados, prev.novoConvidado],
        novoConvidado: "",
      }));
    }
  };

  // Remove convidado do array de convidados
  const removerConvidado = (email) => {
    setFormData((prev) => ({
      ...prev,
      convidados: prev.convidados.filter((c) => c !== email),
    }));
  };

  return (
    <main id="event-menu">
      <button id="close-button" onClick={aoFechar}>
        <i className="fa-solid fa-close"></i>
      </button>

      <form id="event-data-form" onSubmit={handleSubmit}>
        <label htmlFor="titulo" className="form-field full-width">
          <p>
            Nome do Evento*
            {errosFormulario.titulo && (
              <span className="error-message">{errosFormulario.titulo}</span>
            )}
          </p>
          <input
            type="text"
            id="titulo"
            value={formData.titulo}
            onChange={handleInputChange}
            className={errosFormulario.titulo ? "error-input" : ""}
            autoComplete="off"
          />
        </label>

        <div className="form-field full-width">
          <label>
            <p>
              Início e Fim do Evento*
              {errosFormulario.time && (
                <span className="error-message">
                  {errosFormulario.time}
                </span>
              )}
            </p>
          </label>
          <div className="timeslot-container">
            <div className="timeslot-inputs">
              <div className="time-input-group">
                <DatePicker
                  selected={formData.start}
                  onChange={(date) => handleDateTimeChange(date, "start")}
                  showTimeSelect
                  timeIntervals={15}
                  timeCaption="Hora"
                  dateFormat="dd/MM/yyyy HH:mm"
                  timeFormat="HH:mm"
                  locale="pt-BR"
                  className="time-input"
                  placeholderText="Início"
                  minDate={createDateWithTime(0, 0)}
                  maxDate={createDateWithTime(23, 45)}
                  minTime={createDateWithTime(0, 0)}
                  maxTime={createDateWithTime(23, 45)}
                  selectsStart
                  startDate={formData.start}
                  endDate={formData.end}
                />
                <span>às</span>
                <DatePicker
                  selected={formData.end}
                  onChange={(date) => handleDateTimeChange(date, "end")}
                  showTimeSelect
                  timeIntervals={15}
                  timeCaption="Hora"
                  dateFormat="dd/MM/yyyy HH:mm"
                  timeFormat="HH:mm"
                  locale="pt-BR"
                  className="time-input"
                  placeholderText="Fim"
                  minDate={formData.start || createDateWithTime(0, 0)}
                  maxDate={createDateWithTime(23, 45)}
                  minTime={formData.start || createDateWithTime(0, 0)}
                  maxTime={createDateWithTime(23, 45)}
                  selectsEnd
                  startDate={formData.start}
                  endDate={formData.end}
                  filterTime={(time) =>
                    !formData.start ||
                    time.getTime() >=
                      formData.start.getTime() + 15 * 60 * 1000
                  }
                />
              </div>
            </div>
          </div>
        </div>

        <label htmlFor="cor" className="form-field">
          Cor
          <div className="color-picker-container">
            <div
              className="color-preview"
              style={{ backgroundColor: colorMap[formData.cor] }}
              onClick={() => setMostrarSeletorCor(!mostrarSeletorCor)}
            />
            {mostrarSeletorCor && (
              <div className="color-palette">
                {Object.entries(colorMap).map(([colorId, colorValue]) => (
                  <div
                    key={colorId}
                    className="color-option"
                    style={{ backgroundColor: colorValue }}
                    onClick={() => selecionarCor(parseInt(colorId))}
                    title={`Cor ${colorId}`}
                  />
                ))}
              </div>
            )}
          </div>
        </label>

        <label htmlFor="local" className="form-field full-width">
          Local
          <input
            type="text"
            id="local"
            value={formData.local}
            onChange={handleInputChange}
            placeholder="Onde o evento acontecerá"
            autoComplete="off"
          />
        </label>

        <label htmlFor="descricao" className="form-field full-width">
          Descrição
          <textarea
            id="descricao"
            value={formData.descricao}
            onChange={handleInputChange}
            rows="2"
            autoComplete="off"
          />
        </label>

        <div className="form-field full-width">
          <label htmlFor="novoConvidado">
            Convidados
            <div className="convidados-input-container">
              <input
                type="email"
                id="novoConvidado"
                value={formData.novoConvidado}
                onChange={handleInputChange}
                placeholder="Adicione e-mails de convidados"
                onKeyPress={(e) =>
                  e.key === "Enter" &&
                  (e.preventDefault(), adicionarConvidado())
                }
                autoComplete="off"
              />
              <button
                type="button"
                className="adicionar"
                onClick={adicionarConvidado}
              >
                Adicionar
              </button>
            </div>
          </label>

          {formData.convidados.length > 0 && (
            <div className="lista-convidados">
              {formData.convidados.map((email, index) => (
                <div key={index} className="convidado-item">
                  {email}
                  <button
                    type="button"
                    className="remover"
                    onClick={() => removerConvidado(email)}
                  >
                    <i className="fa-solid fa-close"></i>
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        <button
          type="submit"
          id="create-button"
          className="form-field"
          disabled={carregando}
        >
          {carregando ? "Criando..." : "Criar Evento"}
        </button>
      </form>
    </main>
  );
};

CriarEventoMenu.propTypes = {
  aoFechar: PropTypes.func,
};

export default CriarEventoMenu;
