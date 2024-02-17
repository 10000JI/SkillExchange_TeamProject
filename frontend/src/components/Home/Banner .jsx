// Banner 컴포넌트
import React from "react";

const Banner = () => {
  return (
    <section className="section banner relative">
      <div className="container">
        <div className="row items-center">
          <div className="lg:col-6">
            <h1 className="banner-title">재능교환 사이트입니더잉</h1>
            <p className="mt-6">
              "지금 가입하고 다양한 분야에서 재능을 나누어보세요. 새로운 사람들과
              소통하며 즐거운 경험을 만들고, 다양한 영역에서의 역량을
              향상시킬 수 있는 기회를 만나보세요! 재능 교환을 통해 풍부한 경험과
              유익한 인연을 만들어나가는 여정에 참여해보세요."
            </p>
            <a className="btn btn-primary mt-8 " href="#">
              <p className="mt-1 text-lg font-bold">재능 등록하기</p>
            </a>
          </div>
          <div className="lg:col-6">
            <img
              className="w-full object-contain"
              src="./images/banner-img.png"
              width="603"
              height="396"
              alt=""
            />
          </div>
        </div>
      </div>
      <img
        className="banner-shape absolute -top-28 right-0 -z-[1] w-full max-w-[30%]"
        src="./images/banner-shape.svg"
        alt=""
      />
    </section>
    
  );
};

export default Banner;
