import Photo from "../components/Photo";


function Home() {
    return (
        <div class="d-flex justify-content-center" style={{ minHeight: "65vh", backgroundColor: " #937147", textAlign: 'center' }}>
            <Photo url='url("https://cdn.pixabay.com/photo/2016/11/22/23/13/dog-1851107_960_720.jpg")' />
            
        </div>
    );
}

export default Home;