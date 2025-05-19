import { useState, useEffect, useMemo } from 'react';
import { Layout } from '../components/layout/Layout';
import { beautySalonService } from '../services/beautySalonService';
import type { BeautySalon } from '../types/beautySalon';
import '../styles/pages.css';

interface Employee {
    id: string;
    name: string;
    experience: number;
    phone: string;
    salonName?: string;
}

interface AddEmployeeForm {
    name: string;
    experience: number;
    phone: string;
    salonId: string;
}

interface DeleteEmployeeForm {
    salonId: string;
    employeeId: string;
}

interface UpdateEmployeeForm {
    salonId: string;
    employeeId: string;
    name: string;
    experience: number;
    phone: string;
}

interface WorkingHoursForm {
    employeeId: string;
    days: {
        day: number;
        startTime: string;
        endTime: string;
    }[];
}

export const ManageEmployees = () => {
    const [salons, setSalons] = useState<BeautySalon[]>([]);
    const [selectedSalonId, setSelectedSalonId] = useState<string>('');
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showUpdateModal, setShowUpdateModal] = useState(false);
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 5;

    const [formData, setFormData] = useState<AddEmployeeForm>({
        name: '',
        experience: 0,
        phone: '',
        salonId: ''
    });

    const [deleteData, setDeleteData] = useState<DeleteEmployeeForm>({
        salonId: '',
        employeeId: ''
    });

    const [updateData, setUpdateData] = useState<UpdateEmployeeForm>({
        salonId: '',
        employeeId: '',
        name: '',
        experience: 0,
        phone: ''
    });

    const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
    const [employeeToDelete, setEmployeeToDelete] = useState<Employee | null>(null);

    const [showWorkingHoursForm, setShowWorkingHoursForm] = useState(false);
    const [selectedEmployeeForHours, setSelectedEmployeeForHours] = useState<Employee | null>(null);
    const [workingHoursForm, setWorkingHoursForm] = useState<WorkingHoursForm>({
        employeeId: '',
        days: Array.from({ length: 7 }, (_, i) => ({
            day: i + 1,
            startTime: '09:00',
            endTime: '17:00'
        }))
    });

    useEffect(() => {
        fetchMySalons();
    }, []);

    useEffect(() => {
        if (selectedSalonId) {
            fetchEmployees(selectedSalonId);
        }
    }, [selectedSalonId]);

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
            // Add salon name to each employee
            const salonName = salons.find(salon => salon.id === salonId)?.name || '';
            const employeesWithSalon = data.map((emp: Employee) => ({
                ...emp,
                salonName
            }));
            setEmployees(employeesWithSalon);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch employees');
        } finally {
            setLoading(false);
        }
    };

    const handleSalonChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        setSelectedSalonId(e.target.value);
        setCurrentPage(0); // Reset pagination when changing salon
        setSearch(''); // Reset search when changing salon
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'experience' ? parseInt(value) || 0 : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            const response = await fetch(`http://localhost:8090/api/v1/beautySalon/${formData.salonId}/employees`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({
                    name: formData.name,
                    experience: formData.experience,
                    phone: formData.phone
                })
            });

            if (!response.ok) {
                throw new Error('Failed to add employee');
            }

            // Reset form and hide it
            setFormData({
                name: '',
                experience: 0,
                phone: '',
                salonId: ''
            });
            setShowForm(false);
            
            // Refresh employees list if we're viewing the same salon
            if (selectedSalonId === formData.salonId) {
                fetchEmployees(selectedSalonId);
            }
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to add employee');
        } finally {
            setLoading(false);
        }
    };

    // Search and pagination logic
    const filteredEmployees = useMemo(() => {
        return employees.filter(employee =>
            employee.name.toLowerCase().includes(search.toLowerCase()) ||
            employee.phone.toLowerCase().includes(search.toLowerCase())
        );
    }, [employees, search]);

    const paginatedEmployees = useMemo(() => {
        const start = currentPage * pageSize;
        const end = start + pageSize;
        return filteredEmployees.slice(start, end);
    }, [filteredEmployees, currentPage]);

    const totalPages = Math.ceil(filteredEmployees.length / pageSize);

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value);
        setCurrentPage(0);
    };

    const handlePageChange = (newPage: number) => {
        setCurrentPage(newPage);
    };

    const handleDeleteSelect = async (e: React.FormEvent) => {
        e.preventDefault();
        const selectedEmployee = employees.find(emp => emp.id === deleteData.employeeId);
        if (selectedEmployee) {
            setEmployeeToDelete(selectedEmployee);
            setShowDeleteConfirmation(true);
        }
    };

    const handleDeleteConfirm = async () => {
        try {
            setLoading(true);
            const response = await fetch(
                `http://localhost:8090/api/v1/beautySalon/${selectedSalonId}/employees/${deleteData.employeeId}`,
                {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }
            );

            if (!response.ok) {
                throw new Error('Failed to delete employee');
            }

            // Reset form and hide modals
            setDeleteData({
                salonId: '',
                employeeId: ''
            });
            setShowDeleteModal(false);
            setShowDeleteConfirmation(false);
            setEmployeeToDelete(null);

            // Refresh employees list
            fetchEmployees(selectedSalonId);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to delete employee');
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteInputChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const { name, value } = e.target;
        setDeleteData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleUpdateInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setUpdateData(prev => ({
            ...prev,
            [name]: name === 'experience' ? parseInt(value) || 0 : value
        }));

        // If selecting an employee, populate the form with their data
        if (name === 'employeeId' && value) {
            const selectedEmployee = employees.find(emp => emp.id === value);
            if (selectedEmployee) {
                setUpdateData(prev => ({
                    ...prev,
                    name: selectedEmployee.name,
                    experience: selectedEmployee.experience,
                    phone: selectedEmployee.phone
                }));
            }
        }
    };

    const handleUpdateEmployee = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            const response = await fetch(
                `http://localhost:8090/api/v1/beautySalon/${updateData.salonId}/employees/${updateData.employeeId}`,
                {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                    body: JSON.stringify({
                        name: updateData.name,
                        experience: updateData.experience,
                        phone: updateData.phone
                    })
                }
            );

            if (!response.ok) {
                throw new Error('Failed to update employee');
            }

            // Reset form and hide modal
            setUpdateData({
                salonId: '',
                employeeId: '',
                name: '',
                experience: 0,
                phone: ''
            });
            setShowUpdateModal(false);

            // Refresh employees list if we're viewing the same salon
            if (selectedSalonId === updateData.salonId) {
                fetchEmployees(selectedSalonId);
            }
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to update employee');
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

    const handleUpdateClick = () => {
        if (!selectedSalonId) {
            setError('Please select a salon first');
            return;
        }
        setUpdateData(prev => ({ ...prev, salonId: selectedSalonId }));
        setShowUpdateModal(true);
        setError('');
    };

    const handleDeleteClick = () => {
        if (!selectedSalonId) {
            setError('Please select a salon first');
            return;
        }
        setDeleteData(prev => ({ ...prev, salonId: selectedSalonId }));
        setShowDeleteModal(true);
        setError('');
    };

    const handleWorkingHoursClick = (employee: Employee) => {
        setSelectedEmployeeForHours(employee);
        setWorkingHoursForm(prev => ({
            ...prev,
            employeeId: employee.id,
            days: Array.from({ length: 7 }, (_, i) => ({
                day: i + 1,
                startTime: '09:00',
                endTime: '17:00'
            }))
        }));
        setShowWorkingHoursForm(true);
    };

    const handleWorkingHoursChange = (dayIndex: number, field: 'startTime' | 'endTime', value: string) => {
        setWorkingHoursForm(prev => ({
            ...prev,
            days: prev.days.map((day, index) => 
                index === dayIndex ? { ...day, [field]: value } : day
            )
        }));
    };

    const handleWorkingHoursSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedEmployeeForHours) return;

        try {
            setLoading(true);
            setError('');

            // Send requests for each day
            const promises = workingHoursForm.days.map(day => 
                fetch(`http://localhost:8090/api/v1/working-hours/${selectedEmployeeForHours.id}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    },
                    body: JSON.stringify({
                        startTime: day.startTime,
                        endTime: day.endTime,
                        day: day.day
                    })
                })
            );

            await Promise.all(promises);
            setShowWorkingHoursForm(false);
            setSelectedEmployeeForHours(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to set working hours');
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
                        <h1>Manage Employees</h1>
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
                                Add Employee
                            </button>
                            <button 
                                className="button-secondary"
                                onClick={handleUpdateClick}
                            >
                                Update Employee
                            </button>
                            <button 
                                className="button-secondary"
                                onClick={handleDeleteClick}
                            >
                                Delete Employee
                            </button>
                        </div>
                    </div>

                    <div className="search-container">
                        <input
                            type="text"
                            placeholder="Search employees..."
                            value={search}
                            onChange={handleSearchChange}
                            className="search-input"
                        />
                    </div>

                    <div className="table-container">
                        <table className="employees-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Experience</th>
                                    <th>Phone</th>
                                    <th>Salon</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {paginatedEmployees.map((employee) => (
                                    <tr key={employee.id} className="employee-row">
                                        <td>{employee.name}</td>
                                        <td>{employee.experience} years</td>
                                        <td>{employee.phone}</td>
                                        <td>{employee.salonName}</td>
                                        <td>
                                            <div className="action-buttons">
                                                <button 
                                                    className="button-secondary"
                                                    onClick={() => handleWorkingHoursClick(employee)}
                                                >
                                                    Set Working Hours
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                                {paginatedEmployees.length === 0 && (
                                    <tr>
                                        <td colSpan={4} className="no-results">
                                            No employees found
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>

                    {filteredEmployees.length > pageSize && (
                        <div className="pagination">
                            <button
                                onClick={() => handlePageChange(currentPage - 1)}
                                disabled={currentPage === 0}
                                className="pagination-button"
                            >
                                Previous
                            </button>
                            <span className="pagination-info">
                                Page {currentPage + 1} of {totalPages} ({filteredEmployees.length} items)
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
                                <h2>Add New Employee</h2>
                                <form onSubmit={handleSubmit} className="add-employee-form">
                                    <div className="form-group">
                                        <label htmlFor="name">Employee Name</label>
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
                                        <label htmlFor="experience">Years of Experience</label>
                                        <input
                                            type="number"
                                            id="experience"
                                            name="experience"
                                            min="0"
                                            value={formData.experience}
                                            onChange={handleInputChange}
                                            required
                                        />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="phone">Phone Number</label>
                                        <input
                                            type="tel"
                                            id="phone"
                                            name="phone"
                                            value={formData.phone}
                                            onChange={handleInputChange}
                                            required
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
                                            {loading ? 'Adding...' : 'Add Employee'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}

                    {showUpdateModal && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h2>Update Employee Details</h2>
                                <form onSubmit={handleUpdateEmployee} className="update-employee-form">
                                    <div className="form-group">
                                        <label htmlFor="updateEmployeeId">Select Employee</label>
                                        <select
                                            id="updateEmployeeId"
                                            name="employeeId"
                                            value={updateData.employeeId}
                                            onChange={handleUpdateInputChange}
                                            required
                                        >
                                            <option value="">Select an employee</option>
                                            {employees
                                                .filter(emp => emp.salonName === salons.find(s => s.id === selectedSalonId)?.name)
                                                .map(employee => (
                                                    <option key={employee.id} value={employee.id}>
                                                        {employee.name}
                                                    </option>
                                                ))}
                                        </select>
                                    </div>

                                    {updateData.employeeId && (
                                        <>
                                            <div className="form-group">
                                                <label htmlFor="updateName">Employee Name</label>
                                                <input
                                                    type="text"
                                                    id="updateName"
                                                    name="name"
                                                    value={updateData.name}
                                                    onChange={handleUpdateInputChange}
                                                    required
                                                />
                                            </div>

                                            <div className="form-group">
                                                <label htmlFor="updateExperience">Years of Experience</label>
                                                <input
                                                    type="number"
                                                    id="updateExperience"
                                                    name="experience"
                                                    min="0"
                                                    value={updateData.experience}
                                                    onChange={handleUpdateInputChange}
                                                    required
                                                />
                                            </div>

                                            <div className="form-group">
                                                <label htmlFor="updatePhone">Phone Number</label>
                                                <input
                                                    type="tel"
                                                    id="updatePhone"
                                                    name="phone"
                                                    value={updateData.phone}
                                                    onChange={handleUpdateInputChange}
                                                    required
                                                />
                                            </div>
                                        </>
                                    )}

                                    <div className="form-actions">
                                        <button 
                                            type="button" 
                                            className="button-secondary"
                                            onClick={() => setShowUpdateModal(false)}
                                        >
                                            Cancel
                                        </button>
                                        <button 
                                            type="submit" 
                                            className="button-primary"
                                            disabled={loading || !updateData.employeeId}
                                        >
                                            {loading ? 'Updating...' : 'Update Employee'}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}

                    {showDeleteModal && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h2>Delete Employee</h2>
                                <form onSubmit={handleDeleteSelect} className="delete-employee-form">
                                    <div className="form-group">
                                        <label htmlFor="deleteEmployeeId">Select Employee</label>
                                        <select
                                            id="deleteEmployeeId"
                                            name="employeeId"
                                            value={deleteData.employeeId}
                                            onChange={handleDeleteInputChange}
                                            required
                                        >
                                            <option value="">Select an employee</option>
                                            {employees
                                                .filter(emp => emp.salonName === salons.find(s => s.id === selectedSalonId)?.name)
                                                .map(employee => (
                                                    <option key={employee.id} value={employee.id}>
                                                        {employee.name}
                                                    </option>
                                                ))}
                                        </select>
                                    </div>

                                    <div className="form-actions">
                                        <button 
                                            type="button" 
                                            className="button-secondary"
                                            onClick={() => setShowDeleteModal(false)}
                                        >
                                            Cancel
                                        </button>
                                        <button 
                                            type="submit" 
                                            className="button-danger"
                                            disabled={loading || !deleteData.employeeId}
                                        >
                                            Continue
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )}

                    {showDeleteConfirmation && employeeToDelete && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <div className="delete-confirmation">
                                    <h2>Confirm Delete</h2>
                                    <p className="delete-warning">
                                        Are you sure you want to delete the employee:
                                    </p>
                                    <div className="employee-details">
                                        <p><strong>Name:</strong> {employeeToDelete.name}</p>
                                        <p><strong>Experience:</strong> {employeeToDelete.experience} years</p>
                                        <p><strong>Phone:</strong> {employeeToDelete.phone}</p>
                                        <p><strong>Salon:</strong> {employeeToDelete.salonName}</p>
                                    </div>
                                    <p className="delete-warning-text">
                                        This action cannot be undone.
                                    </p>
                                    <div className="form-actions">
                                        <button 
                                            type="button" 
                                            className="button-secondary"
                                            onClick={() => {
                                                setShowDeleteConfirmation(false);
                                                setEmployeeToDelete(null);
                                            }}
                                        >
                                            Cancel
                                        </button>
                                        <button 
                                            type="button" 
                                            className="button-danger"
                                            onClick={handleDeleteConfirm}
                                            disabled={loading}
                                        >
                                            {loading ? 'Deleting...' : 'Delete Employee'}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    )}

                    {showWorkingHoursForm && selectedEmployeeForHours && (
                        <div className="modal-overlay">
                            <div className="modal-content">
                                <h2>Set Working Hours for {selectedEmployeeForHours.name}</h2>
                                <form onSubmit={handleWorkingHoursSubmit} className="working-hours-form">
                                    {workingHoursForm.days.map((day, index) => (
                                        <div key={day.day} className="form-group">
                                            <label>{['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'][index]}</label>
                                            <div className="time-inputs">
                                                <input
                                                    type="time"
                                                    value={day.startTime}
                                                    onChange={(e) => handleWorkingHoursChange(index, 'startTime', e.target.value)}
                                                    required
                                                />
                                                <span>to</span>
                                                <input
                                                    type="time"
                                                    value={day.endTime}
                                                    onChange={(e) => handleWorkingHoursChange(index, 'endTime', e.target.value)}
                                                    required
                                                />
                                            </div>
                                        </div>
                                    ))}

                                    <div className="form-actions">
                                        <button 
                                            type="button" 
                                            className="button-secondary"
                                            onClick={() => setShowWorkingHoursForm(false)}
                                        >
                                            Cancel
                                        </button>
                                        <button 
                                            type="submit" 
                                            className="button-primary"
                                            disabled={loading}
                                        >
                                            {loading ? 'Saving...' : 'Save Working Hours'}
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