import React from "react";
import UserProfileSidebar from "./UserProfileSidebar";
import UserProfileForm from "./UserProfileForm";
import UserProfile from "./UserProfile";

const UserProfileMain = () => {
  return (
    <>
      {/* Floating assets */}
      <img
        className="floating-bubble-1 absolute right-0 top-0 -z-[1]"
        src="/images/floating-bubble-1.svg"
        alt=""
      />
      <img
        className="floating-bubble-2 absolute left-0 top-[387px] -z-[1]"
        src="/images/floating-bubble-2.svg"
        alt=""
      />
      <img
        className="floating-bubble-3 absolute right-0 top-[605px] -z-[1]"
        src="/images/floating-bubble-3.svg"
        alt=""
      />
      <section className="integration-single section pt-0">
        <div className="container">
          <div className="row justify-center">
            <div className="lg:col-12">
                
              <div
                className="integration-single-container tab rounded-xl  px-5 py-4  md:px-3"
                data-tab-group="service-tab"
              >
                {/* <h1 className="text-center">회원정보</h1> */}
                {/* <UserProfileSidebar /> */}
                {/* Content goes here */}
                {/* <UserProfileForm /> */}
                <UserProfile />
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default UserProfileMain;
