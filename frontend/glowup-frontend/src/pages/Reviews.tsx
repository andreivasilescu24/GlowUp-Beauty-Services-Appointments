import { useState, useEffect } from 'react';
import { Layout } from '../components/layout/Layout';
import { beautySalonService } from '../services/beautySalonService';
import type { BeautySalon } from '../types/beautySalon';
import '../styles/pages.css';

interface ReviewForm {
    salonId: string;
    comment: string;
    rating: number;
    recommendToFriends: boolean;
    visitFrequency: string;
}

export const Reviews = () => {
    const [salons, setSalons] = useState<BeautySalon[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [formData, setFormData] = useState<ReviewForm>({
        salonId: '',
        comment: '',
        rating: 5,
        recommendToFriends: false,
        visitFrequency: 'monthly'
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

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError('');

            const response = await fetch(`http://localhost:8090/api/v1/beautySalon/${formData.salonId}/reviews`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({
                    clientId: localStorage.getItem('userId'), // Assuming you store userId in localStorage
                    comment: formData.comment,
                    rating: formData.rating
                })
            });

            if (!response.ok) {
                throw new Error('Failed to submit review');
            }

            // Reset form
            setFormData({
                salonId: '',
                comment: '',
                rating: 5,
                recommendToFriends: false,
                visitFrequency: 'monthly'
            });
            setShowForm(false);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to submit review');
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
                        <h1>Write a Review</h1>
                        <p className="page-description">
                            Share your experience with our beauty salons
                        </p>
                    </div>

                    <div className="review-form-container">
                        <form onSubmit={handleSubmit} className="review-form">
                            <div className="form-group">
                                <label htmlFor="salonId">Select Beauty Salon</label>
                                <select
                                    id="salonId"
                                    name="salonId"
                                    value={formData.salonId}
                                    onChange={handleInputChange}
                                    required
                                    className="form-select"
                                >
                                    <option value="">Choose a salon</option>
                                    {salons.map(salon => (
                                        <option key={salon.id} value={salon.id}>
                                            {salon.name}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div className="form-group">
                                <label>Rating</label>
                                <div className="rating-options">
                                    {[1, 2, 3, 4, 5].map((rating) => (
                                        <label key={rating} className="rating-label" data-rating={rating}>
                                            <input
                                                type="radio"
                                                name="rating"
                                                value={rating}
                                                checked={formData.rating === rating}
                                                onChange={handleInputChange}
                                                required
                                            />
                                            <span className="rating-star">★</span>
                                        </label>
                                    ))}
                                </div>
                            </div>

                            <div className="form-group">
                                <label>How often do you visit?</label>
                                <div className="radio-group">
                                    <label className="radio-label">
                                        <input
                                            type="radio"
                                            name="visitFrequency"
                                            value="weekly"
                                            checked={formData.visitFrequency === 'weekly'}
                                            onChange={handleInputChange}
                                        />
                                        Weekly
                                    </label>
                                    <label className="radio-label">
                                        <input
                                            type="radio"
                                            name="visitFrequency"
                                            value="monthly"
                                            checked={formData.visitFrequency === 'monthly'}
                                            onChange={handleInputChange}
                                        />
                                        Monthly
                                    </label>
                                    <label className="radio-label">
                                        <input
                                            type="radio"
                                            name="visitFrequency"
                                            value="occasionally"
                                            checked={formData.visitFrequency === 'occasionally'}
                                            onChange={handleInputChange}
                                        />
                                        Occasionally
                                    </label>
                                </div>
                            </div>

                            <div className="form-group">
                                <label className="checkbox-label">
                                    <input
                                        type="checkbox"
                                        name="recommendToFriends"
                                        checked={formData.recommendToFriends}
                                        onChange={handleInputChange}
                                    />
                                    I would recommend this salon to friends
                                </label>
                            </div>

                            <div className="form-group">
                                <label htmlFor="comment">Your Review</label>
                                <textarea
                                    id="comment"
                                    name="comment"
                                    value={formData.comment}
                                    onChange={handleInputChange}
                                    required
                                    placeholder="Share your experience..."
                                    className="form-textarea"
                                />
                            </div>

                            <div className="form-actions">
                                <button 
                                    type="submit" 
                                    className="button-primary"
                                    disabled={loading}
                                >
                                    {loading ? 'Submitting...' : 'Submit Review'}
                                </button>
                            </div>
                        </form>
                    </div>

                    {error && <div className="error-message">{error}</div>}
                </div>
            </div>
        </Layout>
    );
}; 