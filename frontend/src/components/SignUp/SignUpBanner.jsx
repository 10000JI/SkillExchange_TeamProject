import React, { useEffect } from 'react';
import Swiper from 'swiper';
import 'swiper/css';


const SignUpBanner = () => {
    useEffect(() => {
        // Swiper 초기화
        new Swiper('.swiper-container', {
          // Swiper 옵션 설정
          slidesPerView: 1,
          spaceBetween: 30,
          pagination: {
            el: '.pagination',
            clickable: true,
          },
        });
      }, []); // useEffect를 빈 배열로 전달하여 한 번만 실행되도록 함
    
      return (
        <div className="swiper-container auth-banner-carousel">
          <div className="swiper-wrapper">
            <div className="swiper-slide">
              <img
                width="667"
                height="557"
                className="mx-auto"
                src="images/signup-carousel-img-1.png"
                alt=""
              />
            </div>
            <div className="swiper-slide">
              <img
                width="667"
                height="557"
                className="mx-auto"
                src="images/signup-carousel-img-1.png"
                alt=""
              />
            </div>
            <div className="swiper-slide">
              <img
                width="667"
                height="557"
                className="mx-auto"
                src="images/signup-carousel-img-1.png"
                alt=""
              />
            </div>
          </div>
          <div className="pagination"></div>
        </div>
      );
  };

  export default SignUpBanner