import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useSelector } from "react-redux";

const UserProfile = () => {
  const [job, setJob] = useState("");
  const [preferredSubject, setPreferredSubject] = useState("");
  const [mySubject, setMySubject] = useState("");
  const [gender, setGender] = useState("");
  const [careerSkills, setCareerSkills] = useState("");
  const [avatar, setAvatar] = useState("");
  const fileInputRef = useRef(null);

  const { user } = useSelector((state) => state.user);

  const handleButtonClick = () => {
    fileInputRef.current.click();
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    switch (name) {
      case "job":
        setJob(value);
        break;
      case "preferredSubject":
        setPreferredSubject(value);
        break;
      case "mySubject":
        setMySubject(value);
        break;
      case "gender":
        setGender(value);
        break;
      case "careerSkills":
        setCareerSkills(value);
        break;
      default:
        break;
    }
  };

  useEffect(() => {
    if (user) {
      // user가 정의되어 있을 때에만 초기값 업데이트
      setJob(user.job || "");
      setPreferredSubject(user.preferredSubject || "");
      setMySubject(user.mySubject || "");
      setGender(user.gender || "");
      setCareerSkills(user.careerSkills || "");
      setAvatar(user.imgUrl || "");
    }
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(
      "폼 데이터:",
      job,
      preferredSubject,
      mySubject,
      gender,
      careerSkills,
      avatar
    );

    try {
      const accessToken = localStorage.getItem("accessToken");

      // avatar를 제외한 나머지 데이터를 JSON으로 변환
      const profileData = {
        job,
        preferredSubject,
        mySubject,
        gender,
        careerSkills,
      };

      // FormData 인스턴스 생성
      const formData = new FormData();
      formData.append("imgFile", avatar);

      // 나머지 데이터를 JSON 문자열로 변환하여 FormData에 추가
      formData.append(
        "profileDto",
        new Blob([JSON.stringify(profileData)], {
          type: "application/json",
        })
      );

      const response = await axios.patch(
        process.env.REACT_APP_SERVER + `/v1/user/profileUpdate`,
        formData,
        {
          withCredentials: true,
          headers: {
            Authorization: accessToken,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      toast.success(response.data.returnMessage);
      window.location.reload()
    } catch (err) {
      toast.error(err.response.data.message);
    }
  };

  // 파일 입력이 변경될 때 실행되는 함수
  const handlerFileInputChange = (e) => {
    const file = e.target.files[0];
    setAvatar(file);
  };

  return (
    <>
      <div className="p-4">
        <div className="mt-24 bg-gray-50 p-8 shadow">
          <div className="grid grid-cols-1 md:grid-cols-3">
            <div className="order-last mt-20 grid grid-cols-3 text-center md:order-first md:mt-0">
              <div>
                <p className="text-xl font-bold text-gray-700">22</p>
                <p className="text-gray-400">Friends</p>
              </div>
              <div>
                <p className="text-xl font-bold text-gray-700">10</p>
                <p className="text-gray-400">Photos</p>
              </div>
              <div>
                <p className="text-xl font-bold text-gray-700">89</p>
                <p className="text-gray-400">Comments</p>
              </div>
            </div>
            <div className="relative">
              <div className="absolute inset-x-0 top-0 mx-auto -mt-24 flex h-32 w-32 items-center justify-center rounded-full bg-indigo-100 text-indigo-500 shadow-2xl">
                {/* 이미지 미리보기 */}
                {avatar && avatar instanceof File ? (
                  <img
                    src={URL.createObjectURL(avatar)}
                    alt="Avatar Preview"
                    className="h-24 w-24 rounded-full object-cover"
                  />
                ) : user && user.imgUrl ? (
                  <img
                    src={user.imgUrl}
                    alt="Avatar Preview"
                    className="h-24 w-24 rounded-full object-cover"
                  />
                ) : (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-24 w-24"
                    viewBox="0 0 20 20"
                    fill="currentColor"
                  >
                    <path
                      fillRule="evenodd"
                      d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
                      clipRule="evenodd"
                    />
                  </svg>
                )}
              </div>
            </div>

            <div className="mt-32 flex justify-between space-x-8 md:mt-0 md:justify-center">
              <button className="btn btn-primary transform rounded px-4 py-2  font-medium uppercase shadow transition hover:-translate-y-0.5 hover:shadow-lg">
                비밀번호 수정
              </button>
              <button
                className="btn btn-primary transform rounded px-4 py-2  font-medium uppercase shadow transition hover:-translate-y-0.5 hover:shadow-lg"
                onClick={handleButtonClick}
              >
                프로필 이미지 수정
              </button>
            </div>
          </div>

          <div className="mt-12 border-b pb-12 text-center">
            <div className="col-6 justify-center">
              <HomePage />
            </div>
          </div>

          <div className="mt-12 flex flex-col justify-center"></div>
          {/* floating assets */}
          <img
            className="floating-bubble-1 absolute right-0 top-0 -z-[1]"
            src="images/floating-bubble-1.svg"
            alt=""
          />
          <img
            className="floating-bubble-2 absolute left-0 top-[387px] -z-[1]"
            src="images/floating-bubble-2.svg"
            alt=""
          />
          <img
            className="floating-bubble-3 absolute right-0 top-[605px] -z-[1]"
            src="images/floating-bubble-3.svg"
            alt=""
          />
          {/* ./end floating assets */}

          <section className="section pt-0">
            <div className="container">
              <div className="row">
                <div className="mb-10 text-center md:col-6 md:order-2 md:mb-0 md:pt-9">
                  <div className="contact-img relative inline-flex pb-5 pl-5">
                    <img src="images/contact-img.png" alt="" />
                    <img
                      className="absolute bottom-0 left-0 -z-[1] h-14 w-14"
                      src="images/shape-2.svg"
                      alt=""
                    />
                  </div>
                </div>
                <div className="md:col-6 md:order-1">
                  <form className="lg:max-w-[484px]" onSubmit={handleSubmit}>
                    {/* 직업 */}
                    <div className="form-group mb-5">
                      <label className="form-label" htmlFor="job">
                        직업
                      </label>
                      <input
                        className="form-control"
                        type="text"
                        id="job"
                        name="job"
                        value={job}
                        onChange={handleChange}
                        placeholder="직업을 입력해주세요"
                      />
                    </div>
                    {/* 관심사 */}
                    <div className="form-group mb-5">
                      <label className="form-label" htmlFor="preferredSubject">
                        관심 기술
                      </label>
                      <input
                        className="form-control"
                        type="text"
                        id="preferredSubject"
                        name="preferredSubject"
                        value={preferredSubject}
                        onChange={handleChange}
                        placeholder="관심있는 기술을 입력해주세요"
                      />
                    </div>
                    {/* 보유스킬 */}
                    <div className="form-group mb-5">
                      <label className="form-label" htmlFor="mySubject">
                        보유 기슬
                      </label>
                      <input
                        className="form-control"
                        type="text"
                        id="mySubject"
                        name="mySubject"
                        value={mySubject}
                        onChange={handleChange}
                        placeholder="보유하신 기술을 입력해주세요"
                      />
                    </div>
                    {/* 파일 입력란 */}
                    <div className="form-group mb-5">
                      <input
                        type="file"
                        id="avatar"
                        name="avatar"
                        style={{ display: "none" }} // 파일 입력란을 숨깁니다.
                        accept=".jpg, .jpg, .png"
                        onChange={handlerFileInputChange}
                        ref={fileInputRef}
                      />
                    </div>
                    {/* 성별 */}
                    <div className="form-group mb-5">
                      <label className="form-label" htmlFor="gender">
                        성별
                      </label>
                      <select
                        name="gender"
                        id="gender"
                        className="form-select"
                        value={gender}
                        onChange={handleChange}
                        required
                      >
                        <option value="">성별을 입력해주세요</option>
                        <option value="남">남</option>
                        <option value="여">여</option>
                      </select>
                    </div>
                    {/* 자기소개 */}
                    <div className="form-group mb-5">
                      <label className="form-label" htmlFor="careerSkills">
                        자기소개
                      </label>
                      <textarea
                        className="form-control h-[150px]"
                        id="careerSkills"
                        name="careerSkills"
                        cols="30"
                        rows="10"
                        value={careerSkills}
                        onChange={handleChange}
                        placeholder="자신의 커리어 소개와 보유하신 기술에 대해서 설명해주세요"
                      ></textarea>
                    </div>
                    {/* Submit Button */}
                    <input
                      className="btn btn-primary block w-full"
                      type="submit"
                      value="회원정보 수정"
                    />
                  </form>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
    </>
  );
};

export default UserProfile;

const ContactPage = () => {
  return <></>;
};

const HomePage = () => {
  return (
    <div>
      <div className="grid grid-cols-3 gap-5">
        <button className="btn btn-outline-primary flex items-center justify-center  rounded p-4 text-red-500 shadow-md ">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="mr-2 h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
            />
          </svg>
          활동 정보
        </button>
        <button className="btn btn-outline-primary flex items-center justify-center  rounded p-4 text-red-500 shadow-md">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="mr-2 h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M5.121 17.804A13.937 13.937 0 0112 16c2.5 0 4.847.655 6.879 1.804M15 10a3 3 0 11-6 0 3 3 0 016 0zm6 2a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          회원 정보
        </button>
        <button className="btn btn-outline-primary flex items-center justify-center rounded p-4 text-red-500 shadow-md">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="mr-2 h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M17 8h2a2 2 0 012 2v6a2 2 0 01-2 2h-2v4l-4-4H9a1.994 1.994 0 01-1.414-.586m0 0L11 14h4a2 2 0 002-2V6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2v4l.586-.586z"
            />
          </svg>
          채팅 or 쪽지
        </button>
      </div>
    </div>
  );
};
