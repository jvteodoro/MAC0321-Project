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
    criarMeet: false,
    convidados: [],
    novoConvidado: "",
    timeSlots: [], // Array de { start: Date, end: Date }
  });

  const [novoTimeSlot, setNovoTimeSlot] = useState({
    start: null,
    end: null,
  });

  const [errosFormulario, setErrosFormulario] = useState({});
  const [mostrarSeletorCor, setMostrarSeletorCor] = useState(false);
  const [carregando, setCarregando] = useState(false);

  useEffect(() => {
    registerLocale("pt-BR", ptBR);
    // Define a data padrão para o dia selecionado
    const dataPadrao = new Date(dayInfo.year, dayInfo.monthIndex, dayInfo.day);
    dataPadrao.setHours(9, 0, 0, 0); // 9:00 AM

    setNovoTimeSlot({
      start: dataPadrao,
      end: new Date(dataPadrao.getTime() + 60 * 60 * 1000), // +1 hora
    });
  }, [dayInfo]);

  const handleInputChange = (e) => {
    const { id, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [id]: type === "checkbox" ? checked : value,
    }));
  };

  // Helper to create time Date objects
  const createTime = (hours, minutes = 0) => {
    const date = new Date();
    date.setHours(hours, minutes, 0, 0);
    return date;
  };

  // Fixed handler for time changes
  const handleTimeSlotChange = (time, field) => {
    if (!time) return;

    setNovoTimeSlot((prev) => {
      const newValue = {
        start: prev.start ? new Date(prev.start) : null,
        end: prev.end ? new Date(prev.end) : null,
      };

      newValue[field] = new Date(time);

      // Ensure end time is always after start time
      if (field === "start" && newValue.end && newValue.start > newValue.end) {
        newValue.end = new Date(newValue.start);
        newValue.end.setHours(newValue.end.getHours() + 1);
      }

      return newValue;
    });
  };

  const adicionarTimeSlot = () => {
    if (novoTimeSlot.start && novoTimeSlot.end) {
      setFormData((prev) => ({
        ...prev,
        timeSlots: [
          ...prev.timeSlots,
          {
            start: novoTimeSlot.start,
            end: novoTimeSlot.end,
          },
        ],
      }));
      // Reset para o próximo intervalo
      setNovoTimeSlot((prev) => ({
        start: new Date(prev.end.getTime() + 30 * 60 * 1000), // +30 minutos
        end: new Date(prev.end.getTime() + 90 * 60 * 1000), // +1.5 horas
      }));
    }
  };

  const removerTimeSlot = (index) => {
    setFormData((prev) => ({
      ...prev,
      timeSlots: prev.timeSlots.filter((_, i) => i !== index),
    }));
  };

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

  const removerConvidado = (email) => {
    setFormData((prev) => ({
      ...prev,
      convidados: prev.convidados.filter((c) => c !== email),
    }));
  };

  const selecionarCor = (colorId) => {
    setFormData((prev) => ({ ...prev, cor: colorId }));
    setMostrarSeletorCor(false);
  };

  const validarFormulario = () => {
    const erros = {};
    if (!formData.titulo.trim()) erros.titulo = "Nome obrigatório";
    if (formData.timeSlots.length === 0)
      erros.timeSlots = "Adicione pelo menos um horário";
    return erros;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const erros = validarFormulario();
    if (Object.keys(erros).length > 0) return setErrosFormulario(erros);

    setCarregando(true);

    try {
      const dadosEvento = {
        summary: formData.titulo,
        description: `${
          formData.descricao || ""
        }\n\nHorários para votação:\n${formData.timeSlots
          .map(
            (slot) =>
              `${slot.start.toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })} - ${slot.end.toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}`
          )
          .join("\n")}`,
        location: formData.local || null,
        colorId: formData.cor.toString(),
        timeSlots: formData.timeSlots,
        attendees: formData.convidados.map((email) => ({ email })),
        createMeet: formData.criarMeet,
      };

      const resposta = await axios.post(
        `/api/events/create-with-timeslots?calendarId=${calendarId}`,
        dadosEvento,
        { withCredentials: true }
      );

      alert(
        `Evento criado com sucesso!${
          resposta.data.hangoutLink
            ? `\nLink do Meet: ${resposta.data.hangoutLink}`
            : ""
        }`
      );
      window.location.href = "/";
    } catch (erro) {
      console.error("Erro:", erro);
      alert(erro.response?.data?.message || "Erro ao criar evento");
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
          />
        </label>

        <div className="form-field full-width">
          <label>
            <p>
              Adicionar Horários*
              {errosFormulario.timeSlots && (
                <span className="error-message">
                  {errosFormulario.timeSlots}
                </span>
              )}
            </p>
          </label>
          <div className="timeslot-container">
            <div className="timeslot-inputs">
              <div className="time-input-group">
                <DatePicker
                  selected={novoTimeSlot.start}
                  onChange={(time) => handleTimeSlotChange(time, "start")}
                  showTimeSelect
                  showTimeSelectOnly
                  timeIntervals={15}
                  timeCaption="Hora"
                  dateFormat="HH:mm"
                  timeFormat="HH:mm"
                  locale="pt-BR"
                  className="time-input"
                  placeholderText="Selecione"
                  minTime={createTime(0, 0)} // Start of day (00:00)
                  maxTime={createTime(23, 45)} // End of day (23:45)
                  selectsStart
                  startDate={novoTimeSlot.start}
                  endDate={novoTimeSlot.end}
                />
                <span>às</span>
                <DatePicker
                  selected={novoTimeSlot.end}
                  onChange={(time) => handleTimeSlotChange(time, "end")}
                  showTimeSelect
                  showTimeSelectOnly
                  timeIntervals={15}
                  timeCaption="Hora"
                  dateFormat="HH:mm"
                  timeFormat="HH:mm"
                  locale="pt-BR"
                  className="time-input"
                  placeholderText="Selecione"
                  minTime={novoTimeSlot.start || createTime(0, 0)}
                  maxTime={createTime(23, 45)}
                  selectsEnd
                  startDate={novoTimeSlot.start}
                  endDate={novoTimeSlot.end}
                  filterTime={(time) =>
                    !novoTimeSlot.start ||
                    time.getTime() >=
                      novoTimeSlot.start.getTime() + 15 * 60 * 1000
                  }
                />
              </div>
              {formData.timeSlots.length > 0 && (
                <div className="timeslot-list">
                  {formData.timeSlots.map((slot, index) => (
                    <div key={index} className="timeslot-item">
                      <span>
                        {slot.start.toLocaleTimeString("pt-BR", {
                          hour: "2-digit",
                          minute: "2-digit",
                        })}{" "}
                        -{" "}
                        {slot.end.toLocaleTimeString("pt-BR", {
                          hour: "2-digit",
                          minute: "2-digit",
                        })}
                      </span>
                      <button
                        type="button"
                        className="remover"
                        onClick={() => removerTimeSlot(index)}
                      >
                        <i className="fa-solid fa-close"></i>
                      </button>
                    </div>
                  ))}
                </div>
              )}
              <button
                type="button"
                id="add-timeslot-button"
                className="adicionar"
                onClick={adicionarTimeSlot}
              >
                Adicionar Horário
              </button>
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

        <div className="form-field">
          <label className="checkbox-container">
            <input
              type="checkbox"
              id="criarMeet"
              checked={formData.criarMeet}
              onChange={handleInputChange}
            />
            <span className="checkmark"></span>
            Criar reunião do Google Meet
            <i className="fa-solid fa-video meet-icon"></i>
          </label>
        </div>

        <label htmlFor="local" className="form-field full-width">
          Local
          <input
            type="text"
            id="local"
            value={formData.local}
            onChange={handleInputChange}
            placeholder="Onde o evento acontecerá"
          />
        </label>

        <label htmlFor="descricao" className="form-field full-width">
          Descrição
          <textarea
            id="descricao"
            value={formData.descricao}
            onChange={handleInputChange}
            rows="2"
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
          {carregando ? "Criando..." : "Criar Evento com Votação"}
        </button>
      </form>
    </main>
  );
};

CriarEventoMenu.propTypes = {
  aoFechar: PropTypes.func,
};

export default CriarEventoMenu;
