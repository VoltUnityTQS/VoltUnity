import React, { useEffect, useState } from 'react';
import { Card, Button, Table, Form, Row, Col } from 'react-bootstrap';
import { getUsers, addUser, deleteUser } from '../../services/api';

const ManageUsers = () => {
    const [users, setUsers] = useState([]);
    const [newUser, setNewUser] = useState({
        name: '',
        email: '',
        password: '',
        role: 'USER'
    });

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        const data = await getUsers();
        setUsers(data);
    };

    const handleAddUser = async () => {
        await addUser(newUser);
        setNewUser({ name: '', email: '', password: '', role: 'USER' });
        fetchUsers();
    };

    const handleDeleteUser = async (userId) => {
        await deleteUser(userId);
        fetchUsers();
    };

    return (
        <Card>
            <Card.Header>
                <Card.Title as="h5">Gerir Utilizadores</Card.Title>
            </Card.Header>
            <Card.Body>
                <h6>Adicionar novo utilizador:</h6>
                <Row className="mb-3">
                    {['name', 'email', 'password'].map(field => (
                        <Col key={field}>
                            <Form.Control
                                placeholder={field}
                                value={newUser[field]}
                                onChange={(e) => setNewUser({ ...newUser, [field]: e.target.value })}
                            />
                        </Col>
                    ))}
                    <Col>
                        <Form.Select
                            value={newUser.role}
                            onChange={(e) => setNewUser({ ...newUser, role: e.target.value })}
                        >
                            <option value="USER">USER</option>
                            <option value="ADMIN">ADMIN</option>
                        </Form.Select>
                    </Col>
                    <Col>
                        <Button variant="primary" onClick={handleAddUser}>
                            Adicionar Utilizador
                        </Button>
                    </Col>
                </Row>

                <h6 className="mt-4">Lista de utilizadores:</h6>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.name}</td>
                                <td>{user.email}</td>
                                <td>{user.role}</td>
                                <td>
                                    <Button
                                        variant="danger"
                                        size="sm"
                                        onClick={() => handleDeleteUser(user.id)}
                                    >
                                        Remover
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Card.Body>
        </Card>
    );
};

export default ManageUsers;