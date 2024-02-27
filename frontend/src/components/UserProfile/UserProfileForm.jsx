import React from "react";
import { IoHome } from "react-icons/io5";

const UserProfileForm = () => {
  return (
    <div className="min-h-3 ">
      <div className="flex flex-row pb-2">
        <div className="mr-3 w-3/12">
          <div className="mb-6 h-full rounded-xl bg-white px-6 py-4 shadow-lg">
          <div className="flex flex-col justify-between h-full">
          <div className="flex-grow">
            <div className="px-4 py-6 text-center border-b">
              <h1 className="text-xl font-bold leading-none"><span className="text-yellow-700">마이</span> 페이지</h1>
            </div>
            <div className="">
              <ul className="space-y-1">
                <li>
                  <a href="javascript:void(0)" className="flex items-center bg-white rounded-xl font-bold text-sm text-yellow-900 py-3 px-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" className="text-lg mr-4" viewBox="0 0 16 16">
                      <path d="M4 .5a.5.5 0 0 0-1 0V1H2a2 2 0 0 0-2 2v1h16V3a2 2 0 0 0-2-2h-1V.5a.5.5 0 0 0-1 0V1H4V.5zM16 14V5H0v9a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2zm-3.5-7h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5z"/>
                    </svg>마이페이지 홈 
                  </a>
                </li>
                <li>
                  <a href="javascript:void(0)" className="flex bg-white hover:bg-yellow-50 rounded-xl font-bold text-sm text-gray-900 py-3 px-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" className="text-lg mr-4" viewBox="0 0 16 16">
                      <path d="M12 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2zm-2 9a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"/>
                    </svg>내가 쓴 글
                  </a>
                </li>
                <li>
                  <a href="javascript:void(0)" className="flex bg-white hover:bg-yellow-50 rounded-xl font-bold text-sm text-gray-900 py-3 px-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" className="text-lg mr-4" viewBox="0 0 16 16">
                      <path d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a1.99 1.99 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3zm-8.322.12C1.72 3.042 1.95 3 2.19 3h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981l.006.139z"/>
                    </svg>내 정보 수정
                  </a>
                </li>
                <li>
                  <a href="javascript:void(0)" className="flex bg-white hover:bg-yellow-50 rounded-xl font-bold text-sm text-gray-900 py-3 px-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" className="text-lg mr-4" viewBox="0 0 16 16">
                      <path d="M2 1a1 1 0 0 0-1 1v4.586a1 1 0 0 0 .293.707l7 7a1 1 0 0 0 1.414 0l4.586-4.586a1 1 0 0 0 0-1.414l-7-7A1 1 0 0 0 6.586 1H2zm4 3.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z"/>
                    </svg>쪽지함
                  </a>
                </li>
              </ul>
            </div>
          </div>
          <div className="p-4">
            <button type="button" className="inline-flex items-center justify-center h-9 px-4 rounded-xl bg-gray-900 text-gray-300 hover:text-white text-sm font-semibold transition">
              <svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" className="" viewBox="0 0 16 16">
                <path d="M12 1a1 1 0 0 1 1 1v13h1.5a.5.5 0 0 1 0 1h-13a.5.5 0 0 1 0-1H3V2a1 1 0 0 1 1-1h8zm-2 9a1 1 0 1 0 0-2 1 1 0 0 0 0 2z"/>
              </svg>
            </button> <span className="font-bold text-sm ml-2">로그아웃</span>
          </div>
        </div>
          </div>
        </div>

        <div className="w-10/12">
          <div className="flex flex-row">
            <div className="mr-2 w-7/12 rounded-xl shadow-lg bg-white bg-no-repeat p-6">
              <p className="text-3xl text-black">
                환영합니다!
                <strong>김동현 님</strong>
              </p>
            </div>

            <div className=" w-5/12 rounded-xl shadow-lg bg-white bg-no-repeat p-6">
              <p className="text-5xl text-indigo-900">
                Inbox <br />
                <strong>23</strong>
              </p>
              <a
                href=""
                className="mt-12 inline-block rounded-full bg-orange-300 px-8 py-2 text-xl text-white underline hover:no-underline"
              >
                <strong>See messages</strong>
              </a>
            </div>
          </div>
          <div className="mt-2 flex h-64 flex-row">
            <div className="w-full rounded-xl bg-white px-6 py-4 shadow-lg">
              a
            </div>
            {/* <div className="bg-white rounded-xl shadow-lg mx-6 px-6 py-4 w-4/12">
              b
            </div>
            <div className="bg-white rounded-xl shadow-lg px-6 py-4 w-4/12">
              c
            </div> */}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfileForm;
