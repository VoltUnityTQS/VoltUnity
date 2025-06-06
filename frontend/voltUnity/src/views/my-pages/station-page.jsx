// src/views/my-pages/station-page.jsx

import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { FiArrowLeft } from 'react-icons/fi';
import { getStation, createBooking } from '../../services/api';

const getColor = (status) => {
    if (status === 'AVAILABLE') return 'text-success';
    if (status === 'IN_USE') return 'text-warning';
    return 'text-danger';
};

const StationPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const stationId = parseInt(id);

    const [station, setStation] = useState(null);
    const [slots, setSlots] = useState([]);

    useEffect(() => {
        async function fetchStation() {
            try {
                const data = await getStation(stationId);
                setStation({
                    id: data.id,
                    nome: data.name,
                    distancia: 0 // não tens no backend
                });

                const slotsAdapted = data.slots.map(slot => ({
                    id: slot.id,
                    status: slot.slotStatus,
                    capacidadeKW: slot.power,
                    rapido: slot.type && slot.type.toLowerCase().includes('fast')
                }));

                setSlots(slotsAdapted);
            } catch (error) {
                console.error('Erro ao buscar estação:', error);
            }
        }

        fetchStation();
    }, [stationId]);

    const handleBooking = async (slotId) => {
        try {
            const bookingData = {
                slotId: slotId,
                userId: 1, // hardcoded user por agora (ajustar se tiveres auth)
                start: new Date().toISOString(),
                end: new Date(Date.now() + 3600000).toISOString(), // +1h
                priceAtBooking: 5.0,
                bookingStatus: 'CONFIRMED'
            };

            await createBooking(bookingData);
            alert(`Booking criado para Slot ${slotId} na estação ${station?.nome}!`);
        } catch (error) {
            console.error('Erro ao criar booking:', error);
            alert('Erro ao criar booking.');
        }
    };

    if (!station) return <div>Estação não encontrada.</div>;

    return (
        <Card>
            <Card.Header className="d-flex align-items-center gap-2 justify-content-between">
                <Card.Title as="h5" className="mb-0">
                    {station.nome} ({station.distancia} km)
                </Card.Title>
            </Card.Header>
            <Card.Body>
                <Button variant="secondary" onClick={() => navigate(-1)} className="mb-3">
                    <FiArrowLeft className="me-1" /> Voltar
                </Button>
                <Table hover>
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th>Slot</th>
                            <th>Capacidade (kW)</th>
                            <th>Rápido</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {slots.map((slot) => (
                            <tr key={slot.id}>
                                <td><MdCircle className={getColor(slot.status)} /></td>
                                <td>Slot #{slot.id}</td>
                                <td>{slot.capacidadeKW} kW</td>
                                <td>{slot.rapido ? 'Sim' : 'Não'}</td>
                                <td>
                                    <Button
                                        variant="primary"
                                        disabled={slot.status !== 'AVAILABLE'}
                                        onClick={() => handleBooking(slot.id)}
                                    >
                                        Reservar
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

export default StationPage;