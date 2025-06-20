import React from 'react';
import PropTypes from 'prop-types';
import "./PageHeader.css";

const PageHeader = (props) => (
  <header id="page-header">
    <h1>AgendUSP</h1>
    <h1 id="page-title">{props["title"]}</h1>	
	<div id="nav-buttons">
		<form action={null} method="POST">
			<button type="submit">Logout</button>
		</form>
		<form action={null} method="POST">
			<button type="submit">Salvar</button>
		</form>
	</div>
  </header>
);

const PageHeaderPropTypes = {
	// always use prop types!
};

PageHeader.propTypes = PageHeaderPropTypes;

export default PageHeader;
