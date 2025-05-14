import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../../services/authService';
import type { UserRole } from '../../types/auth';
import './Auth.css';

export const Register = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        username: '',
        password: '',
        role: 'USER' as UserRole,
    });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        setError('');
        
        try {
            console.log('Sending registration request:', formData);
            const response = await authService.register(formData);
            console.log('Registration successful:', response);
            navigate('/login');
        } catch (err) {
            console.error('Registration error:', err);
            setError(err instanceof Error ? err.message : 'Registration failed');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-box">
                <div className="auth-left">
                    <h2>Join <span className="brand-name">GlowUp</span></h2>
                    <p className="welcome-text">
                        Start your journey to radiant beauty. Create an account to book appointments, 
                        manage your salon, or discover premium beauty services.
                    </p>
                </div>
                <div className="auth-right">
                    {error && <div className="error-message">{error}</div>}
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                                placeholder="Enter your email"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="username">Username</label>
                            <input
                                type="text"
                                id="username"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                                placeholder="Choose a username"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                                placeholder="Choose a password"
                                minLength={6}
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="role">I want to</label>
                            <select
                                id="role"
                                name="role"
                                value={formData.role}
                                onChange={handleChange}
                                required
                                className="role-select"
                            >
                                <option value="USER">Book beauty services</option>
                                <option value="OWNER">Manage my beauty salon</option>
                            </select>
                        </div>
                        <button 
                            type="submit" 
                            className="auth-button"
                            disabled={isLoading}
                        >
                            {isLoading ? 'Creating Account...' : 'Create Account'}
                        </button>
                        <p className="auth-link">
                            Already have an account?<span onClick={() => navigate('/login')}>Sign in</span>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    );
}; 