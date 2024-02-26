import React, { useEffect } from "react";
import "../src/styles/main.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ToastContainer, toast, Bounce } from "react-toastify";
import { loadUser, clearErrors } from "./redux/user/userAction";
import HomePage from "./pages/HomePage";
import SignUpPage from "./pages/SignUpPage";
import SignInPage from "./pages/SignInPage";
import NoticePage from "./pages/NoticePage";
import ActivationPage from "./pages/ActivationPage";
import Store from "./redux/store";
import "react-toastify/dist/ReactToastify.css";
import UserProfile from "./pages/UserProfile";
import FindAccountPage from "./pages/FindAccountPage";

const App = () => {
  // 페이지 로딩 시 사용자 정보를 불러오는 액션 실행
  useEffect(() => {
    Store.dispatch(loadUser());
    Store.dispatch(clearErrors());
  }, []);
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="sign-up" element={<SignUpPage />} />
          <Route path="sign-in" element={<SignInPage />} />
          <Route path="notice" element={<NoticePage />} />
          <Route path="active/:activeToken" element={<ActivationPage />} />
          <Route path="profile" element={<UserProfile />} />
          <Route path="findAccount" element={<FindAccountPage />} />
        </Routes>
        <ToastContainer
          position="top-center"
          autoClose={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          theme="colored"
          transition={Bounce}
        />
      </BrowserRouter>
    </>
  );
};

export default App;
