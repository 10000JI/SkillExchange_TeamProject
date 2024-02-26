import React from "react";

const UserProfileHeader = () => {
  return (
    <>
      <header className="fixed left-60 right-0 top-0 h-16 bg-yellow-50 px-4 py-3">
        <div className="mx-auto max-w-4xl">
          <div className="flex items-center justify-between">
            <div>
              <button
                type="button"
                className="flex items-center rounded-lg border border-transparent p-2 font-semibold text-gray-600 transition hover:border-yellow-300 hover:text-yellow-600 focus:border-yellow-300 focus:text-yellow-600 focus:outline-none"
              >
                <span className="mr-2 inline-flex h-6 w-6 items-center justify-center rounded bg-white text-xs text-gray-600 transition">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="1em"
                    height="1em"
                    fill="currentColor"
                    className="bi bi-chevron-left"
                    viewBox="0 0 16 16"
                  >
                    <path
                      fillRule="evenodd"
                      d="M11.354 1.646a.5.5 0 0 1 0 .708L5.707 8l5.647 5.646a.5.5 0 0 1-.708.708l-6-6a.5.5 0 0 1 0-.708l6-6a.5.5 0 0 1 .708 0z"
                    />
                  </svg>
                </span>
                <span className="text-sm">Archive</span>
              </button>
            </div>
            <div className="text-lg font-bold">Today's Plan</div>
            <div>
              <button
                type="button"
                className="flex items-center rounded-lg border border-transparent p-2 font-semibold text-gray-600 transition hover:border-yellow-300 hover:text-yellow-600 focus:border-yellow-300 focus:text-yellow-600 focus:outline-none"
              >
                <span className="text-sm">This week</span>
                <span className="ml-2 inline-flex h-6 w-6 items-center justify-center rounded bg-white text-xs text-gray-600 transition">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="1em"
                    height="1em"
                    fill="currentColor"
                    className="bi bi-chevron-right"
                    viewBox="0 0 16 16"
                  >
                    <path
                      fillRule="evenodd"
                      d="M4.646 1.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1 0 .708l-6 6a.5.5 0 0 1-.708-.708L10.293 8 4.646 2.354a.5.5 0 0 1 0-.708z"
                    />
                  </svg>
                </span>
              </button>
            </div>
          </div>
        </div>
      </header>
    </>
  );
};

export default UserProfileHeader;
