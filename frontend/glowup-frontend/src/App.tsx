import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Register } from './components/auth/Register';
import { Login } from './components/auth/Login';
import { Home } from './pages/Home';
import { BeautySalons } from './pages/BeautySalons';
import { Categories } from './pages/Categories';
import { MyAppointments } from './pages/MyAppointments';
import { FavoriteSalons } from './pages/FavoriteSalons';
import { MySalons } from './pages/MySalons';
import { SalonAppointments } from './pages/SalonAppointments';
import { ManageEmployees } from './pages/ManageEmployees';
import type { UserRole } from './types/auth';
import './App.css';

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: UserRole[];
}

const ProtectedRoute = ({ children, allowedRoles }: ProtectedRouteProps) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('user_role') as UserRole;

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(userRole)) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
};

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        
        {/* Protected Routes - All Users */}
        <Route path="/home" element={
          <ProtectedRoute>
            <Home />
          </ProtectedRoute>
        } />
        <Route path="/salons" element={
          <ProtectedRoute>
            <BeautySalons />
          </ProtectedRoute>
        } />
        <Route path="/categories" element={
          <ProtectedRoute>
            <Categories />
          </ProtectedRoute>
        } />

        {/* Protected Routes - Users Only */}
        <Route path="/appointments" element={
          <ProtectedRoute allowedRoles={['USER']}>
            <MyAppointments />
          </ProtectedRoute>
        } />
        <Route path="/favorites" element={
          <ProtectedRoute allowedRoles={['USER']}>
            <FavoriteSalons />
          </ProtectedRoute>
        } />

        {/* Protected Routes - Owners Only */}
        <Route path="/my-salons" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <MySalons />
          </ProtectedRoute>
        } />
        <Route path="/salon-appointments" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <SalonAppointments />
          </ProtectedRoute>
        } />
        <Route path="/employees" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <ManageEmployees />
          </ProtectedRoute>
        } />
      </Routes>
    </Router>
  );
}

export default App;
