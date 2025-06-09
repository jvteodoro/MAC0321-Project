import logo from './logo.svg';
import './App.css';
import CalendarMenu from './components/CalendarMenu/CalendarMenu';
import LoginMenu from './components/LoginMenu/LoginMenu'
import PageHeader from './components/PageHeader/PageHeader';
import EventMenu from './components/EventMenu';

function App() {
  return (
    <div className="App">
      {/* <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a> */}
      <LoginMenu/>
      {/* <PageHeader title="Calendário" />
        <CalendarMenu /> */}
      {/* <PageHeader title="EventoNOME" />
      <EventMenu color="#123456" /> */}
    </div>
  );
}

export default App;
