import React from "react";
import Header from "../components/Layout/Header";
import Footer from "../components/Layout/Footer";
import CreateNotice from "../components/Notice/CreateNotice";



const CreateNoticePage = () => {
  return (
    <>
      <Header />
        <CreateNotice />
      <Footer />

    </>
  );
};

export default CreateNoticePage;
