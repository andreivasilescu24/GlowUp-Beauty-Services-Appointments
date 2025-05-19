import { useState, useEffect } from 'react';
import { Layout } from '../components/layout/Layout';
import { beautySalonService } from '../services/beautySalonService';
import type { BeautySalon } from '../types/beautySalon';
import '../styles/pages.css';

interface Employee {
    id: string;
    name: string;
    experience: number;
    phone: string;
}

interface EmployeeService {
    employeeId: string;
    serviceId: string;
    serviceName: string;
    description: string;
    price: number;
    duration: number;
}

interface AppointmentForm {
    beautySalonId: string;
    employeeId: string;
    beautyServiceId: string;
    appointmentDateAndTime: string;
}

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
    const [salons, setSalons] = useState<BeautySalon[]>([]);
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [employeeServices, setEmployeeServices] = useState<EmployeeService[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);

    const [formData, setFormData] = useState<AppointmentForm>({
        beautySalonId: '',
        employeeId: '',
        beautyServiceId: '',
        appointmentDateAndTime: ''
    });

    useEffect(() => {
        fetchSalons();
    }, []);

    const fetchSalons = async () => {
        try {
            setLoading(true);
            const response = await beautySalonService.getBeautySalons();
            setSalons(response);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch beauty salons');
        } finally {
            setLoading(false);
        }
    };

    const fetchEmployees = async (salonId: string) => {
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8090/api/v1/beautySalon/${salonId}/employees`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch employees');
            }
            const data = await response.json();
            setEmployees(data);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch employees');
        } finally {
            setLoading(false);
        }
    };

    const fetchEmployeeServices = async (salonId: string, employeeId: string) => {
        try {
            setLoading(true);
            const response = await fetch(
                `http://localhost:8090/api/v1/beautySalon/${salonId}/employees/${employeeId}/services`,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }
            );
            if (!response.ok) {
                throw new Error('Failed to fetch employee services');
            }
            const data = await response.json();
            setEmployeeServices(data);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch employee services');
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        // If salon is selected, fetch its employees
        if (name === 'beautySalonId' && value) {
            fetchEmployees(value);
            // Reset employee and service selections
            setFormData(prev => ({
                ...prev,
                employeeId: '',
                beautyServiceId: ''
            }));
            setEmployeeServices([]);
        }

        // If employee is selected, fetch their services
        if (name === 'employeeId' && value) {
            fetchEmployeeServices(formData.beautySalonId, value);
            // Reset service selection
            setFormData(prev => ({
                ...prev,
                beautyServiceId: ''
            }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            const response = await fetch('http://localhost:8090/api/v1/appointments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(formData)
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                throw new Error(errorData?.message || 'Failed to create appointment');
            }

            // Reset form and hide it
            setFormData({
                beautySalonId: '',
                employeeId: '',
                beautyServiceId: '',
                appointmentDateAndTime: ''
            });
            setShowForm(false);
            setError('');
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to create appointment');
        } finally {
            setLoading(false);
        }
    };

    if (loading && !salons.length) {
        return <div className="page-loading">Loading...</div>;
    }

    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <div className="page-header">
                        <h1>My Appointments</h1>
                        <button 
                            className="button-primary"
                            onClick={() => setShowForm(true)}
                        >
                            New Appointment
                        </button>
                    </div>

                    {showForm && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h2>Book New Appointment</h2>
                                <form onSubmit={handleSubmit} className="appointment-form">
                                    <div className="form-group">
                                        <label htmlFor="beautySalonId">Select Salon</label>
                                        <select
                                            id="beautySalonId"
                                            name="beautySalonId"
                                            value={formData.beautySalonId}
                                            onChange={handleInputChange}
                                            required
                                        >
                                            <option value="">Select a salon</option>
                                            {salons.map(salon => (
                                                <option key={salon.id} value={salon.id}>
                                                    {salon.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="employeeId">Select Employee</label>
                                        <select
                                            id="employeeId"
                                            name="employeeId"
                                            value={formData.employeeId}
                                            onChange={handleInputChange}
                                            required
                                            disabled={!formData.beautySalonId}
                                        >
                                            <option value="">Select an employee</option>
                                            {employees.map(employee => (
                                                <option key={employee.id} value={employee.id}>
                                                    {employee.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="beautyServiceId">Select Service</label>
                                        <select
                                            id="beautyServiceId"
                                            name="beautyServiceId"
                                            value={formData.beautyServiceId}
                                            onChange={handleInputChange}
                                            required
                                            disabled={!formData.employeeId}
                                        >
                                            <option value="">Select a service</option>
                                            {employeeServices.map(service => (
                                                <option key={service.serviceId} value={service.serviceId}>
                                                    {service.serviceName} - {service.price} RON ({service.duration} min)
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="appointmentDateAndTime">Date and Time</label>
                                        <input
                                            type="datetime-local"
                                            id="appointmentDateAndTime"
                                            name="appointmentDateAndTime"
                                            value={formData.appointmentDateAndTime}
                                            onChange={handleInputChange}
                                            required
                                            min={new Date().toISOString().slice(0, 16)}
                                        />
                                    </div>

                                    <div className="form-actions">
                                        <button 
                                            type="button" 
                                            className="button-secondary"
                                            onClick={() => setShowForm(false)}
                                        >
                                            Cancel
                                        </button>
                                        <button 
                                            type="submit" 
                                            className="button-primary"
                                            disabled={loading}
                                        >
                                            {loading ? 'Booking...' : 'Book Appointment'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}

                    {error && <div className="error-message">{error}</div>}
                </div>
            </div>
        </Layout>
    );
}; 