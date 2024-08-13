import {useState} from "react";
import './Navbar.css'
import {Link, useNavigate} from "react-router-dom";

function Navbar(){
    let navigate = useNavigate()

    const logout = () => {
        localStorage.removeItem("email")
        localStorage.removeItem('id')
        setUser(null)
        navigate("/")
    }

    const [user, setUser] = useState(null)
    if(localStorage.getItem("email") != null && user == null){
        setUser(localStorage.getItem("email"))
    }

    return (
        <section className="top-nav">
            <input id="menu-toggle" type="checkbox"/>
            <label className='menu-button-container' htmlFor="menu-toggle">
                <div className='menu-button'></div>
            </label>
            {user ? (
                <ul className="menu">
                    <li><Link className={"menu-link"} to="/">Home</Link></li>
                    <li><Link className={"menu-link"} to="/about">About Us</Link></li>
                    <li><Link className={"menu-link"} to="/animal">Animals</Link></li>
                    <li>Hello, {user}</li>
                    <li><button className={"no-style-menu-button"} onClick={() => logout()}>Logout</button></li>
                </ul>) : (
                <ul className="menu">
                    <li><Link className={"menu-link"} to="/register">Register</Link></li>
                    <li><Link className={"menu-link"} to="/login">Login</Link></li>
                </ul>)
            }
            <div style={ {right: "5h",position: "relative"}}>
                <Link to={"/"}><img className={"icon"} src={"paw.png"} alt={"icon"}/></Link>
            </div>
        </section>
    );
}

export default Navbar;