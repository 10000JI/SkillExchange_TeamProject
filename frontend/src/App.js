import '../src/styles/main.scss';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import SignUpPage from './pages/SignUpPage';
import SignInPage from './pages/SignInPage';
import NoticePage from './pages/NoticePage';


function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="sign-up" element={<SignUpPage />} />
        <Route path="sign-in" element={<SignInPage />} />
        <Route path="notice" element={<NoticePage />} />
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
