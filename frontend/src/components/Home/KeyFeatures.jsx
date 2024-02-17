import React from 'react';
import {FeatureCardData} from '../../dummyData';

const KeyFeatures = () => {
  return (
    <section className="section key-feature relative">
      <img
        className="absolute left-0 top-0 -z-[1] -translate-y-1/2"
        src="images/icons/feature-shape.svg"
        alt=""
      />
      <div className="container">
        <div className="row justify-between text-center lg:text-start">
          <div className="lg:col-5">
            <h2>재능 교환</h2>
          </div>
          <div className="mt-6 lg:col-5 lg:mt-0">
            <p>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi egestas
              Werat viverra id et aliquet. vulputate egestas sollicitudin .
            </p>
          </div>
        </div>
        <div
          className="key-feature-grid mt-10 grid grid-cols-2 gap-7 md:grid-cols-3 xl:grid-cols-4"
        >
          {FeatureCardData.map((user) => (
            <FeatureCard
              key={user.id}
              title={user.preferredSubject}
              description={user.mySubject}
              icon={user.avatar}
            />
          ))}
        </div>
      </div>
    </section>
    
    
  );
};

// 개별 Feature를 나타내는 컴포넌트
const FeatureCard = ({ title, description, icon }) => (
  <div className="flex flex-col justify-between rounded-lg bg-white p-5 shadow-lg">
    <div>
      <h3 className="h4 text-xl lg:text-2xl">{title}</h3>
      <p>{description}</p>
    </div>
    <span className="icon mt-4">
      <img className="object-contain" src={icon} alt="" />
    </span>
  </div>
);

export default KeyFeatures;