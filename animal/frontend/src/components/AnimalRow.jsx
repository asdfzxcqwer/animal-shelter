import React, { useEffect, useState } from 'react';
import { Button, Modal, Nav } from 'react-bootstrap';
import Select from 'react-select';
import AddPayment from '../components/AddPayment';

const AnimalRow = ({ animal }) => {

    const [feedback, setFeedback] = useState([])
    const [payment, setPayment] = useState([])
    const [selectedPayment, setSelectedPayment] = useState([]);
    const handleCloseCreatePayment = () => setShowPublishConfirmation(false);
    const [showCreatePayment, setShowPublishConfirmation] = useState(false);
    const handleShowPublishConfirmation = (animal) => {

        setShowPublishConfirmation(true);

        setSelectedPayment(selectedPayment)

    }

    return (
        <>
            <tr>
                <td style={{ color: 'white' }}>{animal?.animalType}</td>
                <td style={{ color: 'white' }}>{animal?.breed}</td>
                <td style={{ color: 'white' }}>{animal?.price?.amount + " " + animal?.price?.currency}</td>
                <td style={{ color: 'white' }}>{animal?.dateOfBirth.replace("T", " ")}</td>
                <td><Button id="btnPayment" onClick={handleShowPublishConfirmation}>Buy</Button></td>

            </tr>
            <Modal show={showCreatePayment} onHide={handleCloseCreatePayment}>
                <Modal.Header>
                    <Modal.Title>Fill details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {feedback}
                    <AddPayment animal={animal} animalId={animal?.id} setPayment={setPayment} />

                </Modal.Body>
                <Modal.Footer>
                    <Button id="btnClose" variant="contained" color="warning" onClick={handleCloseCreatePayment}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
export default AnimalRow