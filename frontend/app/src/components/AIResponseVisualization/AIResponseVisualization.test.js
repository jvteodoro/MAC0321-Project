import React from 'react';
import { shallow, render, mount } from 'enzyme';
import AIResponseVisualization from './AIResponseVisualization';

describe('AIResponseVisualization', () => {
  let props;
  let shallowAIResponseVisualization;
  let renderedAIResponseVisualization;
  let mountedAIResponseVisualization;

  const shallowTestComponent = () => {
    if (!shallowAIResponseVisualization) {
      shallowAIResponseVisualization = shallow(<AIResponseVisualization {...props} />);
    }
    return shallowAIResponseVisualization;
  };

  const renderTestComponent = () => {
    if (!renderedAIResponseVisualization) {
      renderedAIResponseVisualization = render(<AIResponseVisualization {...props} />);
    }
    return renderedAIResponseVisualization;
  };

  const mountTestComponent = () => {
    if (!mountedAIResponseVisualization) {
      mountedAIResponseVisualization = mount(<AIResponseVisualization {...props} />);
    }
    return mountedAIResponseVisualization;
  };  

  beforeEach(() => {
    props = {};
    shallowAIResponseVisualization = undefined;
    renderedAIResponseVisualization = undefined;
    mountedAIResponseVisualization = undefined;
  });

  // Shallow / unit tests begin here
 
  // Render / mount / integration tests begin here
  
});
