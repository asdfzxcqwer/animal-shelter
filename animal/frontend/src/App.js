import './App.css';
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Navbar from "./layout/Navbar";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import PaymentResult from "./components/PaymentResult";
import AllAnimals from "./components/AllAnimals";
import Footer from './layout/Footer';
import AboutUs from './pages/AboutUs';
function App() {
  return (
      <div className={"App"}>
        <Router>
          <Navbar />
            <Routes>
                <Route exact path="/" element={<Home />}></Route>
                <Route exact path="/about" element={<AboutUs/>}></Route>
                <Route exact path="/login" element={<Login/>}></Route>
                <Route exact path="/register" element={<Register />}></Route>
                <Route path="/animal" element={<AllAnimals />} />
                <Route path="/payment/error" element={<PaymentResult photo='url("https://cdn.pixabay.com/photo/2016/05/07/20/49/cat-1378186_960_720.jpg")' result="PAYMENT ERROR"/>}/>
                <Route path="/payment/cancel" element={<PaymentResult photo='url("https://cdn.pixabay.com/photo/2019/07/30/05/53/dog-4372036_960_720.jpg")' result="PAYMENT CANCELLED"/>}/>
                <Route path="/payment/success" element={<PaymentResult photo='url("https://cdn.pixabay.com/photo/2020/03/31/19/20/dog-4988985_960_720.jpg")' result="PAYMENT SUCCESS"/>}/>
            </Routes>
      </Router>
      <Footer/>
      </div>
  );
}

export default App;
