import { Layout } from '../components/layout/Layout';
import '../styles/pages.css';

export const Home = () => {
    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <h1 className="page-title">Welcome to GlowUp!</h1>
                    <p className="page-description">Your beauty journey starts here.</p>
                    
                    <div className="welcome-stats">
                        <div className="stat-card">
                            <h3>Beauty Salons</h3>
                            <p>Discover top-rated salons in your area</p>
                        </div>
                        <div className="stat-card">
                            <h3>Services</h3>
                            <p>Browse through various beauty services</p>
                        </div>
                        <div className="stat-card">
                            <h3>Appointments</h3>
                            <p>Book and manage your appointments</p>
                        </div>
                    </div>
                </div>
            </div>
        </Layout>
    );
}; 