import React, { useState } from "react";
import Lottie from "lottie-react";
import axios from "axios";
// import LOGIN from "../../lottie/Login.json";
import FIND from "../../lottie/FIND.json";
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const FindForm = () => {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [visible, setVisible] = useState(false);
  const [activeTab, setActiveTab] = useState(0);
  const navigate = useNavigate();
  const ChangeTab = (tab) => {
    if (activeTab === tab) {
      setActiveTab(0);
    } else {
      setActiveTab(tab);
    }
  };

  const findIdHandleSubmit = async (e) => {
    e.preventDefault();
    console.log("아이디 찾기 데이터", id)
    const email = id
    try {
        const response = await axios.post(
            process.env.REACT_APP_SERVER + `/v1/user/emailToFindId`, {email} ,{ withCredentials: true }
        );
        toast.success(response.data.returnMessage);
        console.log(id)
    } catch (err) {
        toast.error(err.response.data.message);
    }
  }

  const findPasswordHandleSubmit = async (e) => {
    e.preventDefault();
    console.log("비밀번호 찾기 데이터", password)
    const email = password
    try {
        const response = await axios.post(
            process.env.REACT_APP_SERVER + `/v1/user/emailToFindPw`,{email}
        );
        toast.success(response.data.returnMessage);
    } catch (err) {
        toast.error(err.response.data.message);
    }
  }

  return (
    <section className="">
      <div className="container max-w-full">
        <div className="row">
          <div className="min-h-[980px] bg-white py-10 lg:col-6 lg:py-[114px] flex items-center justify-center">
            <div className="mx-auto w-full max-w-[650px]">
              <div className="mb-8 flex items-center justify-between">
                <h1 className="mb-4">회원 정보 찾기</h1>
                <button
                  className="btn btn-primary"
                  onClick={() => ChangeTab(activeTab === 0 ? 1 : 0)}
                >
                  {activeTab === 1 ? "아이디 찾기" : "비밀번호 찾기"}
                </button>
              </div>
              {activeTab === 0 && (
                <div className="mt-9 rounded-xl p-10 ">
                  <div className="relative my-3 text-center after:absolute after:left-0 after:top-1/2 after:z-[0] after:w-full after:border-b after:border-border after:content-['']">
                    <span className="relative z-[1] inline-block rounded-full bg-white px-4 py-1 text-lg font-bold">
                      아이디 찾기
                    </span>
                  </div>

                  <div className="text-center">
                    <p className="mb-6">
                      가입하신 이메일 주소를 입력하시면,<br></br>
                      해당 이메일 주소로 <strong>"아이디"</strong> 를 전송해
                      드립니다.
                    </p>
                  </div>

                  <form onSubmit={findIdHandleSubmit}>
                    <div className="form-group">
                      <label htmlFor="id" className="form-label"></label>
                      <input
                        type="email"
                        id="id"
                        required
                        value={id}
                        onChange={(e) => setId(e.target.value)}
                        className="form-control"
                        placeholder="이메일을 입력해주세요"
                      />
                    </div>

                    <input
                      className="btn btn-primary mt-10 block w-full"
                      type="submit"
                      value="이메일 발송"
                    />
                  </form>
                </div>
              )}
              {activeTab === 1 && (
                <div className="mt-9 rounded-xl p-10">
                  <div className="relative my-3 text-center after:absolute after:left-0 after:top-1/2 after:z-[0] after:w-full after:border-b after:border-border after:content-['']">
                    <span className="relative z-[1] inline-block rounded-full bg-white px-4 py-1 text-lg font-bold">
                      비밀번호 찾기
                    </span>
                  </div>

                  <div className="text-center">
                    <p className="mb-6">
                      가입하신 이메일 주소를 입력하시면,
                      <br />
                      해당 이메일 주소로 <strong>"임시 비밀번호"</strong> 를
                      전송해 드립니다.
                    </p>
                  </div>

                  <form onSubmit={findPasswordHandleSubmit}>
                    <div className="form-group">
                      <label htmlFor="email" className="form-label"></label>
                      <input
                        type="email"
                        id="email"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="form-control"
                        placeholder="이메일을 입력해주세요"
                      />
                    </div>

                    <input
                      className="btn btn-primary mt-10 block w-full"
                      type="submit"
                      value="이메일 발송"
                    />
                  </form>
                </div>
              )}
            </div>
          </div>

          <div className="auth-banner flex flex-col items-center justify-center bg-white py-16 lg:col-6">
            <div className="w-full text-center">
              <Lottie
                animationData={FIND}
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

export default FindForm;
