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
  const { calendarId } = location.state || {};

  // Estados do componente
  const [formData, setFormData] = useState({
    titulo: "",
    dataInicio: null,
    dataFim: null,
    cor: 0, // Armazena apenas o ID da cor
    descricao: "",
    local: "",
    convidados: [],
    novoConvidado: "",
  });

  const [errosFormulario, setErrosFormulario] = useState({});
  const [mostrarSeletorCor, setMostrarSeletorCor] = useState(false);

  useEffect(() => {
    registerLocale("pt-BR", ptBR);
    // Define a data inicial como agora + 1 hora
    const dataPadrao = new Date();
    dataPadrao.setHours(dataPadrao.getHours() + 1);

    setFormData((prev) => ({
      ...prev,
      dataInicio: dataPadrao,
      dataFim: new Date(dataPadrao.getTime() + 60 * 60 * 1000), // +1 hora
    }));
  }, []);

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
      const dadosEvento = {
        summary: formData.titulo,
        start: { dateTime: formData.dataInicio.toISOString() },
        end: { dateTime: formData.dataFim.toISOString() },
        colorId: formData.cor,
        description: formData.descricao,
        location: formData.local,
        attendees: formData.convidados.map((email) => ({ email })),
      };

      await axios.post(
        `http://localhost:12003/google/events/create?calendarId=${calendarId}`,
        dadosEvento,
        { withCredentials: true }
      );

      alert("Evento criado com sucesso!");
      window.location.href = "http://localhost:3000/";
    } catch (erro) {
      console.error("Falha ao criar evento:", erro);
      alert(erro.response?.data?.message || "Erro ao criar evento");
    }
  };

  const aoFechar = () => {
    window.location.href = "http://localhost:3000/";
  };

  return (
    <main id="event-menu">
      <button id="close-button" onClick={aoFechar}>
        <i className="fa-solid fa-close"></i>
      </button>

      <form id="event-data-form" onSubmit={handleSubmit}>
        <label htmlFor="titulo" className="form-field full-width">
          Nome do Evento*
          <input
            type="text"
            id="titulo"
            value={formData.titulo}
            onChange={handleInputChange}
            className={errosFormulario.titulo ? "error-input" : ""}
          />
          {errosFormulario.titulo && (
            <span className="error-message">{errosFormulario.titulo}</span>
          )}
        </label>

        <label htmlFor="dataInicio" className="form-field">
          Data Inicial*
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
          {errosFormulario.dataInicio && (
            <span className="error-message">{errosFormulario.dataInicio}</span>
          )}
        </label>

        <label htmlFor="dataFim" className="form-field">
          Data Final
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
          {errosFormulario.dataFim && (
            <span className="error-message">{errosFormulario.dataFim}</span>
          )}
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
            rows="1"
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
                className="adicionar-convidado"
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
                    className="remover-convidado"
                    onClick={() => removerConvidado(email)}
                  >
                    <i className="fa-solid fa-close"></i>
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        <button type="submit" id="create-button" className="form-field">
          Criar Evento
        </button>
      </form>
    </main>
  );
};

CriarEventoMenu.propTypes = {
  aoFechar: PropTypes.func,
};

export default CriarEventoMenu;
