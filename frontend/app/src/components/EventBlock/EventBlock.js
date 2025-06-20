import React from "react";
import PropTypes from "prop-types";
import "./EventBlock.css";
import axios from "axios";

const EventBlock = ({ eventInfo, clickable }) => {
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

  function redirect (shouldRedirect, calendarId, eventId) {
    if (shouldRedirect)
      window.location.assign(window.location.origin + `/evento?calendarId=${calendarId}&eventId=${eventId}`);
  };

  return (
    <div
      className={`eventBlock${clickable ? " link" : ""}`}
      onClick={() => redirect(clickable, eventInfo.calendarId, eventInfo.eventId)}
      style={{ backgroundColor: colorMap[(eventInfo.colorId || 0)] }}
    >
      <span>
        {eventInfo.title} ({eventInfo.startTime} - {eventInfo.endTime})
      </span>
    </div>
  );
};

const EventBlockPropTypes = {
  // always use prop types!
};

EventBlock.propTypes = EventBlockPropTypes;

export default EventBlock;
