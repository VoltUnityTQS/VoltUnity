// src/routes.jsx

import React, { Suspense, Fragment, lazy } from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';

import Loader from './components/Loader/Loader';
import AdminLayout from './layouts/AdminLayout';
import { BASE_URL } from './config/constant';

import AccountPage from './views/my-pages/AccountPage';

const AuthGuard = ({ children }) => {
    const location = useLocation();
    const user = localStorage.getItem('selectedUser');

    if (!user) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    return children;
};

export const renderRoutes = (routes = []) => (
    <Suspense fallback={<Loader />}>
        <Routes>
            {routes.map((route, i) => {
                const Guard = route.guard || Fragment;
                const Layout = route.layout || Fragment;
                const Element = route.element;

                return (
                    <Route
                        key={i}
                        path={route.path}
                        element={
                            <Guard>
                                <Layout>
                                    {route.routes ? renderRoutes(route.routes) : <Element props={true} />}
                                </Layout>
                            </Guard>
                        }
                    />
                );
            })}
        </Routes>
    </Suspense>
);

const routes = [
    // Root -> Login
    {
        exact: 'true',
        path: '/',
        element: () => <Navigate to="/login" replace />
    },

    // Login
    {
        exact: 'true',
        path: '/login',
        element: lazy(() => import('./views/my-pages/LoginPage'))
    },

    // Authenticated routes
    {
        path: '*',
        guard: AuthGuard,
        layout: AdminLayout,
        routes: [
            // Admin dashboard
            {
                exact: 'true',
                path: '/app/dashboard/default',
                element: lazy(() => import('./views/dashboard'))
            },

            // User dashboard
            {
                exact: 'true',
                path: '/user-dashboard',
                element: lazy(() => import('./views/my-pages/user-dashboard'))
            },

            // Common to both roles
            {
                exact: 'true',
                path: '/user-main',
                element: lazy(() => import('./views/my-pages/user-main')),
                title: 'Estações'
            },
            {
                exact: 'true',
                path: '/station/:id',
                element: lazy(() => import('./views/my-pages/station-page')),
                title: 'Estação'
            },
            {
                exact: 'true',
                path: '/account',
                element: AccountPage,
                title: 'Conta'
            },

            // Admin-only pages
            {
                exact: 'true',
                path: '/manage-users',
                element: lazy(() => import('./views/my-pages/manage-users')),
                title: 'Gerir Utilizadores'
            },
            {
                exact: 'true',
                path: '/manage-stations',
                element: lazy(() => import('./views/my-pages/manage-stations')),
                title: 'Gerir Estações'
            },
            {
                exact: 'true',
                path: '/manage-cars',
                element: lazy(() => import('./views/my-pages/manage-cars.jsx')),
                title: 'Gerir Carros'
            },

            // Fallback
            {
                path: '*',
                exact: 'true',
                element: () => <Navigate to={BASE_URL} replace />
            }
        ]
    }
];

export default routes;