import React from "react";
import TableForm from "../Share/TableForm";

const IntegrationSingleSection = () => (
  <section className="integration-single section pt-0">
    <div className="container">
      <div className="row justify-center">
        <div className="lg:col-10">
          <div
            className="integration-single-container tab rounded-xl bg-white px-5 py-16 shadow-lg md:px-10"
            data-tab-group="service-tab"
          >
            <div className="px-4 text-center">
              <img
                className="mx-auto"
                src="images/icons/webflow-colored.svg"
                alt=""
              />
              <h1 className="mt-6">공지사항</h1>
              <p className="mt-6">
                We are looking for a personal financial planning pro Certified
                Financial Planner preferred)
                <br />
                who will lead our client advising efforts. You will be a
                fiduciary who works
              </p>
              <a className="btn btn-primary mt-8 px-10" href="#">
                Integrate Webflow
              </a>
            </div>
            <div className="my-12 border-y border-border py-3">
                <TableForm />
            </div>
            
            </div>
          </div>
        </div>
      </div>
  </section>
);


export default IntegrationSingleSection;