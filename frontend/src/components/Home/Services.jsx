// Services 컴포넌트
import React from 'react';

const Services = () => {
  return (
    <section className="section services">
      <div className="container">
        {/* 첫 번째 탭 내용 */}
        <div className="tab row gx-5 items-center" data-tab-group="integration-tab">
          {/* 첫 번째 탭의 내용 */}
        </div>
        {/* 두 번째 탭 내용 */}
        <div className="row gx-5 mt-12 items-center lg:mt-0">
          {/* 두 번째 탭의 내용 */}
        </div>
        {/* 세 번째 탭 내용 */}
        <div className="row gx-5 mt-12 items-center lg:mt-0">
          {/* 세 번째 탭의 내용 */}
        </div>
      </div>
    </section>
  );
};

export default Services;