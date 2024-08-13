import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Alert, Button, Col, FloatingLabel, Form, Row } from 'react-bootstrap';
import SelectCurrency from 'react-select-currency';
import Select from 'react-select';
import { useNavigate } from "react-router-dom";
import './Register.css'
import './AddAnimal.css'
const AddAnimal = () => {
    const navigate = useNavigate();
    function sleep(time) {
        return new Promise((resolve) => setTimeout(resolve, time)
        )
    }
    const [breed, setBreed] = useState("")
    const [breederId, setBreederId] = useState(0)
    const [animalType, setAnimalType] = useState("")
    const [priceAmount, setPriceAmount] = useState("")
    const [currency, setCurrency] = useState("")
    const [dateOfBirth, setDateOfBirth] = useState("")
    const [dateOfBirthValidation, setDateOfBirthValidation] = useState(false)
    const [feedback, setFeedback] = useState([])
    const [isFormCorrect, setIsFormCorrect] = useState(false)
    const [breedValidation, setNameValidation] = useState(false)
    const clearNotification = () => {
        setFeedback([])
    }
    const options = [
        { value: "DOG", label: "Dog" },
        { value: "CAT", label: "Cat" },
    ]

    const [user, setUser] = useState(null)
    if (localStorage.getItem("id") != null && user == null) {
        setUser(localStorage.getItem("id"))
    }
    const addAnimalHandler = async (e) => {
        e.preventDefault();
        if (isFormCorrect) {
            console.log(user)
            const newAnimal = {
                breederId: user,
                animalType: animalType.value,
                price: {
                    amount: priceAmount,
                    currency: currency
                },
                animalStatus: "AVAILABLE",
                dateOfBirth: dateOfBirth,
                breed: breed
            }
            function validateStatus(status) {
                return status === 201
            }

            await axios.post(`http://localhost:8080/api/v1/animals`, newAnimal, {
                headers: {
                    authorization: localStorage.getItem('authorization')
                }
            }, validateStatus).then((res) => {
                setFeedback(
                    <Alert variant="success">
                        Animal was added!
                    </Alert>

                )
                console.log(res)

                setTimeout(clearNotification, 5000);
                sleep(1000).then(() => {
                    navigate("/animals");
                })
                //window.location.href='/animals'
            }
            ).catch((e) => {
                setFeedback(
                    <Alert variant="danger">
                        {e.response.data.message == null ? "Animal failed to add!" : e.response.data.message}
                    </Alert>
                )
                setTimeout(clearNotification, 5000);
            })
        }
    }
    function timeout(delay) {
        return new Promise(res => setTimeout(res, delay));
    }
    useEffect(() => {
        breedInputHandler()
    }, [breed])

    const breedInputHandler = () => {
        const regName = /^[a-zA-Z0-9 ]+$/;
        if (breed !== "" && breed.length < 2) {
            setNameValidation("Breed should have at least 2 characters")
            setIsFormCorrect(false)
        }
        else if (breed !== "" && !regName.test(breed)) {
            setNameValidation("Breed contains characters that are not allowed")
            setIsFormCorrect(false)
        }
        else {
            setNameValidation("")
            setIsFormCorrect(true)
        }
    }

    useEffect(() => {
        dateOfBirthInputHandler()
    }, [dateOfBirth])

    const dateOfBirthInputHandler = () => {
        if (Date.now() < Date.parse(dateOfBirth)) {
            setDateOfBirthValidation("This date have to be in past")
            setIsFormCorrect(false)
        }
        else {
            setDateOfBirthValidation("")
            setIsFormCorrect(true)
        }
    }


    return (
        <div className="body">
            <div className="feedback" >{feedback}</div>

            <div className="center" id="center_add_animal">

                <h1>Add Animal</h1>
                <Form onSubmit={(e) => addAnimalHandler(e)}>

                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <FloatingLabel label="Breed">
                                <Form.Control onChange={(e) => { setBreed(e.target.value) }} type="text" placeholder="Breed" required />
                            </FloatingLabel>
                            {breedValidation !== "" ? <p style={{ color: "red", marginBottom: "0px" }}>{breedValidation}</p> : null}
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <FloatingLabel label="Price">
                                <Form.Control onChange={(e) => setPriceAmount(e.target.value)} type="number" min={0.00} step="0.01" placeholder="0.00" />
                            </FloatingLabel>
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <SelectCurrency style={{ width: "100%", height: "inherit", borderColor: "lightgray" }} value={currency} onChange={(e) => setCurrency(e.target.value)} />
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <FloatingLabel label="Date of Birth">
                                <Form.Control onChange={(e) => setDateOfBirth(e.target.value)} type="datetime-local" placeholder="Date of Birth" required />
                            </FloatingLabel>
                            {dateOfBirthValidation !== "" ? <p style={{ color: "red", marginBottom: "0px" }}>{dateOfBirthValidation}</p> : null}
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <Select style={{ height: "58px" }} onChange={(e) => { setAnimalType(e) }} options={options} placeholder="Animal Type" />
                        </Form.Group>
                    </Row>
                    <div className="d-flex justify-content-end">
                        <Button className="ps-4 pe-4 w-100" variant="success" type="submit">
                            Add Animal
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
    )
}

export default AddAnimal