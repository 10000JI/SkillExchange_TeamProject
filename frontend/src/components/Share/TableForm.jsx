import { useMemo, useEffect, useState } from "react";

import {
  MaterialReactTable,
  useMaterialReactTable,
} from "material-react-table";
import { Box, Button, CircularProgress, TextField  } from "@mui/material";
import { Link } from "react-router-dom";
import axios from "axios";

const TableForm = () => {
  const [data, setData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [isLoading, setIsLoading] = useState(false);
  const [isRefetching, setIsRefetching] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [keyword, setKeyword] = useState("");
  const itemsPerPage = 10;

  useEffect(() => {
    // /v1/notices/list에서 데이터를 가져옴
    // axios
    //   .get(process.env.REACT_APP_SERVER + `/v1/notices/list`)
    //   .then((response) => {
    //     setData(response.data.content);
    //   })
    //   .catch((error) => {
    //     console.error("Error fetching data:", error);
    //   });
    fetchData();
  }, [currentPage, keyword]);

  const fetchData = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get(
        process.env.REACT_APP_SERVER + `/v1/notices/list`,
        {
          params: {
            limit: itemsPerPage,
            skip: currentPage - 1,
            keyword: keyword, // 추가: 키워드 전달,
          },
        }
      );
      setData(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("Error fetching data:", error);
    } finally {
      setIsLoading(false);
    }
  };
  const columns = useMemo(
    () => [
      {
        accessorKey: "id",
        header: "글번호",
        size: 40,
        // Cell: ({ cell }) => (
        //   <Button
        //     color="primary"
        //     component={Link}
        //     to={`/notice-detail/${cell.row.original.id}`}
        //     style={{ textDecoration: 'none', color: 'inherit',}}
        //   >
        //     <div className="clickable-cell">
        //       {cell.row.original.id}
        //     </div>
        //   </Button>
        // ),
      },
      {
        accessorKey: "writer",
        header: "작성자",
        size: 150,
        // Cell: ({ cell }) => (
        //   <Button
        //     color="primary"
        //     component={Link}
        //     to={`/notice-detail/${cell.row.original.id}`}
        //     style={{ textDecoration: "none", color: "inherit" }}
        //   >
        //     <div className="clickable-cell">{cell.row.original.writer}</div>
        //   </Button>
        // ),
      },
      {
        accessorKey: "title", //normal accessorKey
        header: "제목",
        size: 200,
        Cell: ({ cell }) => (
          <Button
            color="primary"
            component={Link}
            to={`/notice-detail/${cell.row.original.id}`}
            style={{ textDecoration: "none", color: "inherit" }}
          >
            <div className="clickable-cell">{cell.row.original.title}</div>
          </Button>
        ),
      },
      {
        accessorKey: "regdate",
        header: "작성일",
        size: 150,
        Cell: ({ cell }) => (
          <div className="clickable-cell">
            {new Date(cell.row.original.regDate).toLocaleString()}
          </div>
        ),
      },
      {
        accessorKey: "hit",
        header: "조회수",
        size: 40,
        Cell: ({ cell }) => (
          <Button
            color="primary"
            component={Link}
            to={`/notice-detail/${cell.row.original.id}`}
            style={{ textDecoration: "none", color: "inherit" }}
          >
            <div className="clickable-cell">{cell.row.original.hit}</div>
          </Button>
        ),
      },
    ],
    []
  );
  const table = useMaterialReactTable({
    columns,
    data,
    enableColumnFilters: false,
    enableColumnActions: false,
    enableSorting: false,
    enableHiding: false,
    enableDensityToggle: false,
    enableRowSelection: false,
    onColumnVisibilityChange: false,
    muiTableBodyCellProps: {
      sx: {
        paddingY: "70px",
      },
    },
    muiTableBodyRowProps: ({ row, staticRowIndex, table }) => ({
      component: Link,
      to: `/notice-detail/${row.original.id}`,
      sx: { cursor: "pointer", textDecoration: "none", color: "inherit" },
    }),

    muiTablePaperProps: {
      elevation: 0, // 쉐도우 제거
      sx: {
        // 추가적인 스타일링이 필요하다면 여기에 작성
      },
    },
    muiTableHeadCellProps: {
      align: "left",
    },
    muiTableBodyCellProps: {
      align: "left",
    },

    muiPaginationProps: {
      color: "primary",
      shape: "rounded",
      showRowsPerPage: false,
      variant: "outlined",
    },
    paginationDisplayMode: "pages",

    state: {
      isLoading: isLoading || isSaving, // cell skeletons and loading overlay
      showProgressBars: isRefetching, // progress bars while refetching
    },

    muiCircularProgressProps: {
      sx: { mt: 2 }, // 로딩 스피너 위치 조정
    },
    muiTableToolbarProps: {
      children: (
        <>
          <TextField
            label="검색어 입력"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            variant="standard"
            sx={{ mr: 2 }}
          />
          <Button onClick={() => fetchData()} variant="contained">
            검색
          </Button>
        </>
      ),
    },

    positionToolbarAlertBanner: "bottom", //show selected rows count on bottom toolbar
    //add custom action buttons to top-left of top toolbar
    // renderTopToolbarCustomActions: ({ table }) => (
    //   <Box sx={{ display: 'flex', gap: '1rem', p: '4px' }}>
    //     <Button
    //       color="primary"
    //       onClick={() => {
    //         alert('Create New Account');
    //       }}
    //       variant="contained"
    //     >
    //       글쓰기
    //     </Button>
    //   </Box>
    // ),
  });

  return (
    <div>
      <MaterialReactTable table={table} />
      {isLoading && (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
          <CircularProgress />
        </Box>
      )}
      <div>
        <Button
          onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
          disabled={currentPage === 1}
        >
          이전 페이지
        </Button>
        <span>현재 페이지: {currentPage}</span>
        <Button
          onClick={() =>
            setCurrentPage((prev) => Math.min(prev + 1, totalPages))
          }
          disabled={currentPage === totalPages || totalPages === 1}
        >
          다음 페이지
        </Button>
      </div>
    </div>
  );
};
export default TableForm;
