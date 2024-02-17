import React from "react";

const SignUpForm = () => {
  return (
    <div className="row">
      {/* 왼쪽: 폼 요소 */}
      <div className="min-h-[1200px] bg-white py-10 lg:col-12 lg:py-[114px]">
        <div className="mx-auto w-full max-w-[750px]">
          <img className="mb-8" src="images/flower.svg" alt="" />
          <h1 className="mb-4">회원가입</h1>
          <p>Donec sollicitudin molestie malesda sollitudin</p>
          <div className="signin-options mt-10">
            <a
              className="btn btn-outline-white block w-full text-dark"
              href="#"
            >
              Sign Up With Google
            </a>
          </div>
          <div className="relative my-8 text-center after:absolute after:left-0 after:top-1/2 after:z-[0] after:w-full after:border-b after:border-border after:content-['']">
            <span className="relative z-[1] inline-block bg-white px-2">
              Or Sign Up With Email
            </span>
          </div>

          <form action="#">
            <div className="form-group">
              <label htmlFor="id" className="form-label">
                아이디
              </label>
              <input
                type="text"
                id="id"
                className="form-control"
                placeholder="아이디를 입력해주세요"
              />
            </div>
            <div className="form-group mt-4">
              <label htmlFor="email" className="form-label">
                이메일
              </label>
              <input
                type="email"
                id="email"
                className="form-control"
                placeholder="이메일을 입력해주세요"
              />
            </div>
            {/* <div className="form-group mt-4">
              <label className="form-label" htmlFor="gender">
                성별
              </label>
              <select
                name="gender"
                id="gender"
                className="form-select"
                required
              >
                <option value="">성별</option>
                <option value="investment plane">남성</option>
                <option value="investment plane-2">여성</option>
              </select>
            </div> */}
            <div className="form-group mt-4">
              <label htmlFor="password" className="form-label">
                패스워드
              </label>
              <input
                type="password"
                id="password"
                className="form-control"
                placeholder="비밀번호를 입력해주세요"
              />
            </div>
            <div className="form-group mt-4">
              <label htmlFor="passwordCheck" className="form-label">
                패스워드 체크
              </label>
              <input
                type="password"
                id="passwordCheck"
                className="form-control"
                placeholder="비밀번호 체크"
              />
            </div>

            <input
              className="btn btn-primary mt-10 block w-full"
              type="submit"
              value="회원가입"
            />
          </form>
        </div>
      </div>

      {/* 오른쪽: 이미지
      <div className="lg:col-6 bg-amber-200 py-10 lg:py-[114px]">
        <img
          className="mx-auto"
          src="images/signup-carousel-img-1.png"
          alt=""
        />
      </div> */}
    </div>
  );
};

export default SignUpForm;
