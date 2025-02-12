import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Alert, Button, Col, FloatingLabel, Form, Row } from 'react-bootstrap';
import SelectCurrency from 'react-select-currency';
import Select from 'react-select';
import './Register.css'
import './AddAnimal.css'
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
const UpdateAnimal = () => {


    const [animalId, setAnimalId] = useState(0)

    const [breed, setBreed] = useState("")
    const [breederId, setBreederId] = useState(0)
    const [animalType, setAnimalType] = useState("")
    const [priceAmount, setPriceAmount] = useState("")
    const [currency, setCurrency] = useState("")
    const [dateOfBirth, setDateOfBirth] = useState("")
    const [feedback, setFeedback] = useState([])
    const [isFormCorrect, setIsFormCorrect] = useState(false)
    const [breedValidation, setNameValidation] = useState(false)
    const [dateOfBirthValidation, setDateOfBirthValidation] = useState(false)

    const navigate = useNavigate();
    const { id } = useParams();

    const [user, setUser] = useState(null)
    if (localStorage.getItem("id") != null && user == null) {
        setUser(localStorage.getItem("id"))
    }

    useEffect(() => {
        getAnimalById();
    }, [])

    const getAnimalById = async e => {
        const animalsInfo = await axios.get(`http://localhost:8080/api/v1/animals/${id}`, {
            headers: {
                authorization: localStorage.getItem('authorization')
            }
        });
        setAnimalId(id)
        setCurrency(animalsInfo.data.price.currency)
        setBreed(animalsInfo.data.breed)
        setAnimalType(animalsInfo.data.animalType)
        if (animalsInfo.data.animalType === options[1].value) {
            setAnimalType(options[1])
        }
        else {
            setAnimalType(options[0])
        }
        setDateOfBirth(animalsInfo.data.dateOfBirth)
        setBreederId(user)
        setPriceAmount(animalsInfo.data.price.amount)
    }
    const clearNotification = () => {
        setFeedback([])
    }
    const options = [
        { value: "DOG", label: "Dog" },
        { value: "CAT", label: "Cat" },
    ]
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
    function sleep(time) {
        return new Promise((resolve) => setTimeout(resolve, time)
        )
    }
    const updateAnimalHandler = async (e) => {
        e.preventDefault();
        if (isFormCorrect) {
            const updateAnimal = {
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
            await axios.put(`http://localhost:8080/api/v1/animals/${id}`, updateAnimal, {
                headers: {
                    authorization: localStorage.getItem('authorization')
                }
            }, validateStatus).then((res) => {
                setFeedback(
                    <Alert variant="success">
                        Animal was updated!
                    </Alert>

                )
                console.log(res)

                setTimeout(clearNotification, 5000);
                sleep(1000).then(() => {
                    navigate("/animals");
                })
            }
            ).catch((e) => {
                setFeedback(
                    <Alert variant="danger">
                        {e.response.data.message == null ? "Animal failed to update!" : e.response.data.message}
                    </Alert>
                )
                setTimeout(clearNotification, 5000);
            })
        }
    }

    return (
        <div className="body">
            <div className="feedback" >{feedback}</div>

            <div className="center" id="center_add_animal">

                <h1>Update Animal</h1>
                <Form onSubmit={(e) => updateAnimalHandler(e)}>

                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <FloatingLabel label={"Breed"}>
                                <Form.Control onChange={(e) => { setBreed(e.target.value) }} type="text" value={breed} placeholder="Breed" required />
                            </FloatingLabel>
                            {breedValidation !== "" ? <p style={{ color: "red", marginBottom: "0px" }}>{breedValidation}</p> : null}
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <FloatingLabel label="Price">
                                {console.log("PriceAmount in form = " + priceAmount)}
                                <Form.Control value={priceAmount} onChange={(e) => setPriceAmount(e.target.value)} type="number" min={0.00} step="0.01" required />
                            </FloatingLabel>
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            {console.log("Currency in form = " + currency)}
                            <SelectCurrency value={currency} style={{ width: "100%", height: "inherit", borderColor: "lightgray" }} onChange={(e) => setCurrency(e.target.value)} required />
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <FloatingLabel label="Date of Birth">
                                <Form.Control value={dateOfBirth} onChange={(e) => setDateOfBirth(e.target.value)} type="datetime-local" placeholder="Date of Birth" required />
                            </FloatingLabel>
                            {dateOfBirthValidation !== "" ? <p style={{ color: "red", marginBottom: "0px" }}>{dateOfBirthValidation}</p> : null}
                        </Form.Group>
                    </Row>
                    <Row className="mb-3">
                        <Form.Group as={Col} xs={12} md={12}>
                            <Select style={{ height: "58px" }} value={animalType} onChange={(e) => { setAnimalType(e) }} options={options} multiple />
                        </Form.Group>
                    </Row>
                    <div className="d-flex justify-content-end">
                        <Button className="ps-4 pe-4 w-100" style={{ marginTop: "2vh" }} variant="success" type="submit">
                            Update
                        </Button>
                    </div>
                </Form>
            </div>
        </div>
    )
}
export default UpdateAnimal