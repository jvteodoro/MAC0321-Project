import React, { useState, useEffect } from "react";
import "./App.css";
import { BrowserRouter as Router, Routes, Route, Outlet } from "react-router-dom";
import LoginMenu from "./components/LoginMenu/LoginMenu";
import PrivateRoute from "./components/PrivateRoute";
import PageHeader from "./components/PageHeader/PageHeader";
import CalendarMenu from "./components/CalendarMenu/CalendarMenu";
import EventMenu from "./components/EventMenu";


function App() {
  let baseURL = "http://localhost:12003"

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await fetch(`${baseURL}/secured`);
        if (response.ok) {
          setIsAuthenticated(true);
        } else {
          setIsAuthenticated(false);
        }
      } catch (error) {
        console.error("Error checking authentication", error);
        setIsAuthenticated(false);
      }
    };
    checkAuth();
  }, []);

  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<LoginMenu />} exact />
          <Route path="/login" element={<LoginMenu />} />
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
