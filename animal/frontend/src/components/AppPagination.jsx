import React from "react";
import ReactPaginate from "react-paginate";

const AppPagination = ({ info, pageNumber, setPageNumber }) => {
    return (
        <>
            <ReactPaginate
                className="w-100 pagination justify-content-center gap-4 my-4"
                nextLabel="Next"
                previousLabel="Previous"
                previousLinkClassName="btn btn-dark"
                nextLinkClassName="btn btn-dark"
                pageClassName="page-item"
                pageLinkClassName="page-link"
                activeClassName='active'
                pageCount={info?.totalPages}
                style={{ backgroundColor: 'purple', color: 'purple' }}
                onPageChange={(data) => {
                    setPageNumber(data?.selected)
                }}
            />
        </>
    );
};

export default AppPagination;