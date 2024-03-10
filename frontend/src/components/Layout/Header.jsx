import React from "react";
import {  useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { clearUser } from "../../redux/user/userAction";

const Header = () => {
  const dispatch = useDispatch();
  const { isAuthenticated, user } = useSelector((state) => state.user);

  const handleLogout = () => {
    // 로그아웃 버튼 클릭 시 실행되는 로직
    // 여기에서 로컬 스토리지의 accessToken을 지우고 Redux state를 초기화하는 작업을 수행
    localStorage.removeItem("accessToken");
    dispatch(clearUser()); // Redux action을 사용하여 사용자 정보 초기화
    // 페이지 리로드
    window.location.replace('/');
  };

  return (
    <header className="header">
      <nav className="navbar container">
        {/* logo */}
        <div className="order-0">
          <Link to="/">
            <img src="images/logo.svg" height="30" width="147" alt="logo" />
          </Link>
        </div>
        {/* navbar toggler */}
        <input id="nav-toggle" type="checkbox" className="hidden" />
        <label
          id="show-button"
          htmlFor="nav-toggle"
          className="order-1 flex cursor-pointer items-center lg:order-1 lg:hidden"
        >
          <svg className="h-6 fill-current" viewBox="0 0 20 20">
            <title>Menu Open</title>
            <path d="M0 3h20v2H0V3z m0 6h20v2H0V9z m0 6h20v2H0V0z"></path>
          </svg>
        </label>
        <label
          id="hide-button"
          htmlFor="nav-toggle"
          className="order-2 hidden cursor-pointer items-center lg:order-1"
        >
          <svg className="h-6 fill-current" viewBox="0 0 20 20">
            <title>Menu Close</title>
            <polygon
              points="11 9 22 9 22 11 11 11 11 22 9 22 9 11 -2 11 -2 9 9 9 9 -2 11 -2"
              transform="rotate(45 10 10)"
            ></polygon>
          </svg>
        </label>
        {/* /navbar toggler */}
        <ul
          id="nav-menu"
          className="navbar-nav order-2 hidden w-full flex-[0_0_100%] lg:order-1 lg:flex lg:w-auto lg:flex-auto lg:justify-center lg:space-x-5"
        >
          <li className="nav-item">
            <Link to="/" className="nav-link active">
              메인페이지
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/notice" className="nav-link">
              공지사항
            </Link>
          </li>
          <li className="nav-item">
            <a href="blog.html" className="nav-link">
              재능등록
            </a>
          </li>
          <li className="nav-item">
            <a href="features.html" className="nav-link">
              자유게시판
            </a>
          </li>
          <li className="nav-item">
            <a href="how-it-works.html" className="nav-link">
              모집해요
            </a>
          </li>
          <li className="nav-item">
            <a href="contact.html" className="nav-link">
              자주 묻는 질문
            </a>
          </li>
          <li className="nav-item mt-3.5 lg:hidden">
            <a
              className="btn btn-white btn-sm border-border"
              href="signin.html"
            >
              Sign Up Now
            </a>
          </li>
        </ul>

        <div className="order-1 ml-auto hidden items-center md:order-2 md:ml-0 lg:flex">
          {isAuthenticated ? (
            <>
              <Link to="/profile" className="btn btn-primary btn-sm mr-5 font-bold">
                마이페이지
              </Link>
            
              <button
              className="btn btn-primary btn-sm font-bold"
              onClick={handleLogout} // 로그아웃 클릭 시 handleLogout 함수 실행
            >
              로그아웃
            </button>
            </>
          ) : (
            <Link to="/sign-in" className="btn btn-primary btn-sm font-bold">
              로그인
            </Link>
          )}
        </div>
      </nav>
    </header>
  );
};

export default Header;
