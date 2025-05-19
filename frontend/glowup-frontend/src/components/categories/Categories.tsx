import { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { categoryService } from '../../services/categoryService';
import type { Category } from '../../types/category';
import './Categories.css';

export const Categories = () => {
    const navigate = useNavigate();
    const [categories, setCategories] = useState<Category[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [search, setSearch] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;

    const fetchCategories = async () => {
        try {
            setLoading(true);
            const response = await categoryService.getCategories();
            setCategories(response);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to fetch categories');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleCategoryClick = (categoryId: number) => {
        // TODO: Implement navigation to category details
        console.log('Category clicked:', categoryId);
    };

    // Client-side search
    const filteredCategories = useMemo(() => {
        return categories.filter(category => 
            category.name.toLowerCase().includes(search.toLowerCase())
        );
    }, [categories, search]);

    // Client-side pagination
    const paginatedCategories = useMemo(() => {
        const start = currentPage * pageSize;
        const end = start + pageSize;
        return filteredCategories.slice(start, end);
    }, [filteredCategories, currentPage]);

    const totalPages = Math.ceil(filteredCategories.length / pageSize);

    const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearch(e.target.value);
        setCurrentPage(0);
    };

    const handlePageChange = (newPage: number) => {
        setCurrentPage(newPage);
    };

    if (loading && !categories.length) {
        return <div className="categories-loading">Loading categories...</div>;
    }

    if (error && !categories.length) {
        return <div className="categories-error">{error}</div>;
    }

    return (
        <div className="categories-container">
            <div className="categories-header">
                <h1>Beauty Categories</h1>
                <div className="search-container">
                    <input
                        type="text"
                        placeholder="Search categories..."
                        value={search}
                        onChange={handleSearchChange}
                        className="search-input"
                    />
                </div>
            </div>

            <div className="table-container">
                <table className="categories-table">
                    <thead>
                        <tr>
                            <th>Category</th>
                        </tr>
                    </thead>
                    <tbody>
                        {paginatedCategories.map((category) => (
                            <tr 
                                key={category.id}
                                className="category-row"
                                onClick={() => handleCategoryClick(category.id)}
                                role="button"
                                tabIndex={0}
                            >
                                <td className="category-name">{category.name}</td>
                            </tr>
                        ))}
                        {paginatedCategories.length === 0 && (
                            <tr>
                                <td className="no-results">
                                    No categories found
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {filteredCategories.length > pageSize && (
                <div className="pagination">
                    <button
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 0}
                        className="pagination-button"
                    >
                        Previous
                    </button>
                    <span className="pagination-info">
                        Page {currentPage + 1} of {totalPages} ({filteredCategories.length} items)
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