import React from 'react';
import { shallow, render, mount } from 'enzyme';
import WeeklyParticipationVisualization from './WeeklyParticipationVisualization';

describe('WeeklyParticipationVisualization', () => {
  let props;
  let shallowWeeklyParticipationVisualization;
  let renderedWeeklyParticipationVisualization;
  let mountedWeeklyParticipationVisualization;

  const shallowTestComponent = () => {
    if (!shallowWeeklyParticipationVisualization) {
      shallowWeeklyParticipationVisualization = shallow(<WeeklyParticipationVisualization {...props} />);
    }
    return shallowWeeklyParticipationVisualization;
  };

  const renderTestComponent = () => {
    if (!renderedWeeklyParticipationVisualization) {
      renderedWeeklyParticipationVisualization = render(<WeeklyParticipationVisualization {...props} />);
    }
    return renderedWeeklyParticipationVisualization;
  };

  const mountTestComponent = () => {
    if (!mountedWeeklyParticipationVisualization) {
      mountedWeeklyParticipationVisualization = mount(<WeeklyParticipationVisualization {...props} />);
    }
    return mountedWeeklyParticipationVisualization;
  };  

  beforeEach(() => {
    props = {};
    shallowWeeklyParticipationVisualization = undefined;
    renderedWeeklyParticipationVisualization = undefined;
    mountedWeeklyParticipationVisualization = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
