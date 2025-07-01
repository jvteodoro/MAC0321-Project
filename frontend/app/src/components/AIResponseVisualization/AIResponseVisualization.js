import React from "react";
import PropTypes from "prop-types";
import "./AIResponseVisualization.css";

const AIResponseVisualization = ({ response }) => {
  if (!response) {
    return (
      <div className="visualization-container ai-response">
        No response data.
      </div>
    );
  }
  // Sample response structure:
  // {
  //   question: "What are the benefits of React?",
  //   answer: "React offers several benefits including component reusability...",
  //   confidence: "High",
  //   sources: ["React Documentation", "Stack Overflow 2022 Survey"],
  //   timestamp: "2023-06-15T14:30:00Z"
  // }

  return (
    <div className="visualization-container ai-response">
      {response.question && <h3>Question: {response.question}</h3>}

      <div className="ai-answer">
        {response.answer ? (
          <>
            <p>
              <strong>AI Answer:</strong>
            </p>
            <p>{response.answer}</p>
          </>
        ) : (
          <p>No answer provided</p>
        )}
      </div>

      <div className="response-meta">
        {response.confidence && (
          <p>
            <strong>Confidence:</strong> {response.confidence}
          </p>
        )}

        {response.sources && response.sources.length > 0 && (
          <>
            <p>
              <strong>Sources:</strong>
            </p>
            <ul>
              {response.sources.map((source, index) => (
                <li key={index}>{source}</li>
              ))}
            </ul>
          </>
        )}

        {response.timestamp && (
          <p className="timestamp">
            <small>
              Generated on: {new Date(response.timestamp).toLocaleString()}
            </small>
          </p>
        )}
      </div>
    </div>
  );
};

AIResponseVisualization.propTypes = {
  response: PropTypes.shape({
    question: PropTypes.string,
    answer: PropTypes.string,
    confidence: PropTypes.string,
    sources: PropTypes.arrayOf(PropTypes.string),
    timestamp: PropTypes.string,
  }).isRequired,
};

export default AIResponseVisualization;
