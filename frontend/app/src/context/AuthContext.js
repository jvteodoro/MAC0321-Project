import React, { createContext, useContext, useState, useEffect, useCallback } from "react";
import axios from "axios";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    loading: true,
    authenticated: false,
    user: null,
    accessToken: null,
  });

  // Fetch user info and access token from backend
  const fetchAuth = useCallback(async () => {
    try {
      const response = await axios.get("http://localhost:12003/api/auth/me", {
        withCredentials: true,
      });
      setAuth({
        loading: false,
        authenticated: true,
        user: response.data.user,
        accessToken: response.data.accessToken,
      });
    } catch (err) {
      setAuth({
        loading: false,
        authenticated: false,
        user: null,
        accessToken: null,
      });
    }
  }, []);

  useEffect(() => {
    fetchAuth();
  }, [fetchAuth]);

  const getAccessToken = () => auth.accessToken;

  const logout = async () => {
    await axios.post("http://localhost:12003/api/auth/logout", {}, { withCredentials: true });
    setAuth({
      loading: false,
      authenticated: false,
      user: null,
      accessToken: null,
    });
  };

  return (
    <AuthContext.Provider value={{ ...auth, getAccessToken, fetchAuth, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
