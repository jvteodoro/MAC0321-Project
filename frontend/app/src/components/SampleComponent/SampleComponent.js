import React from 'react';
import PropTypes from 'prop-types';
import styles from './SampleComponent.css';

const SampleComponent = props => (
	<div><h1>This is a component called SampleComponent.</h1></div>
);

// todo: Unless you need to use lifecycle methods or local state,
// write your component in functional form as above and delete
// this section. 
// class SampleComponent extends React.Component {
//   render() {
//     return <div>This is a component called SampleComponent.</div>;
//   }
// }

const SampleComponentPropTypes = {
	// always use prop types!
};

SampleComponent.propTypes = SampleComponentPropTypes;

export default SampleComponent;
