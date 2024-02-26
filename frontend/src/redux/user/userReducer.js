// createSlice는 Redux 슬라이스를 생성하는 함수로, 초기 상태와 각 액션에 대한 리듀서를 자동으로 생성합니다.
// userSlice는 'user'라는 이름의 Redux 슬라이스를 생성합니다.

// loadUser와 clearErrors 액션 생성자 함수를 임포트합니다. 이들은 userAction 파일에서 정의된 것이어야 합니다.
import { createSlice } from "@reduxjs/toolkit";
import { loadUser, clearErrors } from "./userAction";

// userSlice를 생성합니다.
const userSlice = createSlice({
  // 슬라이스의 이름을 정의합니다.
  name: 'user',
  // 초기 상태를 정의합니다.
  initialState: {
    isAuthenticated: false,
    loading: false,
    user: null,
    error: null,
  },
  // extraReducers를 사용하여 비동기 액션에 대한 추가적인 리듀서를 정의합니다.
  extraReducers: (builder) => {
    builder
      // loadUser 액션이 pending 상태일 때의 리듀서를 정의합니다.
      .addCase(loadUser.pending, (state) => {
        state.loading = true; // 로딩 상태를 true로 설정하여 로딩 중임을 나타냅니다.
      })
      // loadUser 액션이 fulfilled 상태일 때의 리듀서를 정의합니다.
      .addCase(loadUser.fulfilled, (state, action) => {
        state.isAuthenticated = true; // 인증 상태를 true로 설정합니다.
        state.loading = false; // 로딩 상태를 false로 설정하여 로딩이 완료되었음을 나타냅니다.
        state.user = action.payload; // API에서 받아온 사용자 정보를 저장합니다.
      })
      // loadUser 액션이 rejected 상태일 때의 리듀서를 정의합니다.
      .addCase(loadUser.rejected, (state, action) => {
        state.loading = false; // 로딩 상태를 false로 설정하여 로딩이 완료되었음을 나타냅니다.
        state.error = action.error.message; // 오류 메시지를 저장합니다.
        state.isAuthenticated = false; // 인증 상태를 false로 설정합니다.
      })
      // clearErrors 액션이 fulfilled 상태일 때의 리듀서를 정의합니다.
      .addCase(clearErrors.fulfilled, (state) => {
        state.error = null; // 오류 상태를 초기화하여 오류가 없음을 나타냅니다.
      });
  },
});

// 생성된 userSlice의 리듀서를 내보냅니다.
export default userSlice.reducer;