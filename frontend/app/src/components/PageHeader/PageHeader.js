import React from 'react';
import PropTypes from 'prop-types';
import styles from './PageHeader.css';

const PageHeader = (props) => (
	<header id='page-header'>
		<h1>AgendUSP</h1>
		<h1 id='page-title'>{props["title"]}</h1>
	</header>
	);

const PageHeaderPropTypes = {
	// always use prop types!
};

PageHeader.propTypes = PageHeaderPropTypes;

export default PageHeader;
