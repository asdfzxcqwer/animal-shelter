import axios from 'axios'
import React, { useEffect, useState } from 'react'
import queryString from "query-string"

const PaymentResult = ({ photo, result }) => {
    const url = queryString.parse(window.location.search)
    const [paymentId, setStripeId] = useState(url.session_id)


    const fetchData = async () => {
        if (paymentId !== "") {
            axios.get(`http://localhost:8085/api/v1/payments/check/${paymentId}`, {
                headers: {
                    authorization: localStorage.getItem('authorization')
                }
            })
        }
        console.log(paymentId)
    }
    useEffect(() => {
        fetchData()


    }, paymentId)
    return (
        <div class="d-flex justify-content-center" style={{ minHeight: "75vh", backgroundColor: " #937147", textAlign: 'center' }}>
            <div className="d-flex justify-content-center">
                <div className='mt-5' style={{
                    backgroundImage: photo,
                    textAlign: 'center',
                    backgroundRepeat: 'no-repeat',
                    backgroundSize: ' cover',
                    minHeight: 550,
                    minWidth: 800,
                    borderRadius: "30px",
                    maxWidth: 600,
                    maxHeight: 600,
                    color: "white",
                    fontSize: 50,
                    justifyContent: "center",
                    display: "flex"
                }}>
                    {result}
                </div>
            </div>
            <div style={{ minHeight: "10vh", backgroundColor: " #937147" }}></div>
        </div>
    )
}

export default PaymentResult