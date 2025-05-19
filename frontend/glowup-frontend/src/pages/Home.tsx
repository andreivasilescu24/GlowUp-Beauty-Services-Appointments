import { Layout } from '../components/layout/Layout';
import { useNavigate } from 'react-router-dom';
import '../styles/pages.css';

export const Home = () => {
    const navigate = useNavigate();

    return (
        <Layout>
            <div className="page-container">
                <div className="home-content">
                    <h1 className="home-title">Welcome to GlowUp!</h1>
                    <p className="home-description">
                        Discover and book appointments at the finest beauty salons in your area.
                    </p>
                    <button 
                        className="explore-button"
                        onClick={() => navigate("/salons")}
                    >
                        Explore our Salons
                    </button>
                </div>
            </div>
        </Layout>
    );
}; 