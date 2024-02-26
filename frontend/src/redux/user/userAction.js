import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

// clearErrors 액션 생성자 함수 정의
export const clearErrors = createAsyncThunk("user/clearErrors", async () => {
  // 특별한 데이터가 필요하지 않을 경우, 임의의 값을 반환합니다.
  return null;
});

// loadUser 액션 생성자 함수 정의
export const loadUser = createAsyncThunk("user/loadUser", async () => {
  try {
    const accessToken = localStorage.getItem("accessToken");
    // axios를 사용하여 API 호출을 수행합니다.
    const { data } = await axios.get(
      process.env.REACT_APP_SERVER + `/v1/user/findId`,
      {
        headers: {
          Authorization: accessToken,
        },
      },
      
    );

    // API 호출이 성공하면 콘솔에 사용자 정보를 출력하고 해당 정보를 반환합니다.
    console.log(data.user);
    return data.user;
  } catch (error) {
    // API 호출 중에 오류가 발생하면 오류 응답 메시지를 throw하여 Redux에 전달합니다.
    throw error.response.data.message;
  }
});
