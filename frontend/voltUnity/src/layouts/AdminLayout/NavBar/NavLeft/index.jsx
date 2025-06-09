import React from 'react';
import { ListGroup, Dropdown } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

import useWindowSize from '../../../../hooks/useWindowSize';
import NavSearch from './NavSearch';

const NavLeft = () => {
    const navigate = useNavigate();
    const windowSize = useWindowSize();

    const user = JSON.parse(localStorage.getItem('selectedUser'));
    const role = user?.role;

    const handleNavigate = (path) => {
        navigate(path);
    };

    const handleLogout = () => {
        localStorage.removeItem('selectedUser');
        navigate('/login');
    };

    return (
        <React.Fragment>
            <ListGroup as="ul" bsPrefix=" " className="navbar-nav mr-auto">
                <ListGroup.Item as="li" bsPrefix=" " className="nav-item">
                    <Dropdown align="start">
                        <Dropdown.Toggle variant={'link'} id="dropdown-menu-nav">
                            Menu
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                            {role === 'ADMIN' ? (
                                <>
                                    <Dropdown.Item onClick={() => handleNavigate('/app/dashboard/default')}>Dashboard</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/user-main')}>Estações</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/manage-users')}>Gerir Utilizadores</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/manage-stations')}>Gerir Estações</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/manage-cars')}>Gerir Carros</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/account')}>Conta</Dropdown.Item>
                                    <Dropdown.Divider />
                                    <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                                </>
                            ) : (
                                <>
                                    <Dropdown.Item onClick={() => handleNavigate('/user-dashboard')}>Dashboard</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/user-main')}>Estações</Dropdown.Item>
                                    <Dropdown.Item onClick={() => handleNavigate('/account')}>Conta</Dropdown.Item>
                                    <Dropdown.Divider />
                                    <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                                </>
                            )}
                        </Dropdown.Menu>
                    </Dropdown>
                </ListGroup.Item>

                <ListGroup.Item as="li" bsPrefix=" " className="nav-item">
                    <NavSearch windowWidth={windowSize.width} />
                </ListGroup.Item>
            </ListGroup>
        </React.Fragment>
    );
};

export default NavLeft;