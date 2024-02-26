import React, { useState } from "react";
import axios from "axios";
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai";
import { toast } from "react-toastify";
import { useForm } from "react-hook-form";


const SignUpForm = () => {
  const [visible, setVisible] = useState(false);
  const [signUp, setSignUp] = useState(false);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({ mode: "onChange" });

  const handleRegistration = async (e) => {
    try {
      const formData = {
        id: e.id,
        email: e.email,
        password: e.password,
        passwordCheck: e.passwordCheck,
      };
      const response = await axios.post(
        process.env.REACT_APP_SERVER + `/v1/user/signUp`,
        formData
      );
      setSignUp = true;
    
      console.log("서버응답", response.data);
      toast.success(response.data.returnMessage);
      // window.location.replace("/");
    } catch (error) {
      console.error("응답에러", error);
  
      // 서버 응답에 따라 다르게 처리
      if (error.response) {
        // 서버 응답이 있는 경우 (예: 4xx, 5xx 에러)
        console.error("서버 응답 데이터:", error.response.data);
  
        // 서버에서 온 에러 메시지 출력
        toast.error(error.response.data.details[0]);
  
        // 서버에서 온 상세 내용 출력
        if (error.response.data.details) {
          console.error("상세 내용:", error.response.data.details);
        }
      } else if (error.request) {
        // 요청이 전혀 이루어지지 않은 경우
        console.error("요청 오류:", error.request);
      } else {
        // 오류를 발생시킨 요청을 설정하는 중에 문제가 발생한 경우
        console.error("오류 메시지:", error.message);
      }
    }
  };

  const handleError = (errors) => {};

  const registerOptions = {
    id: {
      required: "id는 필수 정보입니다.",
      minLength: {
        value: 5,
        message: "id는 5글자 이상 입력해 주세요.",
      },
    },
    email: {
      required: "이메일은 필수 정보입니다.",
      pattern: {
        value: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,4}$/,
        message: "이메일 형식이 올바르지 않습니다.",
      },
    },
    password: {
      required: "비밀번호는 필수 정보입니다.",
      pattern: {
        value:
          /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,16}$/,
        message: "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.",
      },
    },
    passwordCheck: {
      validate: (value, { password }) =>
        value === password || "비밀번호가 일치하지 않습니다.",
    },
  };

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

          <form onSubmit={handleSubmit(handleRegistration, handleError)}>
            {/* ID 입력 필드 */}
            <div className="form-group">
              <label htmlFor="id" className="form-label">
                아이디
              </label>
              <input
                type="text"
                id="id"
                name="id"
                className="form-control"
                placeholder="아이디를 입력해주세요"
                {...register("id", registerOptions.id)}
              />
              <p className="text-danger mt-3" style={{ color: "red" }}>
                {errors?.id && errors.id.message}{" "}
              </p>
            </div>

            {/* 이메일 입력 필드 */}
            <div className="form-group mt-4">
              <label htmlFor="email" className="form-label">
                이메일
              </label>
              <input
                type="email"
                id="email"
                name="email"
                className="form-control"
                placeholder="이메일을 입력해주세요"
                {...register("email", registerOptions.email)}
              />
              <p className="text-danger mt-3" style={{ color: "red" }}>
                {errors?.email && errors.email.message}
              </p>
            </div>

            {/* 비밀번호 입력 필드 */}
            <div className="form-group mt-4">
              <label htmlFor="password" className="form-label">
                비밀번호
              </label>
              <div className="password-input-container relative">
                <input
                  type={visible ? "text" : "password"}
                  id="password"
                  name="password"
                  className="form-control"
                  placeholder="비밀번호를 입력해주세요"
                  {...register("password", registerOptions.password)}
                />
                {visible ? (
                  <AiOutlineEye
                    className="absolute right-3 top-3 cursor-pointer"
                    size={35}
                    onClick={() => setVisible(false)}
                  />
                ) : (
                  <AiOutlineEyeInvisible
                    className="absolute right-3 top-3 cursor-pointer"
                    size={35}
                    onClick={() => setVisible(true)}
                  />
                )}
                <p className="text-danger mt-3" style={{ color: "red" }}>
                  {errors?.password && errors.password.message}
                </p>
              </div>
            </div>

            {/* 비밀번호 확인 입력 필드 */}
            <div className="form-group mt-4">
              <label htmlFor="passwordCheck" className="form-label">
                패스워드 체크
              </label>
              <input
                type={visible ? "text" : "password"}
                id="passwordCheck"
                name="passwordCheck"
                className="form-control"
                placeholder="비밀번호 체크"
                {...register("passwordCheck", registerOptions.passwordCheck)}
              />
              <p className="text-danger mt-3" style={{ color: "red" }}>
                {errors?.passwordCheck && errors.passwordCheck.message}
              </p>
            </div>

            {/* 제출 버튼 */}
            <input
              className="btn btn-primary mt-10 block w-full"
              type="submit"
              value={signUp ? "이메일 재발송" : "회원가입"}
            />
          </form>
        </div>
      </div>
    </div>
  );
};

export default SignUpForm;
