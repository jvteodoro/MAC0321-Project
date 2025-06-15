import React, { useState, useEffect } from "react";
import { Navigate, useLocation } from "react-router-dom";

const PrivateRoute = ({ children }) => {
  const [authState, setAuthState] = useState({
    loading: true,
    authenticated: false,
  });
  const location = useLocation();

  useEffect(() => {
    const verifyAuth = async () => {
      try {
        const response = await fetch("http://localhost:12003/api/auth/check", {
          method: "GET",
          credentials: "include", // Necessário para cookies/sessions
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
          cache: "no-store", // Não guarda o estado de autenticação no cache
        });

        setAuthState({
          loading: false,
          authenticated: response.ok,
        });

        if (!response.ok) {
          // Guarda o caminho protected inteiro (pathname + search)
          const protectedPath = location.pathname + location.search;
          sessionStorage.setItem("preAuthPath", protectedPath);
          throw new Error("Unauthorized");
        }
      } catch (error) {
        console.error("Auth verification failed:", error);
        setAuthState({
          loading: false,
          authenticated: false,
        });
      }
    };

    verifyAuth();
  }, [location.pathname, location.search]);

  if (authState.loading) {
    return <div className="auth-loading">Verifying authentication...</div>;
  }

  return authState.authenticated ? (
    children
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default PrivateRoute;
