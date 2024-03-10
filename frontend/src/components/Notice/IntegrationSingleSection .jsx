import React from "react";
import TableForm from "../Share/TableForm";
import { Link } from "react-router-dom";

const IntegrationSingleSection = () => (
  <section className="integration-single section pt-10">
    <div className="container">
      <div className="row justify-center">
        <div className="lg:col-12">
          <div
            className="integration-single-container tab rounded-xl bg-white px-5 py-10 shadow-lg md:px-10"
            data-tab-group="service-tab"
          >
            <div className="px-4 text-center">
              {/* <img
                className="mx-auto"
                src="images/icons/webflow-colored.svg"
                alt=""
              /> */}
              <h1 className="mt-6">공지사항</h1>
              <Link to="/notice-create" className="btn btn-primary mt-8 px-10">
                글 등록하기
              </Link>
            </div>
            <div className="my-12 border-y border-border py-3">
              <h4 className="h5 ml-2 mt-2 inline-block border-b-[3px] border-primary font-primary font-medium leading-8">
                공지사항
              </h4>
              <TableForm />
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
);

export default IntegrationSingleSection;
