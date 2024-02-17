import React from "react";
import Header from "../components/Layout/Header";
import Footer from "../components/Layout/Footer";
import SignUpForm from "../components/SignUp/SignUpForm";
import SignUpBanner from "../components/SignUp/SignUpBanner";

const SignUpPage = () => {
  return (
    <>
      <Header />
 
          <SignUpForm />

      <Footer />
    </>
  );
};

export default SignUpPage;
