import axios from "axios";
import Lottie from "lottie-react";
import FAIL from "../lottie/FAIL.json";
import SUCCESS from "../lottie/SUCCESS.json";
import React, { useEffect } from "react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { set } from "react-hook-form";

const ActivationPage = () => {
  const { activeToken } = useParams();
  const [error, setError] = useState(true);


  useEffect(() => {
    const sendRequest = async () => {
      try {
        const response = await axios.post(
            process.env.REACT_APP_SERVER + `/v1/user/activation`,
          {
            activeToken,
          }
        );
        console.log("서버응답", response);
        setError(false)
      } catch (err) {
        setError(true);
      }
    };

    if (activeToken) {
      sendRequest();
    }
  }, [activeToken]);

  return (
    // 화면에 메시지 출력
    <div
      style={{
        width: "100%",
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
      }}
    >
      {error ? (
        <>
          <div className="auth-banner flex flex-col items-center justify-center">
            <div className="w-full text-center">
              <h1>인증에 실패하였습니다!</h1>
              <Lottie
                animationData={FAIL}
                style={{ width: "1000px", height: "1000px" }}
                loop={false}
              />
            </div>
          </div>
        </>
      ) : (
        <>
          <div className="auth-banner flex flex-col items-center justify-center">
            <div className="w-full text-center">
              <h1>계정이 활성화되었습니다!</h1>
              <Lottie
                animationData={SUCCESS}
                style={{ width: "1000px", height: "1000px" }}
                loop={false}
              />
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default ActivationPage;
