// src/views/my-pages/station-page.jsx

import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Row, Col, Card, Table, Button } from 'react-bootstrap';
import { MdCircle } from 'react-icons/md';
import { FiArrowLeft } from 'react-icons/fi';
import { getStation, createBooking } from '../../services/api';

const getColor = (status) => {
    if (status === 'available') return 'text-success';
    if (status === 'in_use') return 'text-warning';
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
                    distancia: data.distance || 0 // se tiveres esta info
                });
                // Adaptar os slots
                const slotsAdapted = data.slots.map(slot => ({
                    id: slot.id,
                    status: slot.slotStatus,
                    capacidadeKW: slot.capacityKW,
                    rapido: slot.fast
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
            const userData = {
                user: 'user@example.com' // podes melhorar depois (ex: user autenticado)
            };
            await createBooking(stationId, slotId, userData);
            alert(`Booking criado para Slot ${slotId} na estação ${station?.nome}!`);
            // Podes refazer fetch dos slots se quiseres atualizar estado
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
                                        disabled={slot.status !== 'available'}
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