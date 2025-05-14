import { Layout } from '../components/layout/Layout';
import '../styles/pages.css';

// Temporary mock data until we connect to the backend
const mockAppointments = [
    {
        id: 1,
        salonName: "Elegant Beauty",
        service: "Hair Styling",
        date: "2024-03-25",
        time: "14:00",
        status: "Upcoming"
    },
    {
        id: 2,
        salonName: "Modern Styles",
        service: "Manicure",
        date: "2024-03-27",
        time: "10:30",
        status: "Upcoming"
    },
    {
        id: 3,
        salonName: "Natural Glow",
        service: "Facial Treatment",
        date: "2024-03-20",
        time: "15:00",
        status: "Completed"
    }
];

export const MyAppointments = () => {
    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <h1 className="page-title">My Appointments</h1>
                    <p className="page-description">View and manage your upcoming beauty appointments</p>
                    
                    <div className="grid-container">
                        {mockAppointments.map(appointment => (
                            <div key={appointment.id} className="card">
                                <h3 className="card-title">{appointment.salonName}</h3>
                                <div className="card-description">
                                    <p><strong>Service:</strong> {appointment.service}</p>
                                    <p><strong>Date:</strong> {appointment.date}</p>
                                    <p><strong>Time:</strong> {appointment.time}</p>
                                    <p><strong>Status:</strong> 
                                        <span className={`status-${appointment.status.toLowerCase()}`}>
                                            {appointment.status}
                                        </span>
                                    </p>
                                </div>
                                <div style={{ marginTop: '1rem', display: 'flex', gap: '0.5rem' }}>
                                    <button className="button-secondary">Reschedule</button>
                                    <button className="button-primary">Cancel</button>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </Layout>
    );
}; 