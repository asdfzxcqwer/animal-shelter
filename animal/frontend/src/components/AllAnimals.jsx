import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Col, Row } from 'react-bootstrap';
import Select from 'react-select';
import AppPagination from '../components/AppPagination';
import AnimalRow from '../components/AnimalRow';
import LoginPlaceholder from '../components/LoginPlaceHolder';

const Animals = () => {
    const [animals, setAnimals] = useState([])
    const [info, setInfo] = useState([])
    const [recordsPerPage, setRecordsPerPage] = useState(10)
    const [pageNumber, setPageNumber] = useState(0)

    const [sort, setSort] = useState("dateOfBirth,asc")



    const pageRecords = [
        { value: 10, label: 10 },
        { value: 20, label: 20 },
        { value: 30, label: 30 }
    ]


    const fetchData = async () => {
        axios.get(`http://localhost:8085/api/v1/animals?status=AVAILABLE&page=${pageNumber}&size=${recordsPerPage}&sort=${sort}`, {
            headers: {
                authorization: localStorage.getItem('authorization')
            }
        }).then(res => {
            setAnimals(res.data.content)
            setInfo(res.data)
        })
    }
    const [user, setUser] = useState(null)
    if (localStorage.getItem("email") != null && user == null) {
        setUser(localStorage.getItem("email"))
    }

    useEffect(() => {
        fetchData()
    }, [pageNumber, recordsPerPage, sort])

    return (
        <>
            {user ? (
                <div class="d-flex justify-content-center" style={{ minHeight: "75vh", backgroundColor: " #937147", textAlign: 'center' }}>
                    <div style={{ overflowX: "auto", minHeight: "75vh", backgroundColor: " #937147", textAlign: 'center' }} className='container ' >
                        <div className='py-4'>
                            <div className="table-title">
                                <div className="row mb-1">
                                    <div className="text-center" style={{ color: 'white' }}>
                                        <h2><b>ANIMALS</b></h2>
                                    </div>
                                </div>
                            </div>
                            <Row>

                                <Col className='col-12' >
                                    <Select
                                        className='mb-3'
                                        onChange={(e) => setRecordsPerPage(e.label)}
                                        options={pageRecords}
                                        placeholder={"Records per page"}
                                    />
                                </Col>
                            </Row>
                            <table className="table">
                                <thead className="btn-dark" >
                                    <tr>
                                        <th scope="col" style={{ color: 'white' }} onClick={sort === "animalType,asc" ? (e) => setSort("animalType,desc") : (e) => setSort("animalType,asc")} >ANIMAL TYPE</th>
                                        <th scope="col" style={{ color: 'white' }} onClick={sort === "breed,asc" ? (e) => setSort("breed,desc") : (e) => setSort("breed,asc")} >BREED</th>
                                        <th scope="col" style={{ color: 'white' }} onClick={sort === "price.amount,desc" ? (e) => setSort("price.amount,asc") : (e) => setSort("price.amount,desc")}>PRICE</th>
                                        <th scope="col" style={{ color: 'white' }} onClick={sort === "dateOfBirth,desc" ? (e) => setSort("dateOfBirth,asc") : (e) => setSort("dateOfBirth,desc")}>DATE OF BIRTH</th>
                                        <th scope="col">BUY</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {animals.map((animal, index) => (
                                        <AnimalRow fetchData={fetchData} key={index} animal={animal} />
                                    ))}
                                </tbody>
                            </table>
                            <AppPagination pageNumber={pageNumber} setPageNumber={setPageNumber} info={info} />
                        </div>
                    </div>
                </div>
            ) : (
                <div class="d-flex justify-content-center" style={{ minHeight: "65vh", backgroundColor: " #937147", textAlign: 'center' }}>
                    <LoginPlaceholder />
                </div>
            )}
        </>
    )
}

export default Animals