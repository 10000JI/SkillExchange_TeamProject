import React from "react";
import TableForm from "../Share/TableForm";

const Notice = () => {
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
      {/* End floating assets */}
      {/* Common hero */}
      <section className="page-hero pb-8 pt-16">
        <div className="container">
          <div className="text-center">
            <ul className="breadcrumb inline-flex h-8 items-center justify-center space-x-2 rounded-3xl bg-theme-light px-4 py-2">
              <li className="leading-none text-dark">
                <a className="inline-flex items-center text-primary" href="#">
                  <svg
                    className="mr-1.5"
                    width="15"
                    height="15"
                    viewBox="0 0 16 16"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M13.1769 15.0588H10.3533V9.41178H5.64744V15.0588H2.82391V6.58825H1.88274V16H14.118V6.58825H13.1769V15.0588ZM6.58862 15.0588V10.353H9.41215V15.0588H6.58862ZM15.8084 6.09225L15.2512 6.85178L8.00038 1.52472L0.749559 6.8499L0.192383 6.09131L8.00038 0.357666L15.8084 6.09225Z"
                      fill="black"
                    />
                  </svg>
                  <span className="text-sm leading-none">Home</span>
                </a>
              </li>
              <li className="leading-none text-dark">
                <span className="text-sm leading-none">
                  / 공지사항
                </span>
              </li>
            </ul>
          </div>
        </div>
      </section>
      {/* End Common hero */}

      <section className="integration-single section pt-0">
        <div className="container">
          <div className="row justify-center">
            <div className="lg:col-10">
                
              <div
                className="integration-single-container tab rounded-xl bg-white px-5 py-16 shadow-lg md:px-10"
                data-tab-group="service-tab"
              >
                <h1 className="text-center">공지사항</h1>
                <TableForm />
                {/* Content goes here */}
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default Notice;
