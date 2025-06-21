import React from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginMenu from "./components/LoginMenu/LoginMenu";
import LoginSuccess from "./components/LoginSuccess";
import PrivateRoute from "./components/PrivateRoute";
import PageHeader from "./components/PageHeader/PageHeader";
import CalendarMenu from "./components/CalendarMenu/CalendarMenu";
import CreateEventMenu from "./components/EventMenus/CreateEventMenu";
import EditEventMenu from "./components/EventMenus/EditEventMenu";


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
            path="/evento/editar"
            element={
              <PrivateRoute>
                <PageHeader title="Editar Evento" />
                <EditEventMenu/>
              </PrivateRoute>
            }
          />
          <Route
            path="/evento/criar"
            element={
              <PrivateRoute>
                <PageHeader title="Criar Evento" />
                <CreateEventMenu/>
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
