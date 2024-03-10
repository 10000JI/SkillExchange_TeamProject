import React, { useState,useEffect } from "react";
import { AiOutlinePlusCircle } from "react-icons/ai";
import { useSelector } from "react-redux";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const CreateNotice = () => {
  const [files, setFiles] = useState([]);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [writer, setWriter] = useState("")
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

  useEffect(() => {
    if (user) {
      // user가 정의되어 있을 때에만 초기값 업데이트
      setWriter(user.id || "");
    //   setAvatar(user.imgUrl || "");
    }
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const accessToken = localStorage.getItem("accessToken");

      const noticeDto = {
        writer,
        title,
        content,
      };

      console.log(writer, title, content)

      // FormData를 사용하여 이미지 및 필드 데이터를 모두 담음
      const formData = new FormData();

      const filesToUpload = files || [];
      console.log(filesToUpload)
      filesToUpload.forEach((file, index) => {
        // 파일 필드의 이름을 설정 (filename 속성 추가)
        formData.append(`files`, file.file, file.file.name);
    });

      // 나머지 데이터를 JSON 문자열로 변환하여 FormData에 추가
      formData.append(
        "noticeDto",
        new Blob([JSON.stringify(noticeDto)], {
          type: "application/json",
        })
      );

      // axios를 사용하여 서버로 데이터 전송
      const response = await axios.post(
        process.env.REACT_APP_SERVER + `/v1/notices/register`,
        formData,
        {
            withCredentials: true,
            headers: {
                Authorization: accessToken,
                "Content-Type": "multipart/form-data",
            },
        }
      );

   // 성공 시 서버 응답을 출력
      console.log(response)
      toast.success(response.data.returnMessage);
      navigate('/notice');
    } catch (error) {
      console.log(error)
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
                  공지 등록
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
                      <img
                        src={file.preview}
                        key={file.file.name}
                        alt=""
                        className="m-2 h-[120px] w-[120px] object-cover"
                      />
                    ))}
                </div>

                <input
                  type="submit"
                  className="btn btn-primary mt-8 min-w-[189px] cursor-pointer"
                  value="공지 등록"
                />
              </form>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default CreateNotice;
