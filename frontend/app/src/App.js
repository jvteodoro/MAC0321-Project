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
import VoteMenu from "./components/VoteMenu/VoteMenu";
// import CreatePollMenu from "./components/OBSOLETE_CreatePollMenu/CreatePollMenu";
import AIResponseVisualization from "./components/AIResponseVisualization/AIResponseVisualization";


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
            path="/evento/criar"
            element={
              <PrivateRoute>
                <PageHeader title="Criar Reunião" />
                <CreateEventMenu/>
              </PrivateRoute>
            }
          />
            <Route
              path="/evento/editar"
              element={
                <PrivateRoute>
                  <PageHeader title="Editar Reunião" />
                  <EditEventMenu/>
                </PrivateRoute>
              }
            />
            {/* <Route
              path="/evento/criarEnquete"
              element={
                <PrivateRoute>
                  <PageHeader title="Criar Enquete" />
                  <CreatePollMenu/>
                </PrivateRoute>
              }
            /> */}
          <Route
            path="/votar"
            element={
              <PrivateRoute>
                <PageHeader title="Votar" />
                <VoteMenu/>
              </PrivateRoute>
            }
          />
          <Route
            path="/relatorioIA"
            element={
              <PrivateRoute>
                <PageHeader title="Relatório IA" />
                <AIResponseVisualization/>
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
