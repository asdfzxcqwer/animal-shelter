
import icon_email from './icons8-mail-96.png'
import icon_adress from './icons8-address-96.png'

export default function Body() {
    return (
        <>
            <div className={"kot"} xyz="fade flip-down stagger duration-10 delay-15 ease-out-back">
                <img className={"kot_zdj xyz-nested"} src={"g5.jpg"} alt={"kotek"} />
            </div>
            <div className={"text_help"} xyz="fade right ease-in-out delay-1">Need help? Don't worry, we're here for you.</div>
            <div className={"contact"}>

                <div className={"contact_box"}>
                    <div className={"pageTitle_aboutUs"}>
                        Contact Us
                    </div>
                    <div className={"contact_text"}>
                        <img className={"icon_2"} src={icon_email} alt={"icon"} />
                        <div className={"contact_information"}>
                            <div className={"contact_title"}>
                                Email
                            </div>
                            <div className={"contact_data"}>
                                animals_contact@gmail.com
                            </div>
                        </div>


                    </div>
                    <div className={"contact_text"}>
                        <img className={"icon_3"} src={icon_adress} alt={"icon"} />
                        <div className={"contact_information"}>
                            <div className={"contact_title"}>
                                Adress
                            </div>
                            <div className={"contact_data"}>
                                Wita Stwosza 57
                            </div>
                            <div className={"contact_data"}>
                                80-308 Gdańsk, Poland
                            </div>
                        </div>

                    </div>
                </div>
                <div className={"map"}>
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2322.803085577214!2d18.56935663215836!3d54.39579304000268!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x46fd752650c01941%3A0x155831af2884f369!2sWita%20Stwosza%2057%2C%2080-308%20Gda%C5%84sk!5e0!3m2!1spl!2spl!4v1671458709532!5m2!1spl!2spl"
                        width="500"
                        height="350"
                        style={{ border: 0 }}
                        allowfullscreen=""
                        loading="lazy"
                        referrerpolicy="no-referrer-when-downgrade">
                    </iframe>
                </div>
            </div>
        </>
    );
}