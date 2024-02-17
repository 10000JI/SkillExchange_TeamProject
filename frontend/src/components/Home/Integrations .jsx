import React from "react";
import {integrationData} from '../../dummyData';
const IntegrationBox = ({ imageSrc, title, category, description }) => {
  return (
    <div className="integration-tab-item mb-8 md:col-6 lg:col-4" data-groups={`["${category}"]`}>
      <div className="rounded-xl bg-white px-10 pb-8 pt-11 shadow-lg max-h-[350px] min-h-[350px]">
        <div className="integration-card-head flex items-center space-x-4">
          <img src={imageSrc} alt="" />
          <div>
            <h4 className="h5">{title}</h4>
            <span className="font-medium">카테고리 : {category}</span>
          </div>
        </div>
        <div className="my-5 border-y border-border py-5">
          <p>{description}</p>
        </div>
        <a className="inline-flex items-center font-semibold text-dark" href="#">
          글 상세보기 
          <svg
            className="ml-1.5"
            width="13"
            height="16"
            viewBox="0 0 13 16"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M12.7071 8.70711C13.0976 8.31658 13.0976 7.68342 12.7071 7.29289L6.34315 0.928932C5.95262 0.538408 5.31946 0.538408 4.92893 0.928932C4.53841 1.31946 4.53841 1.95262 4.92893 2.34315L10.5858 8L4.92893 13.6569C4.53841 14.0474 4.53841 14.6805 4.92893 15.0711C5.31946 15.4616 5.95262 15.4616 6.34315 15.0711L12.7071 8.70711ZM0 9H12V7H0V9Z"
              fill="currentColor"
            />
          </svg>
        </a>
      </div>
    </div>
  );
};

const Integrations = () => {

  return (
    <section className="section pt-0">
      <div className="container">
        <div className="row justify-center">
          <div className="lg:col-10">
            <ul className="integration-tab filter-list justify-center">
              {/* 필터 버튼들 */}
            </ul>
          </div>
        </div>
        <div className="integration-tab-items row mt-10">
          {/* 데이터를 IntegrationBox 컴포넌트로 매핑 */}
          {integrationData.map((item) => (
            <IntegrationBox
              key={item.id}
              imageSrc={item.imageSrc}
              title={item.title}
              category={item.category}
              description={item.description}
            />
          ))}
        </div>
      </div>
    </section>
  );
};

export default Integrations;
