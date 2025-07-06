import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom'; // Adicione esta linha

const AIResponseVisualization = () => {
   const location = useLocation();
  const { calendarId, firstDay, lastDay } = location.state || {};
  const [responseData, setResponseData] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAIResponse = async () => {
      if (!calendarId || !firstDay) return;

      setLoading(true);
      setError(null);

      const prompt = await axios.get(
          `http://localhost:12003//prompt/semana?firstDay=${firstDay}&calendarId=${calendarId}`, { withCredentials: true }
        );


      try {
        const response = await axios.get(
          `http://localhost:12003/aiReport?firstDay=${firstDay}&calendarId=${calendarId}&prompt=${prompt.data}`, { withCredentials: true }
        );
        setResponseData(response.data);
      } catch (err) {
        setError('Erro ao buscar os dados.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchAIResponse();
  }, [calendarId, firstDay]);

  return (
    <div style={{ padding: '20px' }}>
      <h2>Visualização da Resposta da IA</h2>
      <p><strong>Calendário:</strong> {calendarId}</p>
      <p><strong>Data Inicial:</strong> {firstDay}</p>
      <p><strong>Data Final:</strong> {lastDay}</p>

      {loading && <p>Carregando resposta...</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {!loading && !error && (
        <div style={{ marginTop: '20px', whiteSpace: 'pre-wrap' }}>
          <h3>Resposta da API:</h3>
          <p>{responseData}</p>
        </div>
      )}
    </div>
  );
};

export default AIResponseVisualization;