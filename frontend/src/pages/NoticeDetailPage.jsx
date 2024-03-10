import React from "react";
import Header from "../components/Layout/Header";
import Footer from "../components/Layout/Footer";
import NoticeDetailComponent from "../components/NoticeDetail/NoticeDetailComponent";


const NoticeDetailPage = () => {
  return (
    <>
      <Header />
        <NoticeDetailComponent />
      <Footer />

    </>
  );
};

export default NoticeDetailPage;
