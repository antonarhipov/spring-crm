import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { GlobalStyle } from '@styles/global'
import { RootState } from '@store/index'
import { UserRole } from '@models'
import MainLayout from '@components/layout/MainLayout'
const LoginPage = React.lazy(() => import('@pages/Login'))
import LoadingSpinner from '@components/common/LoadingSpinner'

const DashboardPage = React.lazy(() => import('@pages/Dashboard'))
const CustomersPage = React.lazy(() => import('@pages/Customers'))
const CustomerDetailsPage = React.lazy(() => import('@pages/CustomerDetails'))
const UsersPage = React.lazy(() => import('@pages/Users'))
const SettingsPage = React.lazy(() => import('@pages/Settings'))

const PrivateRoute: React.FC<{ element: React.ReactElement }> = ({ element }) => {
  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated)
  return isAuthenticated ? element : <Navigate to="/login" replace />
}

const AdminRoute: React.FC<{ element: React.ReactElement }> = ({ element }) => {
  const { isAuthenticated, user } = useSelector((state: RootState) => state.auth)
  const isAdmin = user.roles.includes(UserRole.ADMIN)
  return isAuthenticated && isAdmin ? element : <Navigate to="/" replace />
}

const App: React.FC = () => {
  return (
    <>
      <GlobalStyle />
      <React.Suspense fallback={<LoadingSpinner />}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<PrivateRoute element={<MainLayout />} />}>
            <Route index element={<Navigate to="/dashboard" replace />} />
            <Route path="dashboard" element={<DashboardPage />} />
            <Route path="customers" element={<CustomersPage />} />
            <Route path="customers/:id" element={<CustomerDetailsPage />} />
            <Route path="users" element={<AdminRoute element={<UsersPage />} />} />
            <Route path="settings" element={<SettingsPage />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      </React.Suspense>
    </>
  )
}

export default App
