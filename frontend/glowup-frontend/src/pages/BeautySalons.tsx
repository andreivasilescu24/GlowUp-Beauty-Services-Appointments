import { Layout } from '../components/layout/Layout';
import '../styles/pages.css';

// Temporary mock data until we connect to the backend
const mockSalons = [
    {
        id: 1,
        name: "Elegant Beauty",
        description: "Luxury beauty treatments and professional care",
        rating: 4.8,
        location: "Downtown"
    },
    {
        id: 2,
        name: "Modern Styles",
        description: "Contemporary beauty solutions for modern needs",
        rating: 4.5,
        location: "Westside"
    },
    {
        id: 3,
        name: "Natural Glow",
        description: "Organic and natural beauty treatments",
        rating: 4.7,
        location: "Eastside"
    }
];

export const BeautySalons = () => {
    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <h1 className="page-title">Beauty Salons</h1>
                    <p className="page-description">Discover the best beauty salons in your area.</p>
                    
                    <div className="grid-container">
                        {mockSalons.map(salon => (
                            <div key={salon.id} className="card">
                                <h3 className="card-title">{salon.name}</h3>
                                <p className="card-description">{salon.description}</p>
                                <div style={{ marginTop: '1rem' }}>
                                    <p>⭐ {salon.rating}</p>
                                    <p>📍 {salon.location}</p>
                                </div>
                                <button className="button-primary" style={{ marginTop: '1rem' }}>
                                    View Details
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </Layout>
    );
}; 