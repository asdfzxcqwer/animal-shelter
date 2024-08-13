import {
  MDBFooter,
  MDBContainer,
  MDBBtn
}  from 'mdb-react-ui-kit';
import React from 'react';

function Footer() {

  return (
   <MDBFooter className='text-center text-white' style={{ backgroundColor: ' rgb(72, 45, 10)' }}>
      <MDBContainer className='p-4'>
             <section className=''>
          <p className='d-flex justify-content-center align-items-center'>
                      <span className='me-3'>We are market for people looking for sweet and cute dogs and cats.</span>
                 
                      <MDBBtn type='button' outline color="light" rounded >
                     Buy us!
              </MDBBtn>
                
                        
          </p>
        </section>

      </MDBContainer>

      <div className='text-center p-3' style={{ backgroundColor: 'rgba(0, 0, 0, 0.2)' }}>
        © 2022 :
        <a className='text-white' href='https://animals.com/'>
          Animals.com
        </a>
      </div>
    </MDBFooter>
  )
}
export default Footer