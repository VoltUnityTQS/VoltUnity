import React, { useEffect, useState } from 'react';
import { Card, Button, Table, Form, Row, Col } from 'react-bootstrap';
import { getUserById, getCarsByUser, addCar, deleteCar } from '../../services/api';

const AccountPage = () => {
    const storedUser = JSON.parse(localStorage.getItem('selectedUser'));
    const userId = storedUser?.id;

    const [user, setUser] = useState(null);
    const [cars, setCars] = useState([]);
    const [newCar, setNewCar] = useState({
        make: '',
        model: '',
        licensePlate: ''
    });

    useEffect(() => {
        async function fetchData() {
            try {
                const userData = await getUserById(userId);
                setUser(userData);

                const carsData = await getCarsByUser(userId);
                setCars(carsData);
            } catch (error) {
                console.error('Erro ao carregar dados da conta:', error);
            }
        }

        if (userId) {
            fetchData();
        }
    }, [userId]); // <- importante: depende de userId

    const handleAddCar = async () => {
        try {
            const carPayload = {
                make: newCar.make,
                model: newCar.model,
                licensePlate: newCar.licensePlate,
                userId: user.id
            };
            await addCar(carPayload);
            const updatedCars = await getCarsByUser(user.id);
            setCars(updatedCars);
            setNewCar({ make: '', model: '', licensePlate: '' });
        } catch (error) {
            console.error('Erro ao adicionar carro:', error);
            if (error.response && error.response.data && error.response.data.message) {
                alert(error.response.data.message);
            } else {
                alert('Erro inesperado ao adicionar carro.');
            }
        }
    };

    const handleDeleteCar = async (carId) => {
        try {
            await deleteCar(carId, user.id);
            const updatedCars = await getCarsByUser(user.id);
            setCars(updatedCars);
        } catch (error) {
            console.error('Erro ao remover carro:', error);
        }
    };

    if (!user) return <div>A carregar dados do utilizador...</div>;

    return (
        <Card>
            <Card.Header>
                <Card.Title as="h5">Definições da Conta</Card.Title>
            </Card.Header>
            <Card.Body>
                <h6>Informações do Utilizador:</h6>
                <p><strong>Nome:</strong> {user.name}</p>
                <p><strong>Email:</strong> {user.email}</p>
                <p><strong>Role:</strong> {user.role}</p>

                <hr />

                <h6>Carros:</h6>
                <Table hover>
                    <thead>
                        <tr>
                            <th>Marca</th>
                            <th>Modelo</th>
                            <th>Matrícula</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {cars.map((car) => (
                            <tr key={car.id}>
                                <td>{car.make}</td>
                                <td>{car.model}</td>
                                <td>{car.licensePlate}</td>
                                <td>
                                    <Button variant="danger" size="sm" onClick={() => handleDeleteCar(car.id)}>
                                        Remover
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>

                <hr />

                <h6>Adicionar novo carro:</h6>
                <Row className="mb-3">
                    <Col>
                        <Form.Control
                            placeholder="Marca"
                            value={newCar.make}
                            onChange={(e) => setNewCar({ ...newCar, make: e.target.value })}
                        />
                    </Col>
                    <Col>
                        <Form.Control
                            placeholder="Modelo"
                            value={newCar.model}
                            onChange={(e) => setNewCar({ ...newCar, model: e.target.value })}
                        />
                    </Col>
                    <Col>
                        <Form.Control
                            placeholder="Matrícula"
                            value={newCar.licensePlate}
                            onChange={(e) => setNewCar({ ...newCar, licensePlate: e.target.value })}
                        />
                    </Col>
                    <Col>
                        <Button variant="primary" onClick={handleAddCar}>
                            Adicionar Carro
                        </Button>
                    </Col>
                </Row>
            </Card.Body>
        </Card>
    );
};

export default AccountPage;