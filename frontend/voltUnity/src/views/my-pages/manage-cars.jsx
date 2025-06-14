import React, { useEffect, useState } from 'react';
import { Card, Button, Table, Form, Row, Col } from 'react-bootstrap';
import { getAllCars, addCarGlobal, deleteCar, getUsers } from '../../services/api';

const ManageCars = () => {
    const [cars, setCars] = useState([]);
    const [users, setUsers] = useState([]);
    const [carUserMap, setCarUserMap] = useState({});
    const [newCar, setNewCar] = useState({
        make: '',
        model: '',
        licensePlate: '',
        userId: ''
    });

    useEffect(() => {
        fetchCars();
        fetchUsers();
    }, []);

    const fetchCars = async () => {
        const data = await getAllCars();
        setCars(data);

        // Construir o map { carId: userId }
        const map = {};
        data.forEach(car => {
            if (car.user?.id) {
                map[car.id] = car.user.id;
            }
        });
        setCarUserMap(map);
    };

    const fetchUsers = async () => {
        const data = await getUsers();
        setUsers(data);
    };

    const handleAddCar = async () => {
        if (!newCar.userId) {
            alert('Por favor selecione um utilizador.');
            return;
        }

        await addCarGlobal(newCar);
        setNewCar({ make: '', model: '', licensePlate: '', userId: '' });
        fetchCars();
    };

    const handleDeleteCar = async (carId, userId) => {
        if (!userId) {
            alert('Erro: este carro não tem utilizador associado!');
            return;
        }

        await deleteCar(carId, userId);
        fetchCars();
    };

    return (
        <Card>
            <Card.Header>
                <Card.Title as="h5">Gerir Carros</Card.Title>
            </Card.Header>
            <Card.Body>
                <h6>Adicionar novo carro:</h6>
                <Row className="mb-3">
                    {['make', 'model', 'licensePlate'].map(field => (
                        <Col key={field}>
                            <Form.Control
                                placeholder={field}
                                value={newCar[field]}
                                onChange={(e) => setNewCar({ ...newCar, [field]: e.target.value })}
                            />
                        </Col>
                    ))}
                    <Col>
                        <Form.Select
                            value={newCar.userId}
                            onChange={(e) => setNewCar({ ...newCar, userId: e.target.value })}
                        >
                            <option value="">Selecionar utilizador</option>
                            {users.map(user => (
                                <option key={user.id} value={user.id}>
                                    {user.name} ({user.email})
                                </option>
                            ))}
                        </Form.Select>
                    </Col>
                    <Col>
                        <Button variant="primary" onClick={handleAddCar}>
                            Adicionar Carro
                        </Button>
                    </Col>
                </Row>

                <h6 className="mt-4">Lista de carros:</h6>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Marca</th>
                            <th>Modelo</th>
                            <th>Matrícula</th>
                            <th>Utilizador</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {cars.map(car => (
                            <tr key={car.id}>
                                <td>{car.id}</td>
                                <td>{car.make}</td>
                                <td>{car.model}</td>
                                <td>{car.licensePlate}</td>
                                <td>{car.user?.name}</td>
                                <td>
                                <Button
                                    variant="danger"
                                    size="sm"
                                    onClick={() => {
                                        
                                        const userId = carUserMap[car.id];
                                        
                                        handleDeleteCar(car.id, userId);
                                    }}
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

export default ManageCars;