import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { Alert, Button, Col, FloatingLabel, Form, Row } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'



const AddPayment = ({ animalId, setPayment }) => {
    const [house, setHouse] = useState("")
    const [city, setCity] = useState("")
    const [email, setEmail] = useState("")
    const navigate = useNavigate()
    const [feedback, setFeedback] = useState([])
    const [isFormCorrect, setIsFormCorrect] = useState(false)
    const [emailValidation, setEmailValidation] = useState("")
    const emailRegex = new RegExp(".+@.+\..+");

    const clearNotification = () => {
        setFeedback([])
    }

    const addPaymentHandler = async (e) => {
        e.preventDefault();

        if (isFormCorrect) {
            const newPayment = {
                animalId: animalId,
                house: house,
                city: city,
                email: email

            }
            function validateStatus(status) {
                return status === 201
            }
            await axios.post(`http://localhost:8085/api/v1/payments`, newPayment, {
                headers: {
                    authorization: localStorage.getItem('authorization')
                }
            }).then((res) => {
                setFeedback(
                    <Alert variant="success">
                        You successfully registered!
                    </Alert>
                )
                setPayment(res.data)

                setTimeout(clearNotification, 5000);
                window.open(res.data.link, "_blank")
            }
            ).catch((e) => {
                setFeedback(
                    <Alert variant="danger">
                        {e.response.data.message}
                    </Alert>
                )
                setTimeout(clearNotification, 5000);
            })

        }
    }

    useEffect(() => {
        emailInputHandler()
    }, [email])

    const emailInputHandler = () => {
        if (!emailRegex.test(email) && email !== "") {
            setEmailValidation("Email not valid")
            setIsFormCorrect(false)
        }
        else {
            setEmailValidation("")
            setIsFormCorrect(true)
        }

    }

    return (
        <div>
            <Form>
                <Row className="mb-4">
                    {feedback}
                    <Form.Group as={Col} xs={12} md={12} controlId="formGridName">
                        <FloatingLabel label="Street">
                            <Form.Control id="textHouse" onChange={(e) => setHouse(e.target.value)} type="text" placeholder="Street" required />
                        </FloatingLabel>
                    </Form.Group>
                </Row>
                <Row className="mb-4">
                    <Form.Group as={Col} xs={12} md={12} controlId="formGridName">
                        <FloatingLabel label="City">
                            <Form.Control id="textCity" onChange={(e) => setCity(e.target.value)} type="text" placeholder="City" required />
                        </FloatingLabel>
                    </Form.Group>
                </Row>
                <Row className="mb-4">
                    <Form.Group as={Col} xs={12} md={12} controlId="formGridName">
                        <FloatingLabel label="Email">
                            <Form.Control id="textEmail" onChange={(e) => setEmail(e.target.value)} type="text" placeholder="Email" required />
                        </FloatingLabel>
                        {emailValidation !== "" ? <p style={{ color: "red", marginBottom: "0px" }}>{emailValidation}</p> : null}
                    </Form.Group>
                </Row>
                <div class="d-flex justify-content-center">
                    <Button id="btnCreatePayment" variant="outline-primary" type="submit" className='mb-1' onClick={(e) => addPaymentHandler(e)} style={{ fontWeight: 'bold', fontSize: 15, borderWidth: 2, justifyContent: 'center' }} >
                        Next
                    </Button>
                </div>
            </Form>
        </div>

    )
}

export default AddPayment