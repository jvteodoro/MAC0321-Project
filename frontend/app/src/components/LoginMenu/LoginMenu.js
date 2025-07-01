import Google_Icon from "../../assets/Google_Icon.webp";
import { useLocation } from "react-router-dom";
import "./LoginMenu.css";

const LoginMenu = (props) => {
  const location = useLocation();

  const handleGoogleLogin = () => {
    // Pega a rota protected do state do navigation ou dá fallback pro caminho atual
    const protectedPath =
      location.state?.from?.pathname || window.location.pathname;

    // Guarda na sessionStorage pra manter durante o redirecionamento do OAuth
    sessionStorage.setItem("preAuthPath", protectedPath);

    // Redireciona para o login do Google do backend
    window.location.href = "http://localhost:12003/oauth2/authorization/Google";
  };

  return (
    <div id="menu-wrapper" className="modal">
      <h1>AgendUSP</h1>
      <div id="login-menu" className="modal">
        <h2 id="login-header">Login</h2>
        <button id="googleSignIn" type="button" onClick={handleGoogleLogin}>
          <img src={Google_Icon} alt="Google Icon"/>
          Entrar com o Google
        </button>
      </div>
    </div>
  );
};

export default LoginMenu;
