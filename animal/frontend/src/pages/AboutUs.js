import Body from "./Body.jsx"
import "@animxyz/core";
import { XyzTransition } from "@animxyz/react";
import BodyTwo from "./BodyTwo.jsx";
import BodyThree from "./BodyThree.jsx";
function AboutUs() {
    return (
        <div class="d-flex justify-content-center" style={{ minHeight: "75vh", backgroundColor: " #937147", textAlign: 'center' }}>
            <XyzTransition appear duration="auto">
            <div className="page-wrap">
                    <Body />
                    <BodyTwo/>
                    <BodyThree/>
             </div>
             </XyzTransition>
        </div>
    );
}

export default AboutUs;