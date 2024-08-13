import './Register.css'
import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function Register() {

    let navigate = useNavigate()

    const [user, setUser] = useState({
        firstName:"",
        lastName:"",
        email:"",
        password:""
    })

    const{firstName, lastName, email, password} = user

    const [isError, setIsError] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")

    const onInputChange = (e) => {
        setUser({...user, [e.target.name]:e.target.value})
    }

    const onSubmit = async (e) => {
        e.preventDefault()
        await axios.post("http://localhost:8080/api/v1/authorization/register", user)
            .then(response => {
                console.log(response)
                navigate("/")
            })
            .catch(error => {
                console.log(error.response)
                setIsError(true)
                setErrorMessage(error.response.data.message)
            });

    }

    return (
        <div className="body">
            <div className="center" id="center_register">
                <h1>Register breeder</h1>
                <form onSubmit={(e) => onSubmit(e)}>
                    <div className="txt_field">
                        <input type="text" name='firstName' value={firstName} onChange={(e) => onInputChange(e)} required/>
                            <label>First name</label>
                    </div>
                    <div className="txt_field">
                        <input type="text" name='lastName' value={lastName} onChange={(e) => onInputChange(e)} required />
                        <label>Last name</label>
                    </div>
                    <div className="txt_field">
                        <input type="text" name='email' value={email} onChange={(e) => onInputChange(e)} required />
                        <label>Email</label>
                    </div>
                    <div className="txt_field">
                        <input type="password" name='password' value={password} onChange={(e) => onInputChange(e)} required />
                        <label>Password</label>
                    </div>
                    <input type="submit" value="Register"/>
                    <div  className="signup_link">Already have an account? <a href="/login">Log in</a>
                    </div>
                    <div >
                        {isError ? (
                            <div className={"error_message"}>{errorMessage}</div>
                        ) : (
                            <div></div>
                        )}
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Register;