import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import style from './AIResponseVisualization.css';

const AIResponseVisualization = () => {
   const location = useLocation();
  const { calendarId, firstDay } = location.state || {};
  const [responseData, setResponseData] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAIResponse = async () => {
      if (!calendarId || !firstDay) return;
      setLoading(true);
      setError(null);

      try {
        const response = await axios.get(
          `http://localhost:12003/prompt/semana?firstDay=${firstDay}&calendarId=${calendarId}`,
          { withCredentials: true }
        );
        setResponseData(response.data);
      } catch (err) {
        setError("Erro ao buscar os dados.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchAIResponse();
  }, [calendarId, firstDay]);

  // Converts the AI response string to HTML
  function parseAIResponseToHTML(response) {
    if (!response) return '';
    let html = response;

    // Bold sections: **text**
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

    // Unordered lists: lines starting with -
    html = html.replace(/(?:^|\n)- (.*?)(?=\n|$)/g, '<li>$1</li>');
    // Ordered lists: lines starting with 1. 2. etc.
    html = html.replace(/(?:^|\n)(\d+)\. (.*?)(?=\n|$)/g, '<li>$1. $2</li>');

    // Wrap consecutive <li> in <ul> or <ol>
    // Unordered
    html = html.replace(/(<li>.*?<\/li>)+/gs, match => `<ul>${match}</ul>`);
    // Remove double <ul><ul> if any
    html = html.replace(/<\/ul>\s*<ul>/g, '');

    // Paragraphs: split by double newlines
    html = html.replace(/\n{2,}/g, '</p><p>');
    // Single newlines to <br>
    html = html.replace(/\n/g, '<br>');

    // Wrap everything in <p> if not already
    if (!/^<p>/.test(html)) html = `<p>${html}</p>`;

    return html;
  }

  return (
    <div id="responseViewer">
      <h2>Visualização do relatório</h2>

      {loading && <p>Carregando resposta...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && !error && (
        <div
          dangerouslySetInnerHTML={{
            __html: parseAIResponseToHTML(responseData.response)
          }}
        />
      )}
    </div>
  );
};

export default AIResponseVisualization;
