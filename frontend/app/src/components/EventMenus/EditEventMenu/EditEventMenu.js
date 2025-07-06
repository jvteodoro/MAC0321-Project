import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "../EventMenu.css";
import "./EditEventMenu.css";
import ptBR from "date-fns/locale/pt-BR";
import { useAuth } from "../../../context/AuthContext"; // <-- ADD THIS

const EditarEventoMenu = (props) => {
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
  const navigate = useNavigate();
  const { calendarId, eventId } = location.state || {};

  const { user } = useAuth(); // <-- ADD THIS

  // Estados do componente
  const [formData, setFormData] = useState({
    titulo: "",
    dataInicio: null,
    dataFim: null,
    cor: 0,
    descricao: "",
    local: "",
    convidados: [],
    novoConvidado: "",
    hangoutLink: "",
  });

  const [errosFormulario, setErrosFormulario] = useState({});
  const [mostrarSeletorCor, setMostrarSeletorCor] = useState(false);
  const [carregando, setCarregando] = useState(true);
  const [dadosCriador, setDadosCriador] = useState({
    email: "",
    displayName: "",
  });
  const [dadosOrganizador, setDadosOrganizador] = useState({
    email: "",
    displayName: "",
  });
  const [originalEvent, setOriginalEvent] = useState(null);
  const [pollSuccess, setPollSuccess] = useState(false);
  const [pollError, setPollError] = useState(null);
  const [createdPollId, setCreatedPollId] = useState(null);
  const [isOrganizer, setIsOrganizer] = useState(false);
  useEffect(() => {
    registerLocale("pt-BR", ptBR);
    const carregarEvento = async () => {
      try {
        const resposta = await axios.get(
          `http://localhost:12003/events/get?calendarId=${calendarId}&eventId=${eventId}`,
          { withCredentials: true }
        );

        if (resposta.data) {
          const evento = resposta.data;
          setOriginalEvent(evento); // Store the original event object
          setFormData({
            titulo: evento.summary || "",
            dataInicio: evento.start?.dateTime
              ? new Date(evento.start.dateTime)
              : null,
            dataFim: evento.end?.dateTime
              ? new Date(evento.end.dateTime)
              : null,
            cor: evento.colorId ? parseInt(evento.colorId) : 0,
            descricao: evento.description || "",
            local: evento.location || "",
            convidados: Array.isArray(evento.attendees) ? evento.attendees : [],
            novoConvidado: "",
            hangoutLink: evento.hangoutLink || "",
          });

          setDadosCriador({
            email: evento.creator?.email || "",
            displayName:
              evento.creator?.displayName || "Criador não identificado",
          });
          setDadosOrganizador({
            email: evento.organizer?.email || "",
            displayName:
              evento.organizer?.displayName || "Organizador não identificado",
          });

          // Check if a poll already exists for this event
          try {
            const pollResp = await axios.get(
              `http://localhost:12003/poll/byEvent?eventId=${evento.id}`,
              { withCredentials: true }
            );
            if (pollResp.data && pollResp.data.id) {
              setPollSuccess(true);
              setCreatedPollId(pollResp.data.id);
            } else {
              setPollSuccess(false);
              setCreatedPollId(null);
            }
          } catch (pollErr) {
            setPollSuccess(false);
            setCreatedPollId(null);
          }
        }
      } catch (erro) {
        console.error("Erro ao carregar reunião:", erro);
        console.error("Erro ao carregar dados da reunião");
      } finally {
        setCarregando(false);
      }
    };

    if (calendarId && eventId) {
      carregarEvento();
    }
  }, [calendarId, eventId]);

  // Check if user is organizer
  useEffect(() => {
    if (user && dadosOrganizador.email) {
      setIsOrganizer(user.email === dadosOrganizador.email);
    }
  }, [user, dadosOrganizador.email]);

  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setFormData((prev) => ({ ...prev, [id]: value }));
  };

  const fixTimezoneOffset = (date) => {
    if (!date) return "";
    return new Date(date.getTime() - (date.getTimezoneOffset() * 60000));
  }

  const handleDateChange = (data, campo) => {
    setFormData((prev) => ({ ...prev, [campo]: data }));
  };

  const adicionarConvidado = () => {
    if (
      formData.novoConvidado &&
      !formData.convidados.some((c) =>
        typeof c === "string"
          ? c === formData.novoConvidado
          : c?.calendarPerson?.email === formData.novoConvidado
      )
    ) {
      setFormData((prev) => ({
        ...prev,
        convidados: [...prev.convidados, formData.novoConvidado],
        novoConvidado: "",
      }));
    }
  };

  const removerConvidado = (email) => {
    setFormData((prev) => ({
      ...prev,
      convidados: prev.convidados.filter((c) =>
        typeof c === "string"
          ? c !== email
          : c?.calendarPerson?.email !== email && c !== email
      ),
    }));
  };

  const validarFormulario = () => {
    const erros = {};
    if (!formData.titulo.trim()) erros.titulo = "Nome obrigatório";
    if (!formData.dataInicio) erros.dataInicio = "Data inicial obrigatória";
    if (formData.dataFim && formData.dataFim < formData.dataInicio) {
      erros.dataFim = "Data final inválida";
    }
    return erros;
  };

  const selecionarCor = (colorId) => {
    setFormData((prev) => ({ ...prev, cor: colorId }));
    setMostrarSeletorCor(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const erros = validarFormulario();
    if (Object.keys(erros).length > 0) return setErrosFormulario(erros);

    try {
      if (!originalEvent) {
        throw new Error("Evento original não carregado");
      }
      // Clone the original event object
      const updatedEvent = { ...originalEvent };

      // Update only the properties from the form
      updatedEvent.summary = formData.titulo;
      updatedEvent.start = {
        dateTime: fixTimezoneOffset(formData.dataInicio).toISOString(),
        timeZone: "America/Sao_Paulo",
      };
      updatedEvent.end = {
        dateTime: fixTimezoneOffset(formData.dataFim).toISOString(),
        timeZone: "America/Sao_Paulo",
      };
      updatedEvent.colorId = formData.cor.toString();
      updatedEvent.description = formData.descricao || null;
      updatedEvent.location = formData.local || null;

      // Ensure attendees are in the Attendee structure with CalendarPerson
      updatedEvent.attendees =
        formData.convidados.length > 0
          ? formData.convidados.map((c) => {
            // If already in Attendee structure, keep as is
            if (typeof c === "object" && c.calendarPerson) return c;
            // If string (email), wrap as Attendee with CalendarPerson
            return {
              calendarPerson: { email: c },
              organizer: false,
              resource: false,
              optional: false,
              responseStatus: "needsAction",
              comment: "",
              additionalGuests: 0,
            };
          })
          : null;

      await axios.post(
        `http://localhost:12003/events/update?calendarId=${calendarId}`,
        updatedEvent,
        { withCredentials: true }
      );

      console.log("Reunião atualizada com sucesso!");
      window.location.href = "http://localhost:3000/";
    } catch (erro) {
      console.error("Falha ao atualizar reunião:", erro);
      console.error(erro.response?.data?.message || "Erro ao atualizar reunião");
    }
  };

  const cancelarEvento = async () => {
    if (!eventId) return;
    try {
      await axios.post(
        `http://localhost:12003/events/cancel?eventId=${eventId}`,
        {},
        { withCredentials: true }
      );
      console.log("Evento cancelado com sucesso!");
      window.location.href = "http://localhost:3000/";
    } catch (erro) {
      console.error("Falha ao cancelar evento:", erro);
    }
  };

  const deletarEvento = async () => {
    if (!calendarId || !eventId) return;
    try {
      await axios.delete(
        `http://localhost:12003/events/delete/${calendarId}/${eventId}`,
        { withCredentials: true }
      );
      console.log("Evento deletado com sucesso!");
      window.location.href = "http://localhost:3000/";
    } catch (erro) {
      console.error("Falha ao deletar evento:", erro);
    }
  };

  const aoFechar = () => {
    window.location.href = "http://localhost:3000/";
  };

  // Helper to get start/end of the day in ISO string
  const getDayInterval = (date) => {
    if (!date) return [null, null];
    const day = new Date(date);
    return [
      day.toISOString().split("T")[0] + "T00:00:00",
      day.toISOString().split("T")[0] + "T23:59:59",
    ];
  };

  // Handler for "Criar enquete"
  const handleCreatePoll = async () => {
    setPollError(null);
    setPollSuccess(false);
    if (!originalEvent || !formData.dataInicio) {
      setPollError("Evento não carregado ou data inválida.");
      return;
    }
    const [startDate, endDate] = getDayInterval(formData.dataInicio);

    try {
      const response = await axios.get(
        `http://localhost:12003/poll/create?eventId=${originalEvent.id}&startDate=${startDate}&endDate=${endDate}`,
        {
          withCredentials: true,
        }
      );
      setPollSuccess(true);
      setCreatedPollId(response.data.id); // Save poll id for navigation
      console.log("Enquete criada com sucesso!");
    } catch (err) {
      console.error(
        err.response?.data?.message || "Erro ao criar enquete."
      );
      // setPollError(...) removido
    }
  };

  if (carregando) {
    return (
      <main id="event-menu">
        <div className="carregando">Carregando dados da reunião...</div>
      </main>
    );
  }

  // Disable all form controls if not organizer
  const disableInputs = !isOrganizer;
  // Disable poll creation if event is cancelled
  const isEventCancelled = originalEvent && originalEvent.status === "cancelled";

  return (
    <main id="event-menu">
      <button id="close-button" onClick={aoFechar}>
        <i className="fa-solid fa-close"></i>
      </button>

      <div id="event-origin-info">
        <div id="criador-display">
          <label>Criador:</label>
          <div className="criador-info">
            {dadosCriador.displayName} ({dadosCriador.email})
          </div>
        </div>
        <div id="organizador-display">
          <label>Organizador:</label>
          <div className="organizador-info">
            {dadosOrganizador.displayName} ({dadosOrganizador.email})
          </div>
        </div>
      </div>
      <form id="event-data-form" onSubmit={handleSubmit}>
        <label htmlFor="titulo" className="form-field full-width">
          <p>
            Nome da Reunião*
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
            disabled={disableInputs}
          />
        </label>

        <label htmlFor="dataInicio" className="form-field">
          <p>
            Data Inicial*
            {errosFormulario.dataInicio && (
              <span className="error-message">
                {errosFormulario.dataInicio}
              </span>
            )}
          </p>
          <DatePicker
            id="dataInicio"
            selected={formData.dataInicio}
            onChange={(date) => handleDateChange(date, "dataInicio")}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={15}
            dateFormat="dd/MM/yyyy HH:mm"
            className={
              errosFormulario.dataInicio
                ? "error-input date-input"
                : "date-input"
            }
            placeholderText="dd/mm/aaaa hh:mm"
            locale="pt-BR"
            disabled={disableInputs}
          />
        </label>

        <label htmlFor="dataFim" className="form-field">
          <p>
            Data Final
            {errosFormulario.dataFim && (
              <span className="error-message">{errosFormulario.dataFim}</span>
            )}
          </p>
          <DatePicker
            id="dataFim"
            selected={formData.dataFim}
            onChange={(date) => handleDateChange(date, "dataFim")}
            showTimeSelect
            timeFormat="HH:mm"
            timeIntervals={15}
            dateFormat="dd/MM/yyyy HH:mm"
            className={
              errosFormulario.dataFim ? "error-input date-input" : "date-input"
            }
            placeholderText="dd/mm/aaaa hh:mm"
            locale="pt-BR"
            minDate={formData.dataInicio}
            disabled={disableInputs}
          />
        </label>

        <label htmlFor="cor" className="form-field">
          Cor
          <div className="color-picker-container">
            <div
              className="color-preview"
              style={{ backgroundColor: colorMap[formData.cor] }}
              onClick={() => !disableInputs && setMostrarSeletorCor(!mostrarSeletorCor)}
            />
            {mostrarSeletorCor && !disableInputs && (
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
        <div id="enquete-meet" >
          {formData.hangoutLink && (
            <div id="meetLinkHolder" className="form-field">
              <label>Link do Google Meet</label>
              <a
                href={formData.hangoutLink}
                target="_blank"
                rel="noopener noreferrer"
                id="meet-link"
              >
                {formData.hangoutLink}
              </a>
            </div>
          )}

          <button
            type="button"
            className="submit-button criar-enquete-btn"
            style={{ marginBottom: "1em", width: "auto" }}
            onClick={handleCreatePoll}
            disabled={disableInputs || pollSuccess || isEventCancelled}
          >
            Criar enquete
          </button>
        </div>

        <label htmlFor="local" className="form-field full-width">
          Local
          <input
            type="text"
            id="local"
            value={formData.local}
            onChange={handleInputChange}
            placeholder="Onde a reunião acontecerá"
            autoComplete="off"
            disabled={disableInputs}
          />
        </label>

        <label htmlFor="descricao" className="form-field full-width">
          Descrição
          <textarea
            id="descricao"
            value={formData.descricao}
            onChange={handleInputChange}
            rows="1"
            autoComplete="off"
            disabled={disableInputs}
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
                placeholder="Adicione emails de convidados"
                onKeyPress={(e) =>
                  e.key === "Enter" &&
                  (e.preventDefault(), adicionarConvidado())
                }
                autoComplete="off"
                disabled={disableInputs}
              />
              <button
                type="button"
                className="adicionar"
                onClick={adicionarConvidado}
                disabled={disableInputs}
              >
                Adicionar
              </button>
            </div>
          </label>

          {formData.convidados.length > 0 && (
            <div className="lista-convidados">
              {formData.convidados.map((c, index) => {
                // Support both string and Attendee object
                const email =
                  typeof c === "string"
                    ? c
                    : c?.calendarPerson?.email || c?.email || "";
                return (
                  <div key={index} className="convidado-item">
                    {email}
                    <button
                      type="button"
                      className="remover"
                      onClick={() => removerConvidado(email)}
                      disabled={disableInputs}
                    >
                      <i className="fa-solid fa-close"></i>
                    </button>
                  </div>
                );
              })}
            </div>
          )}
        </div>
        <div className="form-field button-row">
          <button
            type="button"
            id="cancel-button"
            onClick={cancelarEvento}
            disabled={disableInputs}
          >
            Cancelar
          </button>
          <button
            type="button"
            id="delete-button"
            onClick={deletarEvento}
            disabled={disableInputs}
          >
            Deletar
            <i className="fa-solid fa-trash"></i>
          </button>
          <button
            type="submit"
            id="save-button"
            disabled={disableInputs}
          >
            Salvar Alterações
          </button>
        </div>
      </form>
    </main>
  );
};

EditarEventoMenu.propTypes = {
  aoFechar: PropTypes.func,
};

export default EditarEventoMenu;
