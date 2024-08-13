import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Col, Row } from 'react-bootstrap';
import Select from 'react-select';
import AppPagination from '../pages/AppPagination';
import AnimalRow from '../pages/AnimalRow';
import './Register.css'
import './List.css'
import { Link, useNavigate } from "react-router-dom";


const AllAnimals = () => {
    const [animals, setAnimals] = useState([])
    const [info, setInfo] = useState([])
    const [recordsPerPage, setRecordsPerPage] = useState(10)
    const [pageNumber, setPageNumber] = useState(0)

    const [sort, setSort] = useState("dateOfBirth,asc")
    const [feedback, setFeedback] = useState([])

    const clearNotification = () => {
        setFeedback([])
    }


    const pageRecords = [
        { value: 10, label: 10 },
        { value: 20, label: 20 },
        { value: 30, label: 30 }
    ]

    const [user, setUser] = useState(null)
    if (localStorage.getItem("id") != null && user == null) {
        setUser(localStorage.getItem("id"))
    }
    const fetchData = async () => {
        axios.get(`http://localhost:8080/api/v1/animals?breederId=${user}&page=${pageNumber}&size=${recordsPerPage}&sort=${sort}`, {
            headers: {
                authorization: localStorage.getItem('authorization')
            }
        }).then(res => {
            setAnimals(res.data.content)
            setInfo(res.data)
        })
    }

    useEffect(() => {
        fetchData()
    }, [pageNumber, recordsPerPage, sort])

    return (
        <div className="body">
            <div className="feedback" >{feedback}</div>
            <div className="head">
                <div className="pageTitle">ANIMALS</div>
                <Link className="addButton" to="/animal/add">Add new animal</Link>
            </div>

            <div className='animals_container' >
                <Select
                    className='mb-3'
                    onChange={(e) => setRecordsPerPage(e.label)}
                    options={pageRecords}
                    placeholder={"Records per page"}
                />
                <div className='animal_row' id="first_row">
                    <div className='animalField' onClick={sort === "animalType,asc" ? (e) => setSort("animalType,desc") : (e) => setSort("animalType,asc")} >Animal type</div>
                    {/*<div className='animalField' >status</div>*/}
                    <div className='animalField' onClick={sort === "breed,asc" ? (e) => setSort("breed,desc") : (e) => setSort("breed,asc")} >breed</div>
                    <div className='animalField' onClick={sort === "dateOfBirth,desc" ? (e) => setSort("dateOfBirth,asc") : (e) => setSort("dateOfBirth,desc")}>date of birth</div>
                    <div className='animalField' onClick={sort === "price.amount,desc" ? (e) => setSort("price.amount,asc") : (e) => setSort("price.amount,desc")}>price</div>
                    <div className='animalField' onClick={sort === "animalStatus,desc" ? (e) => setSort("animalStatus,asc") : (e) => setSort("animalStatus,desc")}>status</div>
                    <div className='animalField' id="actions_txt">actions</div>
                </div>


                {animals.map((animal, index) => (
                    <AnimalRow fetchData={fetchData} key={index} animal={animal} />
                ))}

                <div>
                    <AppPagination pageNumber={pageNumber} setPageNumber={setPageNumber} info={info} />
                </div>
            </div>
        </div>
    )
}

export default AllAnimals