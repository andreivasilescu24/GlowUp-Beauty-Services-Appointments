import { useState, useEffect, useMemo } from 'react';
import { Layout } from '../components/layout/Layout';
import { beautySalonService } from '../services/beautySalonService';
import type { BeautySalon } from '../types/beautySalon';
import '../styles/pages.css';

interface BeautyService {
    id: string;
    name: string;
    description: string;
}

interface Employee {
    id: string;
    name: string;
    experience: number;
    phone: string;
}

interface AddServiceForm {
    name: string;
    description: string;
    salonId: string;
}

interface AssignServiceForm {
    employeeId: string;
    serviceId: string;
    price: number;
    duration: number;
}

export const BeautyServices = () => {
    const [salons, setSalons] = useState<BeautySalon[]>([]);
    const [selectedSalonId, setSelectedSalonId] = useState<string>('');
    const [services, setServices] = useState<BeautyService[]>([]);
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [showAssignForm, setShowAssignForm] = useState(false);
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 5;

    const [formData, setFormData] = useState<AddServiceForm>({
        name: '',
        description: '',
        salonId: ''
    });

    const [assignFormData, setAssignFormData] = useState<AssignServiceForm>({
        employeeId: '',
        serviceId: '',
        price: 0,
        duration: 0
    });

    useEffect(() => {
        fetchMySalons();
    }, []);

    useEffect(() => {
        if (selectedSalonId) {
            fetchServices(selectedSalonId);
            fetchEmployees(selectedSalonId);
        }
    }, [selectedSalonId]);

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

    const fetchMySalons = async () => {
        try {
            setLoading(true);
            const response = await beautySalonService.getMyBeautySalons();
            setSalons(response);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch your beauty salons');
        } finally {
            setLoading(false);
        }
    };

    const fetchServices = async (salonId: string) => {
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8090/api/v1/beautySalon/${salonId}/beautyServices`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            if (!response.ok) {
                throw new Error('Failed to fetch services');
            }
            const data = await response.json();
            console.log('Fetched services:', data); // Debug log
            setServices(data); // Store the complete service objects with their IDs
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch services');
        } finally {
            setLoading(false);
        }
    };

    const handleSalonChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedSalonId(e.target.value);
        setCurrentPage(0); // Reset pagination when changing salon
        setSearch(''); // Reset search when changing salon
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8090/api/v1/beautySalon/${formData.salonId}/beautyServices/add`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({
                    name: formData.name,
                    description: formData.description
                })
            });

            if (!response.ok) {
                throw new Error('Failed to add service');
            }

            // Reset form and hide it
            setFormData({
                name: '',
                description: '',
                salonId: ''
            });
            setShowForm(false);
            
            // Refresh services list if we're viewing the same salon
            if (selectedSalonId === formData.salonId) {
                fetchServices(selectedSalonId);
            }
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to add service');
        } finally {
            setLoading(false);
        }
    };

    const handleAddClick = () => {
        if (!selectedSalonId) {
            setError('Please select a salon first');
            return;
        }
        setFormData(prev => ({ ...prev, salonId: selectedSalonId }));
        setShowForm(true);
        setError('');
    };

    // Search and pagination logic
    const filteredServices = useMemo(() => {
        return services.filter(service =>
            service.name.toLowerCase().includes(search.toLowerCase()) ||
            service.description.toLowerCase().includes(search.toLowerCase())
        );
    }, [services, search]);

    const paginatedServices = useMemo(() => {
        const start = currentPage * pageSize;
        const end = start + pageSize;
        return filteredServices.slice(start, end);
    }, [filteredServices, currentPage]);

    const totalPages = Math.ceil(filteredServices.length / pageSize);

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value);
        setCurrentPage(0);
    };

    const handlePageChange = (newPage: number) => {
        setCurrentPage(newPage);
    };

    const handleAssignService = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!assignFormData.serviceId || !assignFormData.employeeId) {
            setError('Please select both a service and an employee');
            return;
        }

        try {
            setLoading(true);
            console.log('Assigning service with ID:', assignFormData.serviceId); // Debug log
            const response = await fetch(
                `http://localhost:8090/api/v1/beautySalon/${selectedSalonId}/employees/${assignFormData.employeeId}/services/${assignFormData.serviceId}`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                    body: JSON.stringify({
                        price: assignFormData.price,
                        duration: assignFormData.duration
                    })
                }
            );

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                throw new Error(errorData?.message || 'Failed to assign service to employee');
            }

            const data = await response.json();
            console.log('Service assigned successfully:', data);

            // Reset form and hide it
            setAssignFormData({
                employeeId: '',
                serviceId: '',
                price: 0,
                duration: 0
            });
            setShowAssignForm(false);
            setError('');
        } catch (err) {
            console.error('Error assigning service:', err);
            setError(err instanceof Error ? err.message : 'Failed to assign service to employee');
        } finally {
            setLoading(false);
        }
    };

    const handleAssignInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        console.log('Input change:', name, value, 'Selected service:', services.find(s => s.id === value)); // Debug log
        setAssignFormData(prev => ({
            ...prev,
            [name]: name === 'price' || name === 'duration' ? parseFloat(value) || 0 : value
        }));
    };

    const handleAssignClick = () => {
        if (!selectedSalonId) {
            setError('Please select a salon first');
            return;
        }
        setShowAssignForm(true);
        setError('');
    };

    if (loading && !salons.length) {
        return <div className="page-loading">Loading...</div>;
    }

    return (
        <Layout>
            <div className="page-container">
                <div className="page-content">
                    <div className="page-header">
                        <h1>Beauty Services</h1>
                        <div className="salon-select-container">
                            <select
                                value={selectedSalonId}
                                onChange={handleSalonChange}
                                className="salon-select"
                            >
                                <option value="">Select a salon</option>
                                {salons.map(salon => (
                                    <option key={salon.id} value={salon.id}>
                                        {salon.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="header-buttons">
                            <button 
                                className="button-primary"
                                onClick={handleAddClick}
                            >
                                Add Service
                            </button>
                            <button 
                                className="button-secondary"
                                onClick={handleAssignClick}
                            >
                                Assign Service to Employee
                            </button>
                        </div>
                    </div>

                    <div className="search-container">
                        <input
                            type="text"
                            placeholder="Search services..."
                            value={search}
                            onChange={handleSearchChange}
                            className="search-input"
                        />
                    </div>

                    <div className="table-container">
                        <table className="services-table">
                            <thead>
                                <tr>
                                    <th>Service Name</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                {paginatedServices.map((service) => (
                                    <tr key={service.id} className="service-row">
                                        <td>{service.name}</td>
                                        <td>{service.description}</td>
                                    </tr>
                                ))}
                                {paginatedServices.length === 0 && (
                                    <tr>
                                        <td colSpan={2} className="no-results">
                                            No services found
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>

                    {filteredServices.length > pageSize && (
                        <div className="pagination">
                            <button
                                onClick={() => handlePageChange(currentPage - 1)}
                                disabled={currentPage === 0}
                                className="pagination-button"
                            >
                                Previous
                            </button>
                            <span className="pagination-info">
                                Page {currentPage + 1} of {totalPages} ({filteredServices.length} items)
                            </span>
                            <button
                                onClick={() => handlePageChange(currentPage + 1)}
                                disabled={currentPage >= totalPages - 1}
                                className="pagination-button"
                            >
                                Next
                            </button>
                        </div>
                    )}

                    {showForm && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h2>Add New Service</h2>
                                <form onSubmit={handleSubmit} className="add-service-form">
                                    <div className="form-group">
                                        <label htmlFor="name">Service Name</label>
                                        <input
                                            type="text"
                                            id="name"
                                            name="name"
                                            value={formData.name}
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="description">Description</label>
                                        <textarea
                                            id="description"
                                            name="description"
                                            value={formData.description}
                                            onChange={handleInputChange}
                                            required
                                            rows={4}
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
                                            {loading ? 'Adding...' : 'Add Service'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}

                    {showAssignForm && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h2>Assign Service to Employee</h2>
                                <form onSubmit={handleAssignService} className="assign-service-form">
                                    <div className="form-group">
                                        <label htmlFor="employeeId">Select Employee</label>
                                        <select
                                            id="employeeId"
                                            name="employeeId"
                                            value={assignFormData.employeeId}
                                            onChange={handleAssignInputChange}
                                            required
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
                                        <label htmlFor="serviceId">Select Service</label>
                                        <select
                                            id="serviceId"
                                            name="serviceId"
                                            value={assignFormData.serviceId}
                                            onChange={handleAssignInputChange}
                                            required
                                        >
                                            <option value="">Select a service</option>
                                            {services.map(service => {
                                                return (
                                                    <option key={service.id} value={service.id}>
                                                        {service.name}
                                                    </option>
                                                );
                                            })}
                                        </select>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="price">Price</label>
                                        <input
                                            type="number"
                                            id="price"
                                            name="price"
                                            min="0"
                                            step="0.01"
                                            value={assignFormData.price}
                                            onChange={handleAssignInputChange}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="duration">Duration (minutes)</label>
                                        <input
                                            type="number"
                                            id="duration"
                                            name="duration"
                                            min="0"
                                            value={assignFormData.duration}
                                            onChange={handleAssignInputChange}
                                            required
                                        />
                                    </div>

                                    <div className="form-actions">
                                        <button 
                                            type="button" 
                                            className="button-secondary"
                                            onClick={() => setShowAssignForm(false)}
                                        >
                                            Cancel
                                        </button>
                                        <button 
                                            type="submit" 
                                            className="button-primary"
                                            disabled={loading || !assignFormData.serviceId || !assignFormData.employeeId}
                                        >
                                            {loading ? 'Assigning...' : 'Assign Service'}
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