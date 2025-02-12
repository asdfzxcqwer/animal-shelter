import React, { useState } from 'react';
import Select from 'react-select';
import icon_edit from './icons/icons8-edit-64.png'
import icon_delete from './icons/icons8-remove-64.png'
import { Link } from "react-router-dom";
import axios from 'axios'
import queryString from "query-string"
import { Button } from 'react-bootstrap'


const AnimalRow = ({ animal }) => {

    const url = queryString.parse(window.location.search)
    const [animalId, setStripeId] = useState(url.session_id)
    const [feedback, setFeedback] = useState([])

    const clearNotification = () => {
        setFeedback([])
    }

    const deleteAnimalHandler = async (animalId) => {
        if (animalId !== "") {
            axios.delete(`http://localhost:8080/api/v1/animals/${animalId}`, {
                headers: {
                    authorization: localStorage.getItem('authorization')
                }
            }
            ).then(result => {
                window.location.reload()
            })
        }
    }

    return (
        <div className='animal_row'>

            <div className='animalField'>{animal?.animalType}</div>
            {/*<div className='animalField'>AVAILABLE</div>*/}
            <div className='animalField'>{animal?.breed}</div>
            <div className='animalField'>{animal?.dateOfBirth.replace("T", " ")}</div>
            <div className='animalField'>
                <div className='price_amount'>{animal?.price?.amount}</div>
                <div>{animal?.price?.currency}</div>
            </div>
            <div className='animalField'>{animal?.animalStatus}</div>
            <div className='animalField' id="actions_txt">
                <div ><Link to={`/animal/update/${animal.id}`}><img className={"action"} src={icon_edit} alt={"icon"} /></Link></div>
                <Button type="submit" id="delete" className="btn btn-outline-light" onClick={() => deleteAnimalHandler(animal.id)} >
                    <img className={"action"} src={icon_delete} alt={"icon"} />
                </Button>

            </div>


        </div>
    )
}
export default AnimalRow