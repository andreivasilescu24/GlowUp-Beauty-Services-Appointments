import { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { beautySalonService } from '../../services/beautySalonService';
import type { BeautySalon } from '../../types/beautySalon';
import './BeautySalons.css';

export const BeautySalons = () => {
    const navigate = useNavigate();
    const [salons, setSalons] = useState<BeautySalon[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 5;

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

    useEffect(() => {
        fetchSalons();
    }, []);

    const handleSalonClick = (salonId: string) => {
        // TODO: Implement navigation to salon details
        console.log('Salon clicked:', salonId);
    };

    // Client-side search
    const filteredSalons = useMemo(() => {
        return salons.filter(salon => 
            salon.name.toLowerCase().includes(search.toLowerCase()) ||
            salon.address.toLowerCase().includes(search.toLowerCase()) ||
            salon.city.toLowerCase().includes(search.toLowerCase()) ||
            salon.email.toLowerCase().includes(search.toLowerCase()) ||
            salon.phone.toLowerCase().includes(search.toLowerCase())
        );
    }, [salons, search]);

    // Client-side pagination
    const paginatedSalons = useMemo(() => {
        const start = currentPage * pageSize;
        const end = start + pageSize;
        return filteredSalons.slice(start, end);
    }, [filteredSalons, currentPage]);

    const totalPages = Math.ceil(filteredSalons.length / pageSize);

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value);
        setCurrentPage(0);
    };

    const handlePageChange = (newPage: number) => {
        setCurrentPage(newPage);
    };

    if (loading && !salons.length) {
        return <div className="salons-loading">Loading beauty salons...</div>;
    }

    if (error && !salons.length) {
        return <div className="salons-error">{error}</div>;
    }

    return (
        <div className="salons-container">
            <div className="salons-header">
                <h1>Beauty Salons</h1>
                <div className="search-container">
                    <input
                        type="text"
                        placeholder="Search beauty salons..."
                        value={search}
                        onChange={handleSearchChange}
                        className="search-input"
                    />
                </div>
            </div>

            <div className="table-container">
                <table className="salons-table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Address</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Employees</th>
                            <th>City</th>
                        </tr>
                    </thead>
                    <tbody>
                        {paginatedSalons.map((salon) => (
                            <tr 
                                key={salon.id}
                                className="salon-row"
                                onClick={() => handleSalonClick(salon.id)}
                                role="button"
                                tabIndex={0}
                            >
                                <td className="salon-name">{salon.name}</td>
                                <td className="salon-address">{salon.address}</td>
                                <td className="salon-email">{salon.email}</td>
                                <td className="salon-phone">{salon.phone}</td>
                                <td className="salon-employees">{salon.numEmployees}</td>
                                <td className="salon-city">{salon.city}</td>
                            </tr>
                        ))}
                        {paginatedSalons.length === 0 && (
                            <tr>
                                <td colSpan={6} className="no-results">
                                    No beauty salons found
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {filteredSalons.length > pageSize && (
                <div className="pagination">
                    <button
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 0}
                        className="pagination-button"
                    >
                        Previous
                    </button>
                    <span className="pagination-info">
                        Page {currentPage + 1} of {totalPages} ({filteredSalons.length} items)
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
        </div>
    );
}; 