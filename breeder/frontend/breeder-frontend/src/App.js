import './App.css';
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Navbar from "./layout/Navbar";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Register from "./pages/Register";
import About from "./pages/About";
import AddAnimal from "./pages/AddAnimal";
import AllAnimals from './pages/AllAnimals';
import UpdateAnimal from './pages/UpdateAnimal';
import Footer from './layout/Footer';

function App() {
  return (
    <div className="App">
      <Router>
            <Navbar/>
            <Routes>
                <Route exact path="/" element={<Home/>}></Route>
                <Route exact path="/login" element={<Login/>}></Route>
                <Route exact path="/register" element={<Register/>}></Route>
          <Route exact path="/about" element={<About />}></Route>
          <Route exact path="/animals" element={<AllAnimals />}></Route>
          <Route exact path="/animal/add" element={<AddAnimal/>}></Route>
          <Route exact path="/animal/update/:id" element={<UpdateAnimal/>}></Route>
            </Routes>
      </Router>
      <Footer/>
    </div>
  );
}

export default App;
