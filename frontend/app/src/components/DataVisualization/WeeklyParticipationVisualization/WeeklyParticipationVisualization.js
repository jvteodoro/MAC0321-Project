import React from "react";
import PropTypes from "prop-types";
import "../DataVisualization.css";

const WeeklyParticipationVisualization = ({ data }) => {
  // Sample data structure:
//   {
//     title: "Team Sync Meetings",
//     timePeriod: "Jun 1-7, 2023",
//     totalParticipants: 24,
//     averageAttendance: 18,
//     participationRate: "75%",
//     topAttendees: ["Maria S.", "John D.", "Lisa T."],
//     notes: "Attendance dropped mid-week due to company-wide conference"
//   }

  return (
    <div className="visualization-container participation">
      <h2>{data.title || "Weekly Participation Data"}</h2>
      <div className="visualization-content">
        <p>
          <strong>Time Period:</strong> {data.timePeriod || "N/A"}
        </p>
        <p>
          <strong>Total Unique Participants:</strong>{" "}
          {data.totalParticipants || "0"}
        </p>
        <p>
          <strong>Average Attendance:</strong> {data.averageAttendance || "0"}{" "}
          per meeting
        </p>
        <p>
          <strong>Participation Rate:</strong> {data.participationRate || "0%"}
        </p>

        {data.topAttendees && data.topAttendees.length > 0 && (
          <>
            <p>
              <strong>Top Attendees:</strong>
            </p>
            <ul>
              {data.topAttendees.map((attendee, index) => (
                <li key={index}>{attendee}</li>
              ))}
            </ul>
          </>
        )}

        {data.notes && (
          <>
            <p>
              <strong>Notes:</strong>
            </p>
            <p className="visualization-notes">{data.notes}</p>
          </>
        )}
      </div>
    </div>
  );
};

WeeklyParticipationVisualization.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string,
    timePeriod: PropTypes.string,
    totalParticipants: PropTypes.number,
    averageAttendance: PropTypes.number,
    participationRate: PropTypes.string,
    topAttendees: PropTypes.arrayOf(PropTypes.string),
    notes: PropTypes.string,
  }),
};

export default WeeklyParticipationVisualization;
