import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Register } from './components/auth/Register';
import { Login } from './components/auth/Login';
import { Categories } from './components/categories/Categories';
import { Layout } from './components/layout/Layout';
import { Home } from './pages/Home';
import { BeautySalons } from './pages/BeautySalons';
import { MyAppointments } from './pages/MyAppointments';
import { FavoriteSalons } from './pages/FavoriteSalons';
import { MySalons } from './pages/MySalons';
import { SalonAppointments } from './pages/SalonAppointments';
import { ManageEmployees } from './pages/ManageEmployees';
import { ProtectedRoute } from './components/auth/ProtectedRoute';
import { BeautyServices } from './pages/BeautyServices';
import { Reviews } from './pages/Reviews';
import './App.css';

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
            <Layout>
              <Home />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/salons" element={
          <ProtectedRoute>
            <Layout>
              <BeautySalons />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/categories" element={
          <ProtectedRoute>
            <Layout>
              <Categories />
            </Layout>
          </ProtectedRoute>
        } />

        {/* Protected Routes - Users Only */}
        <Route path="/appointments" element={
          <ProtectedRoute allowedRoles={['USER']}>
            <Layout>
              <MyAppointments />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/favorites" element={
          <ProtectedRoute allowedRoles={['USER']}>
            <Layout>
              <FavoriteSalons />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/reviews" element={
          <ProtectedRoute allowedRoles={['USER']}>
            <Layout>
              <Reviews />
            </Layout>
          </ProtectedRoute>
        } />

        {/* Protected Routes - Owners Only */}
        <Route path="/my-salons" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <Layout>
              <MySalons />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/salon-appointments" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <Layout>
              <SalonAppointments />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/employees" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <Layout>
              <ManageEmployees />
            </Layout>
          </ProtectedRoute>
        } />
        <Route path="/beauty-services" element={
          <ProtectedRoute allowedRoles={['OWNER']}>
            <Layout>
              <BeautyServices />
            </Layout>
          </ProtectedRoute>
        } />

        {/* Default Route */}
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
