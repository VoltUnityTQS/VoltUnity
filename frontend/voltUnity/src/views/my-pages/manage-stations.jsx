import React, { useEffect, useState } from 'react';
import { Card, Button, Table, Form, Row, Col } from 'react-bootstrap';
import { getStations, addStation, addSlot } from '../../services/api';

const ManageStations = () => {
    const [stations, setStations] = useState([]);
    const [newStation, setNewStation] = useState({
        name: '',
        lat: '',
        lng: '',
        address: '',
        totalSlots: 0,
        maxPower: 0,
        pricePerKWh: 0
    });

    const [newSlot, setNewSlot] = useState({
        power: '',
        slotNumber: '',
        type: ''
    });
    const [selectedStationId, setSelectedStationId] = useState(null);

    useEffect(() => {
        fetchStations();
    }, []);

    const fetchStations = async () => {
        const data = await getStations();
        setStations(data);
    };

    const handleAddStation = async () => {
        await addStation(newStation);
        setNewStation({
            name: '',
            lat: '',
            lng: '',
            address: '',
            totalSlots: 0,
            maxPower: 0,
            pricePerKWh: 0
        });
        fetchStations();
    };

    const handleAddSlot = async () => {
        if (selectedStationId) {
            await addSlot(selectedStationId, newSlot);
            setNewSlot({ power: '', slotNumber: '', type: '' });
            fetchStations();
        }
    };

    return (
        <Card>
            <Card.Header>
                <Card.Title as="h5">Gerir Estações</Card.Title>
            </Card.Header>
            <Card.Body>
                <h6>Adicionar nova estação:</h6>
                <Row className="mb-3">
                    {['name', 'lat', 'lng', 'address', 'totalSlots', 'maxPower', 'pricePerKWh'].map(field => (
                        <Col key={field}>
                            <Form.Control
                                placeholder={field}
                                value={newStation[field]}
                                onChange={(e) => setNewStation({ ...newStation, [field]: e.target.value })}
                            />
                        </Col>
                    ))}
                    <Col>
                        <Button variant="primary" onClick={handleAddStation}>
                            Adicionar Estação
                        </Button>
                    </Col>
                </Row>

                <h6 className="mt-4">Lista de estações:</h6>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Morada</th>
                            <th>Slots</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        {stations.map(station => (
                            <tr key={station.id}>
                                <td>{station.id}</td>
                                <td>{station.name}</td>
                                <td>{station.address}</td>
                                <td>{station.totalSlots}</td>
                                <td>
                                    <Button
                                        variant="secondary"
                                        size="sm"
                                        onClick={() => setSelectedStationId(station.id)}
                                    >
                                        Adicionar Slot
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>

                {selectedStationId && (
                    <>
                        <h6 className="mt-4">Adicionar slot à estação {selectedStationId}:</h6>
                        <Row>
                            {['power', 'slotNumber', 'type'].map(field => (
                                <Col key={field}>
                                    <Form.Control
                                        placeholder={field}
                                        value={newSlot[field]}
                                        onChange={(e) => setNewSlot({ ...newSlot, [field]: e.target.value })}
                                    />
                                </Col>
                            ))}
                            <Col>
                                <Button variant="primary" onClick={handleAddSlot}>
                                    Adicionar Slot
                                </Button>
                            </Col>
                        </Row>
                    </>
                )}
            </Card.Body>
        </Card>
    );
};

export default ManageStations;