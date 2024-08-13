import React from 'react';
import { Nav } from 'react-bootstrap';
import { NavLink } from "react-router-dom";

const Photo = ({ url }) => {
    return (
        <div >
            <div style={{ minHeight: 70, justifyContent: 'space-between' }}></div>
            <div style={{ backgroundImage: url, maxWidth: '100%', justifyContent: 'left', display: 'flex', textAlign: 'center', backgroundRepeat: 'no-repeat', backgroundSize: ' cover', minHeight: 550, minWidth: 800, borderRadius: "30px", maxWidth: 600, maxHeight: 600 }}>
                <Nav.Link><NavLink style={{ textDecoration: "none", color: "white" }} to="/animal">
                    <button id="btnJoin" style={{ color: 'white', backgroundColor: 'rgb(72, 45, 10)', borderColor: 'rgb(72, 45, 10)', borderRadius: '12px', fontWeight: 'bold', fontSize: "calc(15px + 0.5vw)", minHeight: 5, maxHeight: 80, margin: 20, padding: 20, textAlign: 'center' }}>
                        Buy Me!
                    </button>
                </NavLink></Nav.Link>
            </div>
            <div style={{ minHeight: "5vh" }} />
        </div>
    )
}
export default Photo