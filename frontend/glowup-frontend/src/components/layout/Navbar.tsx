import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../../services/authService';
import './Navbar.css';

export const Navbar = () => {
    const navigate = useNavigate();
    const userRole = authService.getUserRole();

    const handleLogout = () => {
        authService.logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <Link to="/">
                    <span className="brand-name">GlowUp</span>
                </Link>
            </div>
            
            <div className="navbar-links">
                {/* Common Links */}
                <Link to="/home" className="nav-link">Home</Link>
                <Link to="/salons" className="nav-link">Beauty Salons</Link>
                <Link to="/categories" className="nav-link">Categories</Link>

                {/* User-specific Links */}
                {userRole === 'USER' && (
                    <>
                        <Link to="/appointments" className="nav-link">My Appointments</Link>
                    </>
                )}

                {/* Owner-specific Links */}
                {userRole === 'OWNER' && (
                    <>
                        <Link to="/my-salons" className="nav-link">My Salons</Link>
                        <Link to="/salon-appointments" className="nav-link">Salon Appointments</Link>
                        <Link to="/employees" className="nav-link">Manage Employees</Link>
                    </>
                )}
            </div>

            <div className="navbar-end">
                <button onClick={handleLogout} className="logout-button">
                    Logout
                </button>
            </div>
        </nav>
    );
}; 