import React, { useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";

const LoginSuccess = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const hasRun = useRef(false); // Usado para não rodar 2 vezes

  useEffect(() => {
    const verifyAndRedirect = async () => {
      // Usado para não rodar 2 vezes
      if (hasRun.current) return;
      hasRun.current = true;

      try {
        // 1. Verifica autenticação
        const response = await fetch("http://localhost:12003/api/auth/check", {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            "X-Requested-With": "XMLHttpRequest",
          },
        });

        if (!response.ok) throw new Error("Not authenticated");

        // 2. Pega o protectedPath do sessionStorage
        const protectedPath = sessionStorage.getItem("preAuthPath") || "/";

        // 3. Limpa o sessionStorage após ter pego o caminho guardado
        sessionStorage.removeItem("preAuthPath");

        // 4. Faz um redirect
        navigate(protectedPath);
      } catch (error) {
        console.error("Login verification failed:", error);
        navigate("/login", {
          state: {
            error: "Authentication failed. Please try again.",
            from: location.state?.from, // Preserva localização original, se disponível
          },
        });
      }
    };

    verifyAndRedirect();
  }, [navigate]);

  return <div>Finalizing authentication...</div>;
};

export default LoginSuccess;
