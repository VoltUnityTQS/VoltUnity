import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiSettings, FiLogOut } from 'react-icons/fi';

const NavRight = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('selectedUser');
        navigate('/login');
    };

    return (
        <ul className="navbar-nav ml-auto">
            <li className="nav-item">
                <Link to="/account" className="nav-link">
                    <FiSettings size={20} /> Definições
                </Link>
            </li>
            <li className="nav-item">
                <button
                    onClick={handleLogout}
                    className="nav-link btn btn-link"
                    style={{ padding: 0 }}
                >
                    <FiLogOut size={20} /> Logout
                </button>
            </li>
        </ul>
    );
};

export default NavRight;