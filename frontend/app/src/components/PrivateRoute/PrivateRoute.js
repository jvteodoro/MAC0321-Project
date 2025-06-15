import React from 'react';
import PropTypes from 'prop-types';
import styles from './PrivateRoute.css';
import { Navigate } from "react-router-dom";


const PrivateRoute = ({ children, isAuthenticated }) => {
	console.log(isAuthenticated);
	return isAuthenticated ? children : <Navigate to="/login" />;
};

// todo: Unless you need to use lifecycle methods or local state,
// write your component in functional form as above and delete
// this section. 
// class PrivateRoute extends React.Component {
//   render() {
//     return <div>This is a component called PrivateRoute.</div>;
//   }
// }

const PrivateRoutePropTypes = {
	// always use prop types!
};

PrivateRoute.propTypes = PrivateRoutePropTypes;

export default PrivateRoute;
