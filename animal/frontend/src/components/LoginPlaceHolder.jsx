import React from 'react';

const LoginPlaceholder = () => {
    return (
        <div>
            <div className='mt-2' style={{
                backgroundImage: 'url("https://perfect-pets.org/wp-content/uploads/2020/08/pexels-steshka-willems-1407718.jpg")',
                maxWidth: '100%',
                justifyContent: 'left',
                display: 'flex',
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
                PLEASE LOG IN
            </div>
            <div style={{ minHeight: "5vh" }} />
        </div>
    )
}
export default LoginPlaceholder