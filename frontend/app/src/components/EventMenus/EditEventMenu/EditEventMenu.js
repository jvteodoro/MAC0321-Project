import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { useLocation } from "react-router-dom";
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "../EventMenu.css";
import "./EditEventMenu.css";
import ptBR from "date-fns/locale/pt-BR";

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
  const { calendarId, eventId } = location.state || {};

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
            convidados: evento.attendees
              ? evento.attendees.map((a) => a.email)
              : [],
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
        }
      } catch (erro) {
        console.error("Erro ao carregar reunião:", erro);
        console.err("Erro ao carregar dados da reunião");
      } finally {
        setCarregando(false);
      }
    };

    if (calendarId && eventId) {
      carregarEvento();
    }
  }, [calendarId, eventId]);

  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setFormData((prev) => ({ ...prev, [id]: value }));
  };

  const handleDateChange = (data, campo) => {
    setFormData((prev) => ({ ...prev, [campo]: data }));
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
        dateTime: formData.dataInicio.toISOString(),
        timeZone: "America/Sao_Paulo",
      };
      updatedEvent.end = {
        dateTime: formData.dataFim.toISOString(),
        timeZone: "America/Sao_Paulo",
      };
      updatedEvent.colorId = formData.cor.toString();
      updatedEvent.description = formData.descricao || null;
      updatedEvent.location = formData.local || null;
      updatedEvent.attendees =
        formData.convidados.length > 0
          ? formData.convidados.map((email) => ({ email }))
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
      console.err(erro.response?.data?.message || "Erro ao atualizar reunião");
    }
  };

  const aoFechar = () => {
    window.location.href = "http://localhost:3000/";
  };

  if (carregando) {
    return (
      <main id="event-menu">
        <div className="carregando">Carregando dados da reunião...</div>
      </main>
    );
  }

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
          />
        </label>

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

        <label htmlFor="local" className="form-field full-width">
          Local
          <input
            type="text"
            id="local"
            value={formData.local}
            onChange={handleInputChange}
            placeholder="Onde a reunião acontecerá"
            autoComplete="off"
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

        <button type="submit" id="save-button" className="form-field">
          Salvar Alterações
        </button>
      </form>
    </main>
  );
};

EditarEventoMenu.propTypes = {
  aoFechar: PropTypes.func,
};

export default EditarEventoMenu;
