import React, { useState, useEffect } from "react";
import { AiOutlinePlusCircle } from "react-icons/ai";
import { Link, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const UpdateNotice = () => {
  const [files, setFiles] = useState([]);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [writer, setWriter] = useState("");
  const { noticeId } = useParams();
  const navigate = useNavigate();

  const { user } = useSelector((state) => state.user);

  const handleFileChange = (e) => {
    e.preventDefault();
    let files = Array.from(e.target.files);

    // MIME 타입이 이미지가 아닌 경우에 사용할 기본 이미지 경로
    const defaultImagePath =
      "https://image.zdnet.co.kr/2021/04/20/9c747492edd39cd3d885f3bc16bb0f1b.jpg";

    setFiles((prevImages) => [
      ...prevImages,
      ...files.map((file) => ({
        file,
        preview: file.type.startsWith("image/")
          ? URL.createObjectURL(file)
          : defaultImagePath,
      })),
    ]);
  };

  const handleImageRemove = (id) => {
    setFiles((prevImages) => prevImages.filter((image) => image.id !== id));
  };

  useEffect(() => {
    const fetchNoticeData = async () => {
      try {
        const response = await axios.get(
          process.env.REACT_APP_SERVER + `/v1/notices/${noticeId}`
        );
        const noticeData = response.data;

        setTitle(noticeData.title || "");
        setContent(noticeData.content || "");
        setWriter(noticeData.writer || ""); // writer 값 설정 추가

        // 이미지 URL 배열을 files 상태에 설정
        setFiles(
          noticeData.imgUrl.map((url, index) => ({
            file: null, // 이미지 파일을 가져오지 않기 때문에 null로 설정
            preview: url,
            id: index, // 각 이미지에 고유한 ID 설정
          }))
        );
      } catch (error) {
        console.error("데이터를 불러오는 중 에러 발생:", error);
      }
    };

    fetchNoticeData(); // 페이지 로드 시 데이터 불러오기

    if (user) {
      setWriter(user.id || "");
    }
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // FormData를 사용하여 이미지 및 필드 데이터를 모두 담음
      const accessToken = localStorage.getItem("accessToken");

      const noticeDto = {
        writer,
        title,
        content,
        imgUrl: files.map(file => file.preview),
      };

      console.log(writer, title, content);
      console.log(noticeDto)

      const formData = new FormData();
      const filesToUpload = files || [];
      console.log(filesToUpload);
      filesToUpload.forEach((file, index) => {
        formData.append("files", file.file);
      });

      formData.append(
        "noticeDto",
        new Blob([JSON.stringify(noticeDto)], {
          type: "application/json",
        })
      );

      // axios를 사용하여 서버로 데이터 업데이트를 위한 PATCH 요청
      const response = await axios.patch(
        process.env.REACT_APP_SERVER + `/v1/notices/${noticeId}`,
        formData,
        {
          withCredentials: true,
          headers: {
            Authorization: accessToken,
            "Content-Type": "multipart/form-data",
          },
        }
      );

      console.log(response);
      toast.success(response.data.returnMessage);
      navigate("/notice");
    } catch (error) {
      console.error("데이터를 업데이트하는 중 에러 발생:", error);
      toast.error(error.response.data.message);
    }
  };

  return (
    <>
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

      {/* blog single */}
      <section className="section blog-single">
        <div className="container">
          <div className="row justify-center">
            <div className="mt-4 max-w-[810px] lg:col-9">
              <div className="comments mb-6">
                <h3 className="h5 inline-block border-b-[3px] border-primary font-primary font-medium leading-8">
                  공지 수정
                </h3>
              </div>
              <form className="comment-form" onSubmit={handleSubmit}>
                <div className="form-group mt-8 md:col-12 lg:col-12">
                  <p className="mb-4">공지 제목</p>

                  <input
                    type="text"
                    name="title"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder=""
                  />
                </div>
                {/* <div className="row mb-8">
                  <div className="form-group mt-8 md:col-6 lg:col-4">
                    <input type="text" placeholder="Email" />
                  </div>
                  <div className="form-group mt-8 md:col-6 lg:col-4">
                    <input type="text" placeholder="Website" />
                  </div>
                </div> */}
                <p className="mb-4 mt-8">공지 내용</p>
                <div className="form-group mb-4">
                  <textarea
                    cols="30"
                    rows="10"
                    required
                    type="text"
                    name="content"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                  ></textarea>
                </div>

                <label className="pb-4">
                  이미지 업로드 <span className="text-red-500">*</span>
                </label>
                <input
                  type="file"
                  name=""
                  id="upload"
                  className="hidden"
                  multiple
                  onChange={handleFileChange}
                />
                <div className="flex w-full flex-wrap items-center">
                  <label htmlFor="upload">
                    <AiOutlinePlusCircle
                      size={30}
                      className="mt-3"
                      color="#555"
                    />
                  </label>
                  {files &&
                    files.map((file) => (
                      <div key={file.id} className="m-2 relative">
                        <img
                          src={file.preview}
                          alt=""
                          className="h-[120px] w-[120px] object-cover"
                        />
                        <button
                          type="button"
                          className="absolute top-0 right-0 p-1 bg-red-500 text-white rounded"
                          onClick={() => handleImageRemove(file.id)}
                        >
                          삭제
                        </button>
                      </div>
                    ))}
                </div>

                <input
                  type="submit"
                  className="btn btn-primary mt-8 min-w-[189px] cursor-pointer"
                  value="공지 수정"
                />
              </form>
            </div>
          </div>
        </div>
      </section>
      {/* ./end blog-single */}
    </>
  );
};

export default UpdateNotice;
