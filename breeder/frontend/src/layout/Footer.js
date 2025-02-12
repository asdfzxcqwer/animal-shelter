import {
  MDBFooter,
  MDBContainer,
  MDBBtn
}  from 'mdb-react-ui-kit';
import React from 'react';

function Footer() {

  return (
   <MDBFooter className='text-center text-white'>
          <div className='text-center p-3' style={{ background: 'linear-gradient(to left, #f46b45, #eea849)' }}>
        © 2022 :
        <a className='text-white' href='https://animals.com/'>
          Animals.com
        </a>
      </div>
    </MDBFooter>
  )
}
export default Footer