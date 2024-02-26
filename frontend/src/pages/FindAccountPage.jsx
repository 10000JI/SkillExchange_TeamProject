
import React, { useEffect } from "react";
import Header from "../components/Layout/Header";
import Footer from "../components/Layout/Footer";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import FindForm from "../components/FindAccount/FindForm";

const FindAccountPage = () => {
  const navigate = useNavigate();
  const { isAuthenticated} = useSelector((state) => state.user);

  useEffect(() => {
      if(isAuthenticated === true) {
          navigate("/")
      }
  })
  return (
    <>
      <Header />
 
          <FindForm />
          

      <Footer />
      
    </>
  );
};

export default FindAccountPage;
