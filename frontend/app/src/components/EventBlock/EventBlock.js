import React from "react";
import PropTypes from "prop-types";
import "./EventBlock.css";

const EventBlock = (eventInfo) => {
  return (
    <div className="eventBlock" style={{ backgroundColor: eventInfo.eventInfo["color"] }}>
      <span>Time</span>
    </div>
  );
};

const EventBlockPropTypes = {
  // always use prop types!
};

EventBlock.propTypes = EventBlockPropTypes;

export default EventBlock;
