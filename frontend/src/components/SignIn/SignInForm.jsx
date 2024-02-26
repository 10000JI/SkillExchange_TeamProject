import React, { useState } from "react";
import Lottie from "lottie-react";
import axios from "axios";
import LOGIN from "../../lottie/Login.json";
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const SignInForm = () => {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [visible, setVisible] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("폼 데이터:", id, password);

    try {
      const response = await axios.post(
        process.env.REACT_APP_SERVER + `/v1/user/signIn`,
        { id, password },
        { withCredentials: true }
      );

      const accessToken = response.headers["authorization"];
      localStorage.setItem("accessToken", accessToken);

      toast.success("로그인 성공!");
      navigate("/");
      window.location.reload(true);
    } catch (err) {
      toast.error(err.response.data.message);
    }
  };

  return (
    <section className="">
      <div className="container max-w-full">
        <div className="row">
          <div className="min-h-[980px] bg-white py-10 lg:col-6 lg:py-[114px]">
            <div className="mx-auto w-full max-w-[650px]">
              <img className="mb-8" src="images/flower.svg" alt="" />
              <h1 className="mb-4">로그인</h1>
              <p>Donec sollicitudin molestie malesda sollitudin</p>
              <div className="signin-options mt-10">
                <a
                  className="btn btn-outline-white block w-full text-dark"
                  href="#"
                >
                  Sign In With Google
                </a>
              </div>
              <div className="relative my-8 text-center after:absolute after:left-0 after:top-1/2 after:z-[0] after:w-full after:border-b after:border-border after:content-['']">
                <span className="relative z-[1] inline-block bg-white px-2">
                  Or Sign In With Email
                </span>
              </div>

              <form onSubmit={handleSubmit}>
                <div className="form-group">
                  <label htmlFor="id" className="form-label">
                    아이디
                  </label>
                  <input
                    type="text"
                    id="id"
                    required
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                    className="form-control"
                    placeholder="아이디를 입력해주세요"
                  />
                </div>

                <div className="form-group relative mt-4">
                  <label htmlFor="password" className="form-label">
                    비밀번호
                  </label>
                  <div className="password-input-container relative">
                    <input
                      type={visible ? "text" : "password"} // 비밀번호 보이기 여부에 따라 타입 변경
                      id="password"
                      required
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      className="form-control"
                      placeholder="비밀번호를 입력해주세요"
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
                  </div>
                </div>
                <input
                  className="btn btn-primary mt-10 block w-full"
                  type="submit"
                  value="로그인"
                />
                <p className="mt-6 text-center">
                  아직 회원이 아니신가요?{" "}
                  <Link to="/sign-up" className="text-dark">
                    계정을 생성하세요!
                  </Link>
                </p>
                <p className="mt-6 text-center">
                  계정정보를 잊으셨나요?{" "}
                  <Link to="/findAccount" className="text-dark">
                    계정 찾기
                  </Link>
                </p>
              </form>
            </div>
          </div>

          <div className="auth-banner flex flex-col items-center justify-center bg-white py-16 lg:col-6">
            <div className="w-full text-center">
              <Lottie
                animationData={LOGIN}
                style={{ width: "1000px", height: "1000px" }}
                loop={true}
              />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default SignInForm;
