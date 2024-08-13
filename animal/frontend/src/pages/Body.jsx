export default function Body() {
    return (
        <>
            <div
                className="page-features"
                xyz="fade flip-down stagger duration-10 delay-2 ease-out-back"
            >
                <div className="feature-item xyz-nested" style={{ backgroundImage: 'url("den.jpg")' }}></div>
                <div className="feature-item xyz-nested" style={{ backgroundImage: 'url("p6.jpg")' }}></div>
                <div className="feature-item xyz-nested" style={{ backgroundImage: 'url("p8.jpg")' }}></div>
            </div>
        </>
    );
}