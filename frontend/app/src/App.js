import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginMenu from "./components/LoginMenu/LoginMenu";
import LoginSuccess from "./components/LoginSuccess";
import PrivateRoute from "./components/PrivateRoute";
import PageHeader from "./components/PageHeader/PageHeader";
import CalendarMenu from "./components/CalendarMenu/CalendarMenu";
import EventMenu from "./components/EventMenu";


function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route
            path="/"
            element={
              <PrivateRoute>
                <PageHeader title="Calendário" />
                <CalendarMenu />
              </PrivateRoute>
            }
            exact
          />
          <Route path="/login" element={<LoginMenu />} />
          <Route path="/login-success" element={<LoginSuccess />} />
          <Route
            path="/calendario"
            element={
              <PrivateRoute>
                <PageHeader title="Calendário" />
                <CalendarMenu />
              </PrivateRoute>
            }
          />
          <Route
            path="/evento"
            element={
              <PrivateRoute>
                <PageHeader title="EventoNOME" />
                <EventMenu color="#123456" />
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
