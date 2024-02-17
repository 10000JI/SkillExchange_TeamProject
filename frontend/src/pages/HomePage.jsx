import React from "react";
import Banner from "../components/Home/Banner ";
import KeyFeatures from "../components/Home/KeyFeatures";
import Services from "../components/Home/Services";
import DownloadSection from "../components/Home/DownloadSection";
import Header from "../components/Layout/Header";
import Footer from "../components/Layout/Footer";
import Integrations from "../components/Home/Integrations ";

const HomePage = () => {
  return (
    <>
    <Header />
    <Banner />
    {/* <KeyFeatures /> */}
    <Integrations />
    <DownloadSection />
    <Footer />
    </>
  );
};

export default HomePage;
