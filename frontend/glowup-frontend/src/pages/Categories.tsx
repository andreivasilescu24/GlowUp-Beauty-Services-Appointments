import { Layout } from '../components/layout/Layout';
import '../styles/pages.css';

// Temporary mock data until we connect to the backend
const mockCategories = [
    {
        id: 1,
        name: "Hair Styling",
        description: "Professional hair cutting, coloring, and styling services",
        icon: "💇‍♀️"
    },
    {
        id: 2,
        name: "Nail Care",
        description: "Manicures, pedicures, and nail art services",
        icon: "💅"
    },
    {
        id: 3,
        name: "Facial Treatments",
        description: "Skincare treatments and facial therapies",
        icon: "✨"
    },
    {
        id: 4,
        name: "Massage",
        description: "Relaxing and therapeutic massage services",
        icon: "💆‍♀️"
    },
    {
        id: 5,
        name: "Makeup",
        description: "Professional makeup application and tutorials",
        icon: "💄"
    },
    {
        id: 6,
        name: "Body Treatments",
        description: "Full body care and wellness services",
        icon: "🧴"
    }
];

export const Categories = () => {
    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <h1 className="page-title">Categories</h1>
                    <p className="page-description">Browse beauty services by category</p>
                    
                    <div className="grid-container">
                        {mockCategories.map(category => (
                            <div key={category.id} className="card">
                                <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>
                                    {category.icon}
                                </div>
                                <h3 className="card-title">{category.name}</h3>
                                <p className="card-description">{category.description}</p>
                                <button className="button-secondary" style={{ marginTop: '1rem' }}>
                                    View Services
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </Layout>
    );
}; 