// configureStore는 Redux 스토어를 설정하는 함수로, rootReducer와 미들웨어 등을 포함하여 스토어를 구성합니다.
import { configureStore } from "@reduxjs/toolkit";

// userReducer는 user 슬라이스에 대한 리듀서입니다. 이 파일은 user 폴더 내의 userReducer.js 파일이어야 합니다.
import userReducer from "./user/userReducer";

// configureStore를 사용하여 Redux 스토어를 구성합니다.
const Store = configureStore({
  // reducer 옵션에는 루트 리듀서를 전달합니다.
  reducer: {
    user: userReducer, // user 슬라이스에 대한 리듀서를 등록합니다.
    // 다른 슬라이스의 리듀서를 추가할 수 있습니다.
  },
});

// 구성된 스토어를 내보냅니다.
export default Store;