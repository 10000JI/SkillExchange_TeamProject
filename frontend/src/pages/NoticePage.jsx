import React from "react";
import Header from "../components/Layout/Header";
import Footer from "../components/Layout/Footer";
import Notice from "../components/Notice/Notice";
import FloatingBubbles from "../components/Notice/FloatingBubbles ";
import CommonHero from "../components/Notice/CommonHero";
import IntegrationSingleSection from "../components/Notice/IntegrationSingleSection ";


const NoticePage = () => {
  return (
    <>
      <Header />
      <FloatingBubbles />
      <CommonHero />
      <IntegrationSingleSection />
      <Footer />

    </>
  );
};

export default NoticePage;
