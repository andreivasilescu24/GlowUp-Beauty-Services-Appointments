import { useState } from 'react';
import { Layout } from '../components/layout/Layout';
import { MySalons as MySalonsTable } from '../components/mySalons/MySalons';
import '../styles/pages.css';

export const MySalons = () => {
    const [showForm, setShowForm] = useState(false);

    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <div className="page-header">
                        <h1>My Beauty Salons</h1>
                        <div className="header-buttons">
                            <button 
                                className="button-primary"
                                onClick={() => setShowForm(true)}
                            >
                                Add New Salon
                            </button>
                        </div>
                    </div>

                    <MySalonsTable />
                </div>
            </div>
        </Layout>
    );
}; 