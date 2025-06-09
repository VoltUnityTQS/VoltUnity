import React, { useEffect, useState } from 'react';
import { Card, Button, ListGroup, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { api } from '../../services/api';

const LoginPage = () => {
    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        async function fetchUsers() {
            try {
                const response = await api.get('/users');
                setUsers(response.data);
            } catch (error) {
                console.error('Erro ao buscar utilizadores:', error);
            }
        }

        fetchUsers();
    }, []);

    const handleLogin = (user) => {
        // Guardar em selectedUser → para o AuthGuard funcionar
        localStorage.setItem('selectedUser', JSON.stringify(user));

        // REDIRECIONA conforme role:
        if (user.role === 'ADMIN') {
            navigate('/app/dashboard/default');
        } else {
            navigate('/user-main');
        }
    };

    return (
        <div style={{ maxWidth: '600px', margin: '50px auto' }}>
            <Card>
                <Card.Body>
                    <Card.Title>Escolha o utilizador para login</Card.Title>
                    <ListGroup>
                        {users.map(user => (
                            <ListGroup.Item
                                key={user.id}
                                className="d-flex justify-content-between align-items-center"
                            >
                                <span>{user.name} ({user.email})</span>
                                <div>
                                    {user.role === 'ADMIN' && (
                                        <Badge bg="danger" className="me-2">ADMIN</Badge>
                                    )}
                                    <Button variant="primary" onClick={() => handleLogin(user)}>
                                        Entrar
                                    </Button>
                                </div>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                </Card.Body>
            </Card>
        </div>
    );
};

export default LoginPage;