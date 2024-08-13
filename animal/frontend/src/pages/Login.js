import './Register.css'
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import axios from "axios";


function Login() {

    let navigate = useNavigate()

    const [user, setUser] = useState({
        email:"",
        password:""
    })

    const [isError, setIsError] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")


    const{email, password} = user

    const onInputChange = (e) => {
        setUser({...user, [e.target.name]:e.target.value})
    }

    const onSubmit = async (e) => {
        e.preventDefault()
        await axios.post("http://localhost:8085/api/v1/authorization/login", user)
            .then(response => {
                console.log(response)
                console.log(response.headers.get("authorization"))
                localStorage.setItem('email', response.data.email)
                localStorage.setItem('id', response.data.id)
                localStorage.setItem('authorization', response.headers.get("authorization"))
                navigate("/")
            })
            .catch(error => {
                console.log(error.response)
                setIsError(true)
                setErrorMessage(error.response.data.message)
            });

    }


    return (
        <div class="d-flex justify-content-center" style={{ minHeight: "75vh", backgroundColor: " #937147", textAlign: 'center' }}>
            <div  style={{ minHeight: "10vh", backgroundColor: " #937147", textAlign: 'center' }}>  </div>
             <div className="center"  style={{ minHeight: "60vh", backgroundColor: "white", textAlign: 'center' }} >
        
            <h1 >Login</h1>
            <form onSubmit={(e) => onSubmit(e)}>
                    <div className="txt_field">
                    <input type="text" name='email' value={email} onChange={(e) => onInputChange(e)} required placeholder="Email" />
                </div>
                    <div className="txt_field">
                    <input type="password" name='password' value={password} onChange={(e) => onInputChange(e)} required placeholder="Password"/>
                </div>
                <div className="pass">Forgot password?</div>
                <input type="submit" value="Login"/>
                <div  className="signup_link">Dont have an account? <a href="/register">Register</a>
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
            <div  style={{ minHeight: "10vh", backgroundColor: " #937147", textAlign: 'center' }}>
            </div>
        </div>
    );
}

export default Login;