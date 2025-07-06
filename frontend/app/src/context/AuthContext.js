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

  // Busca dados do usuário autenticado
  const fetchAuth = useCallback(async () => {
    try {
      const response = await axios.get("http://localhost:12003/api/auth/me", {
        withCredentials: true,
      });
      setAuth({
        loading: false,
        authenticated: true,
        user: response.data.user,
        userId: response.data.userId,
        accessToken: response.data.accessToken,
      });
    } catch (err) {
      setAuth({
        loading: false,
        authenticated: false,
        user: null,
        userId: null,
        accessToken: null,
      });
    }
  }, []);

  useEffect(() => {
    fetchAuth();
  }, [fetchAuth]);

  const getAccessToken = () => auth.accessToken;

  const logout = async () => {
    try {
      await axios.post("http://localhost:12003/logout", {}, {
        withCredentials: true,
      });
    } catch (err) {
      console.error("Erro ao deslogar no servidor:", err);
    }

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
